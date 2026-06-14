package com.example.kfandomhub.data.model

import com.example.kfandomhub.R

object GroupData {
    val list = listOf(
        group(1, "BLACKPINK", "YG Entertainment", 2016, "3rd Gen", "BLINK", "Blackpink", "12.3M", R.drawable.logo_blackpink),
        group(2, "BABYMONSTER", "YG Entertainment", 2023, "5th Gen", "MONSTIEZ", "Babymonster", "9.8M", R.drawable.logo_babymonster),
        group(3, "TREASURE", "YG Entertainment", 2020, "4th Gen", "TREASURE MAKER", "Treasure_(band)", "7.6M", R.drawable.logo_treasure),
        group(4, "NEWJEANS", "ADOR", 2022, "4th Gen", "BUNNIES", "NewJeans", "11.2M", R.drawable.logo_newjeans),
        group(5, "ILLIT", "Belift Lab", 2024, "5th Gen", "GLLIT", "Illit", "8.1M", R.drawable.logo_illit),
        group(6, "AESPA", "SM Entertainment", 2020, "4th Gen", "MY", "Aespa", "10.5M", R.drawable.logo_aespa),
        group(7, "HEARTS2HEARTS", "SM Entertainment", 2025, "5th Gen", "S2U", "Hearts2Hearts", "6.2M", R.drawable.logo_hearts2hearts),
        group(8, "BTS", "Big Hit Music", 2013, "3rd Gen", "ARMY", "BTS", "40.8M", R.drawable.logo_bts),
        group(9, "GIRLS' GENERATION", "SM Entertainment", 2007, "2nd Gen", "SONE", "Girls'_Generation", "13.4M", R.drawable.logo_girls_generation),
        group(10, "H.O.T.", "SM Entertainment", 1996, "1st Gen", "CLUB H.O.T.", "H.O.T.", "2.5M")
    )

    fun getGroup(groupId: Int): Group? = list.firstOrNull { it.id == groupId }

    private fun group(
        id: Int,
        name: String,
        agency: String,
        debut: Int,
        generation: String,
        fandomName: String,
        wikiTitle: String,
        fanCount: String,
        logoResId: Int? = null
    ): Group {
        return Group(
            id = id,
            name = name,
            agency = agency,
            debut = debut,
            generation = generation,
            fandomName = fandomName,
            wikiTitle = wikiTitle,
            imageUrl = logoResId?.let(::localDrawable) ?: avatar(name),
            fanCount = fanCount,
            members = MemberData.getMembersByGroup(id)
        )
    }

    private fun localDrawable(resId: Int): String {
        return "android.resource://com.example.kfandomhub/$resId"
    }

    private fun avatar(name: String): String {
        val encoded = name.replace(" ", "+")
        return "https://ui-avatars.com/api/?name=$encoded&background=2B143F&color=FFFFFF&bold=true&size=320"
    }
}
