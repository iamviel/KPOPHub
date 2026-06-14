package com.example.kfandomhub.data.repository

import android.content.Context
import android.util.Log
import com.example.kfandomhub.BuildConfig
import com.example.kfandomhub.data.api.WikipediaApiService
import com.example.kfandomhub.data.api.YouTubeApiService
import com.example.kfandomhub.data.model.Group
import com.example.kfandomhub.data.model.GroupData
import com.example.kfandomhub.data.model.Member
import com.example.kfandomhub.data.model.MemberBioData
import com.example.kfandomhub.data.model.MusicRadarPage
import com.example.kfandomhub.data.model.MusicRadarCategory
import com.example.kfandomhub.data.model.TrendingItem
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.kfandomhub.data.model.YouTubeVideo
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KpopRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wikipediaApiService: WikipediaApiService,
    private val youTubeApiService: YouTubeApiService
) {
    private companion object {
        const val DEBUG_TAG = "KFandomHubDebug"
    }

    private val imageCache = mutableMapOf<String, String?>()
    private val memberBioByName by lazy { loadMemberBioByName() }
    private val youtubeApiKey = BuildConfig.YOUTUBE_API_KEY
    private val kpopPlaylistId = BuildConfig.YOUTUBE_KPOP_PLAYLIST_ID
    private val kpopTrendingPlaylistId = BuildConfig.YOUTUBE_KPOP_TRENDING_PLAYLIST_ID

    suspend fun getGroups(): List<Group> = withContext(Dispatchers.IO) {
        GroupData.list
    }

    suspend fun getMembers(groupId: Int): List<Member> = withContext(Dispatchers.IO) {
        val group = GroupData.getGroup(groupId)
        val groupImage = group?.let { fetchImage(it.wikiTitle) }

        MemberDataFallback(groupId).map { member ->
            val memberWithBio = MemberBioData.attach(member, memberBioByName)
            if (memberWithBio.photoUrl.startsWith("android.resource://")) return@map memberWithBio

            memberWithBio.copy(photoUrl = fetchImage(member.wikiTitle) ?: groupImage ?: memberWithBio.photoUrl)
        }
    }

    fun hasYouTubeApiKey(): Boolean = youtubeApiKey.isNotBlank()

    suspend fun getMusicRadar(
        category: MusicRadarCategory,
        pageToken: String? = null
    ): MusicRadarPage = withContext(Dispatchers.IO) {
        if (!hasYouTubeApiKey()) {
            Log.w(DEBUG_TAG, "YouTube API skipped: YOUTUBE_API_KEY is empty")
            return@withContext MusicRadarPage(emptyList(), null)
        }

        runCatching {
            var currentToken = pageToken
            var nextToken: String? = pageToken
            val collected = mutableListOf<TrendingItem>()
            val seenIds = mutableSetOf<String>()
            var attempts = 0

            while ((attempts == 0) || (attempts < 3 && collected.size < 20 && nextToken != null)) {
                attempts += 1
                Log.d(
                    DEBUG_TAG,
                    "YouTube ${category.name}: request attempt=$attempts pageToken=${currentToken ?: "FIRST_PAGE"}"
                )
                val page = fetchVideoIds(category, currentToken)
                nextToken = page.nextPageToken

                val ids = page.videoIds
                    .distinct()
                    .filter { seenIds.add(it) }

                Log.d(
                    DEBUG_TAG,
                    "YouTube ${category.name}: fetchedVideoIds=${page.videoIds.size}, newVideoIds=${ids.size}, nextPageToken=${nextToken != null}"
                )

                if (ids.isNotEmpty()) {
                    val details = youTubeApiService.getVideos(
                        ids = ids.joinToString(","),
                        apiKey = youtubeApiKey
                    )
                    val detailsById = details.items.associateBy { it.id }
                    val items = ids.mapNotNull { detailsById[it] }
                        .mapNotNull { it.toTrendingItem() }
                        .filter { it.isLikelyOfficialKpopMv(category) }

                    Log.d(
                        DEBUG_TAG,
                        "YouTube ${category.name}: detailItems=${details.items.size}, passedFilter=${items.size}"
                    )

                    collected += items
                }

                currentToken = nextToken
                if (nextToken == null) break
            }

            val ordered = when (category) {
                MusicRadarCategory.NEW_RELEASE -> collected.sortedByDescending { it.publishedAtMillis }
                MusicRadarCategory.TRENDING_MV -> collected
                MusicRadarCategory.MOST_VIEWED_MV -> collected.sortedByDescending { it.viewCount }
            }.take(20)

            Log.d(
                DEBUG_TAG,
                "YouTube ${category.name}: collected=${collected.size}, returned=${ordered.size}, hasNextPage=${nextToken != null}"
            )
            MusicRadarPage(ordered, nextToken)
        }.onFailure { error ->
            Log.e(DEBUG_TAG, "YouTube ${category.name}: API request failed", error)
        }.getOrDefault(MusicRadarPage(emptyList(), null))
    }

    private data class VideoIdPage(
        val videoIds: List<String>,
        val nextPageToken: String?
    )

    private suspend fun fetchVideoIds(
        category: MusicRadarCategory,
        pageToken: String?
    ): VideoIdPage {
        val playlistId = when (category) {
            MusicRadarCategory.NEW_RELEASE -> kpopPlaylistId
            MusicRadarCategory.TRENDING_MV -> kpopTrendingPlaylistId
            MusicRadarCategory.MOST_VIEWED_MV -> ""
        }

        if (playlistId.isNotBlank()) {
            val page = youTubeApiService.getPlaylistItems(
                playlistId = playlistId,
                maxResults = 50,
                pageToken = pageToken,
                apiKey = youtubeApiKey
            )
            Log.d(
                DEBUG_TAG,
                "YouTube ${category.name}: playlistItems=${page.items.size}, nextPageToken=${page.nextPageToken != null}"
            )
            return VideoIdPage(
                videoIds = page.items.mapNotNull { item ->
                    item.contentDetails?.videoId ?: item.snippet?.resourceId?.videoId
                },
                nextPageToken = page.nextPageToken
            )
        }

        val page = youTubeApiService.searchVideos(
            query = category.query,
            order = category.searchOrder,
            publishedAfter = category.publishedAfter(),
            maxResults = 50,
            pageToken = pageToken,
            apiKey = youtubeApiKey
        )
        Log.d(
            DEBUG_TAG,
            "YouTube ${category.name}: searchItems=${page.items.size}, query='${category.query}', order=${category.searchOrder}, nextPageToken=${page.nextPageToken != null}"
        )
        return VideoIdPage(
            videoIds = page.items.mapNotNull { it.id?.videoId },
            nextPageToken = page.nextPageToken
        )
    }

    private val MusicRadarCategory.query: String
        get() = when (this) {
            MusicRadarCategory.NEW_RELEASE -> "official MV Kpop"
            MusicRadarCategory.TRENDING_MV -> "official MV Kpop"
            MusicRadarCategory.MOST_VIEWED_MV -> "official music video Kpop"
        }

    private val MusicRadarCategory.searchOrder: String
        get() = when (this) {
            MusicRadarCategory.NEW_RELEASE -> "date"
            MusicRadarCategory.TRENDING_MV -> "relevance"
            MusicRadarCategory.MOST_VIEWED_MV -> "viewCount"
        }

    private fun MusicRadarCategory.publishedAfter(): String? {
        val days = when (this) {
            MusicRadarCategory.NEW_RELEASE -> 45
            MusicRadarCategory.TRENDING_MV -> 60
            MusicRadarCategory.MOST_VIEWED_MV -> null
        } ?: return null

        return apiDateFormat().format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            add(Calendar.DAY_OF_YEAR, -days)
        }.time)
    }

    private fun YouTubeVideo.toTrendingItem(): TrendingItem? {
        val videoId = id ?: return null
        val snippet = snippet ?: return null
        val title = snippet.title?.takeIf { it.isNotBlank() } ?: return null
        val groupName = snippet.channelTitle?.takeIf { it.isNotBlank() } ?: "YouTube"
        val publishedMillis = parsePublishedAt(snippet.publishedAt)
        val views = statistics?.viewCount?.toLongOrNull() ?: 0L

        return TrendingItem(
            videoId = videoId,
            title = title,
            groupName = groupName,
            viewCount = views,
            viewText = formatViews(views),
            releasedAgo = formatReleasedAgo(publishedMillis),
            publishedAtMillis = publishedMillis,
            thumbnailUrl = snippet.thumbnails?.bestUrl().orEmpty(),
            youtubeUrl = "https://www.youtube.com/watch?v=$videoId"
        )
    }

    private fun TrendingItem.isLikelyOfficialKpopMv(category: MusicRadarCategory): Boolean {
        val titleLower = title.lowercase(Locale.US)
        val channelLower = groupName.lowercase(Locale.US)
        val normalizedTitle = " $titleLower "
            .replace("(", " ")
            .replace(")", " ")
            .replace("[", " ")
            .replace("]", " ")
            .replace("-", " ")
            .replace("_", " ")
        val hasOfficialMvSignal =
            titleLower.contains("official mv") ||
                titleLower.contains("official m/v") ||
                titleLower.contains("official music video") ||
                titleLower.contains("[mv]") ||
                titleLower.contains("[m/v]") ||
                normalizedTitle.contains(" m/v ") ||
                normalizedTitle.contains(" mv ")
        val blockedTerms = listOf(
            " ai",
            "ai ",
            "ai music",
            "#ai",
            "a.i.",
            "cover",
            "reaction",
            "lyric",
            "lyrics",
            "audio",
            "teaser",
            "trailer",
            "dance practice",
            "performance video",
            "fanmade",
            "karaoke",
            "instrumental",
            "challenge",
            "shorts",
            "rumi",
            "huntr/x",
            "demon hunters",
            "remix",
            "edit",
            "sped up",
            "nightcore",
            "tutorial",
            "gameplay",
            "roblox",
            "minecraft",
            "tiktok",
            "compilation",
            "playlist",
            "full album",
            "stage mix",
            "line distribution",
            "color coded",
            "sub español",
            "sub indo",
            "sub español",
            "legendado",
            "making film",
            "behind",
            "bts reaction",
            "concept film"
        )
        val blockedChannels = listOf(
            "mania",
            "cover",
            "reaction",
            "lyrics",
            "karaoke",
            "ai",
            "musicuno",
            "fan",
            "archive",
            "random",
            "playlist",
            "compilation",
            "shorts",
            "studio",
            "melody",
            "joyful",
            "musica",
            "topic"
        )
        val officialChannelSignals = listOf(
            "hybe labels",
            "jyp entertainment",
            "smtown",
            "yg entertainment",
            "blackpink",
            "babymonster",
            "treasure",
            "bighit music",
            "bangtantv",
            "1thek",
            "stone music entertainment",
            "mnet k-pop",
            "starshiptv",
            "starship entertainment",
            "kq entertainment",
            "pledis entertainment",
            "ador",
            "belift lab",
            "cube entertainment",
            "woollim",
            "wm entertainment",
            "rbw",
            "source music",
            "fantagio",
            "fnc entertainment",
            "ist entertainment",
            "high up entertainment",
            "modhaus",
            "dreamcatcher company",
            "more vision",
            "aomg",
            "pnation",
            "konnect entertainment",
            "yuehua entertainment",
            "wakeone",
            "xgalx",
            "aespa",
            "newjeans",
            "illit",
            "ive",
            "le sserafim",
            "twice",
            "stray kids",
            "seventeen",
            "enhypen",
            "tomorrow x together",
            "txt",
            "itzy",
            "nmixx",
            "nct",
            "riize",
            "zerobaseone",
            "zb1",
            "ateez",
            "xikers",
            "the boyz",
            "stayc",
            "kiss of life",
            "p1harmony",
            "monsta x",
            "cravity",
            "red velvet",
            "exo",
            "shinee",
            "taemin",
            "key",
            "girls' generation",
            "snsd",
            "iu official",
            "akmu"
        )
        val officialArtistSignals = listOf(
            "blackpink",
            "baby monster",
            "babymonster",
            "treasure",
            "newjeans",
            "new jeans",
            "illit",
            "aespa",
            "bts",
            "girls' generation",
            "snsd",
            "h.o.t.",
            "ive",
            "le sserafim",
            "lesserafim",
            "twice",
            "stray kids",
            "seventeen",
            "enhypen",
            "tomorrow x together",
            "txt",
            "itzy",
            "nmixx",
            "ateez",
            "xikers",
            "the boyz",
            "stayc",
            "kiss of life",
            "p1harmony",
            "monsta x",
            "cravity",
            "red velvet",
            "exo",
            "shinee",
            "nct",
            "riize",
            "zerobaseone",
            "zb1",
            "kep1er",
            "gidle",
            "(g)i-dle",
            "dreamcatcher",
            "fromis_9",
            "tripleS",
            "xg",
            "k-pop",
            "kpop",
            "방탄",
            "블랙핑크",
            "아이브",
            "에스파",
            "뉴진스",
            "르세라핌",
            "베이비몬스터",
            "트와이스",
            "세븐틴",
            "스트레이 키즈",
            "엔하이픈",
            "투모로우바이투게더",
            "있지",
            "엔믹스",
            "제로베이스원",
            "라이즈",
            "에이티즈"
        )
        val enoughViewsForMostViewed = category != MusicRadarCategory.MOST_VIEWED_MV || viewCount >= 1_000_000L
        val fromOfficialChannel = officialChannelSignals.any { channelLower.contains(it) }
        val titleHasKnownArtist = officialArtistSignals.any { titleLower.contains(it) }
        val channelHasKnownArtist = officialArtistSignals.any { channelLower.contains(it) }
        val titleLooksLikeOfficialMv = hasOfficialMvSignal && !titleLower.contains("official audio")

        return titleLooksLikeOfficialMv &&
            blockedTerms.none { titleLower.contains(it) } &&
            blockedChannels.none { channelLower.contains(it) } &&
            enoughViewsForMostViewed &&
            (
                fromOfficialChannel ||
                    (titleHasKnownArtist && channelHasKnownArtist)
            )
    }

    private fun parsePublishedAt(value: String?): Long {
        if (value.isNullOrBlank()) return 0L

        return listOf(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        ).firstNotNullOfOrNull { pattern ->
            runCatching {
                SimpleDateFormat(pattern, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.parse(value)?.time
            }.getOrNull()
        } ?: 0L
    }

    private fun apiDateFormat(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    private fun formatViews(count: Long): String {
        val formatter = DecimalFormat("#.#")
        return when {
            count >= 1_000_000_000L -> "${formatter.format(count / 1_000_000_000.0)}B views"
            count >= 1_000_000L -> "${formatter.format(count / 1_000_000.0)}M views"
            count >= 1_000L -> "${formatter.format(count / 1_000.0)}K views"
            else -> "$count views"
        }
    }

    private fun formatReleasedAgo(publishedMillis: Long): String {
        if (publishedMillis <= 0L) return "Release date unavailable"

        val diff = (System.currentTimeMillis() - publishedMillis).coerceAtLeast(0L)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            days < 1L -> "${TimeUnit.MILLISECONDS.toHours(diff).coerceAtLeast(1L)} hours ago"
            days < 7L -> "$days days ago"
            days < 30L -> "${(days / 7L).coerceAtLeast(1L)} weeks ago"
            days < 365L -> "${(days / 30L).coerceAtLeast(1L)} months ago"
            else -> "${(days / 365L).coerceAtLeast(1L)} years ago"
        }
    }

    private fun MemberDataFallback(groupId: Int): List<Member> {
        return GroupData.getGroup(groupId)?.members.orEmpty()
    }

    private fun loadMemberBioByName() = runCatching {
        context.assets.open("kpop_idol_biodata.csv").bufferedReader(Charsets.UTF_8).use { reader ->
            MemberBioData.parse(reader.readText()).also { biodata ->
                Log.d(DEBUG_TAG, "Local biodata loaded: totalMembers=${biodata.size}")
            }
        }
    }.onFailure { error ->
        Log.e(DEBUG_TAG, "Local biodata failed to load", error)
    }.getOrDefault(emptyMap())

    private suspend fun fetchImage(wikiTitle: String): String? {
        imageCache[wikiTitle]?.let {
            Log.d(DEBUG_TAG, "Wikipedia image cache hit: title='$wikiTitle'")
            return it
        }
        if (imageCache.containsKey(wikiTitle)) {
            Log.d(DEBUG_TAG, "Wikipedia image cache hit: title='$wikiTitle', imageAvailable=false")
            return null
        }

        return try {
            val summary = wikipediaApiService.getPageSummary(wikiTitle)
            val image = summary.originalImage?.source ?: summary.thumbnail?.source
            imageCache[wikiTitle] = image
            Log.d(
                DEBUG_TAG,
                "Wikipedia summary fetched: title='$wikiTitle', imageAvailable=${image != null}"
            )
            image
        } catch (error: Exception) {
            imageCache[wikiTitle] = null
            Log.e(DEBUG_TAG, "Wikipedia summary failed: title='$wikiTitle'", error)
            null
        }
    }
}
