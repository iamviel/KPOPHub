package com.example.kfandomhub.data.model

import com.example.kfandomhub.R

object MemberData {

    val list = listOf(
        Member(1, 1, "Jisoo", "Lead Vocal, Visual", "Jisoo", avatar("Jisoo")),
        Member(2, 1, "Jennie", "Main Rapper, Lead Vocal", "Jennie_(singer)", avatar("Jennie")),
        Member(3, 1, "Rose", "Main Vocal, Lead Dancer", "Rosé_(singer)", avatar("Rose")),
        Member(4, 1, "Lisa", "Main Dancer, Main Rapper", "Lisa_(rapper)", avatar("Lisa")),

        Member(5, 2, "Ruka", "Rapper, Dancer", "Ruka_(singer)", avatar("Ruka")),
        Member(6, 2, "Pharita", "Vocalist", "Pharita", avatar("Pharita")),
        Member(7, 2, "Asa", "Rapper", "Asa_(singer)", avatar("Asa")),
        Member(8, 2, "Ahyeon", "Vocalist, Rapper", "Ahyeon", avatar("Ahyeon")),
        Member(9, 2, "Rami", "Vocalist", "Rami_(singer)", avatar("Rami")),
        Member(10, 2, "Rora", "Vocalist", "Rora_(singer)", avatar("Rora")),
        Member(11, 2, "Chiquita", "Vocalist, Maknae", "Chiquita_(singer)", avatar("Chiquita")),

        Member(12, 3, "Hyunsuk", "Leader, Rapper", "Choi_Hyun-suk", avatar("Hyunsuk")),
        Member(13, 3, "Jihoon", "Leader, Vocalist", "Jihoon_(singer)", avatar("Jihoon")),
        Member(14, 3, "Yoshi", "Rapper", "Yoshi_(singer)", avatar("Yoshi")),
        Member(15, 3, "Junkyu", "Vocalist", "Junkyu", avatar("Junkyu")),
        Member(16, 3, "Jaehyuk", "Vocalist", "Jaehyuk", avatar("Jaehyuk")),
        Member(17, 3, "Asahi", "Vocalist", "Asahi_(singer)", avatar("Asahi")),
        Member(18, 3, "Doyoung", "Dancer", "Doyoung_(singer)", avatar("Doyoung")),
        Member(19, 3, "Haruto", "Rapper", "Haruto_(singer)", avatar("Haruto")),
        Member(20, 3, "Jeongwoo", "Main Vocal", "Park_Jeong-woo", avatar("Jeongwoo")),
        Member(21, 3, "Junghwan", "Maknae", "So_Jung-hwan", avatar("Junghwan")),

        Member(22, 4, "Minji", "Vocalist", "Minji_(singer)", avatar("Minji")),
        Member(23, 4, "Hanni", "Vocalist", "Hanni_(singer)", avatar("Hanni")),
        Member(24, 4, "Danielle", "Vocalist", "Danielle_(singer)", avatar("Danielle")),
        Member(25, 4, "Haerin", "Vocalist", "Haerin", avatar("Haerin")),
        Member(26, 4, "Hyein", "Maknae", "Hyein", avatar("Hyein")),

        Member(27, 5, "Yunah", "Vocalist", "Yunah", avatar("Yunah")),
        Member(28, 5, "Minju", "Vocalist", "Minju_(singer)", avatar("Minju")),
        Member(29, 5, "Moka", "Vocalist", "Moka_(singer)", avatar("Moka")),
        Member(30, 5, "Wonhee", "Vocalist", "Wonhee", avatar("Wonhee")),
        Member(31, 5, "Iroha", "Dancer, Maknae", "Iroha_(singer)", avatar("Iroha")),

        Member(32, 6, "Karina", "Leader, Main Dancer, Rapper", "Karina_(South_Korean_singer)", avatar("Karina")),
        Member(33, 6, "Giselle", "Rapper", "Giselle_(singer)", avatar("Giselle")),
        Member(34, 6, "Winter", "Vocalist, Dancer", "Winter_(singer)", avatar("Winter")),
        Member(35, 6, "Ningning", "Main Vocal", "Ningning", avatar("Ningning")),

        Member(36, 7, "Carmen", "Member", "Hearts2Hearts", avatar("Carmen")),
        Member(37, 7, "Jiwoo", "Member", "Hearts2Hearts", avatar("Jiwoo")),
        Member(38, 7, "Stella", "Member", "Hearts2Hearts", avatar("Stella")),
        Member(39, 7, "Yuha", "Member", "Hearts2Hearts", avatar("Yuha")),
        Member(40, 7, "Ana", "Member", "Hearts2Hearts", avatar("Ana")),
        Member(41, 7, "Ian", "Member", "Hearts2Hearts", avatar("Ian")),
        Member(42, 7, "Yeon", "Member", "Hearts2Hearts", avatar("Yeon")),
        Member(43, 7, "Juun", "Member", "Hearts2Hearts", avatar("Juun")),

        Member(44, 8, "RM", "Leader, Rapper", "RM_(musician)", avatar("RM")),
        Member(45, 8, "Jin", "Vocalist", "Kim_Seok-jin", avatar("Jin")),
        Member(46, 8, "Suga", "Rapper", "Suga", avatar("Suga")),
        Member(47, 8, "J-Hope", "Rapper, Dancer", "J-Hope", avatar("J-Hope")),
        Member(48, 8, "Jimin", "Vocalist, Dancer", "Jimin", avatar("Jimin")),
        Member(49, 8, "V", "Vocalist", "V_(singer)", avatar("V")),
        Member(50, 8, "Jungkook", "Main Vocal", "Jungkook", avatar("Jungkook")),

        Member(51, 9, "Taeyeon", "Leader, Main Vocal", "Taeyeon", avatar("Taeyeon")),
        Member(52, 9, "Sunny", "Vocalist", "Sunny_(singer)", avatar("Sunny")),
        Member(53, 9, "Tiffany", "Vocalist", "Tiffany_Young", avatar("Tiffany")),
        Member(54, 9, "Hyoyeon", "Main Dancer", "Hyoyeon", avatar("Hyoyeon")),
        Member(55, 9, "Yuri", "Dancer, Vocalist", "Kwon_Yu-ri", avatar("Yuri")),
        Member(56, 9, "Sooyoung", "Vocalist", "Choi_Soo-young", avatar("Sooyoung")),
        Member(57, 9, "Yoona", "Vocalist, Visual", "Im_Yoon-ah", avatar("Yoona")),
        Member(58, 9, "Seohyun", "Vocalist", "Seohyun", avatar("Seohyun")),

        Member(59, 10, "Moon Hee-joon", "Leader, Vocalist", "Moon_Hee-joon", avatar("Moon Hee-joon")),
        Member(60, 10, "Jang Woo-hyuk", "Rapper, Dancer", "Jang_Woo-hyuk", avatar("Jang Woo-hyuk")),
        Member(61, 10, "Tony An", "Rapper", "Tony_An", avatar("Tony An")),
        Member(62, 10, "Kangta", "Main Vocal", "Kangta", avatar("Kangta")),
        Member(63, 10, "Lee Jae-won", "Rapper", "Lee_Jae-won_(singer)", avatar("Lee Jae-won"))
    )

    fun getMembersByGroup(groupId: Int): List<Member> {
        return list.filter { it.groupId == groupId }
    }

    private fun avatar(name: String): String {
        val resId = when (name) {
            "Jisoo" -> R.drawable.idol_blackpink_jisoo
            "Jennie" -> R.drawable.idol_blackpink_jennie
            "Rose" -> R.drawable.idol_blackpink_rose
            "Lisa" -> R.drawable.idol_blackpink_lisa
            "Ruka" -> R.drawable.idol_babymonster_ruka
            "Pharita" -> R.drawable.idol_babymonster_pharita
            "Asa" -> R.drawable.idol_babymonster_asa
            "Ahyeon" -> R.drawable.idol_babymonster_ahyeon
            "Rami" -> R.drawable.idol_babymonster_rami
            "Rora" -> R.drawable.idol_babymonster_rora
            "Chiquita" -> R.drawable.idol_babymonster_chiquita
            "Hyunsuk" -> R.drawable.idol_treasure_hyunsuk
            "Jihoon" -> R.drawable.idol_treasure_jihoon
            "Yoshi" -> R.drawable.idol_treasure_yoshi
            "Junkyu" -> R.drawable.idol_treasure_junkyu
            "Jaehyuk" -> R.drawable.idol_treasure_jaehyuk
            "Asahi" -> R.drawable.idol_treasure_asahi
            "Doyoung" -> R.drawable.idol_treasure_doyoung
            "Haruto" -> R.drawable.idol_treasure_haruto
            "Jeongwoo" -> R.drawable.idol_treasure_jeongwoo
            "Junghwan" -> R.drawable.idol_treasure_junghwan
            "Minji" -> R.drawable.idol_newjeans_minji
            "Hanni" -> R.drawable.idol_newjeans_hanni
            "Danielle" -> R.drawable.idol_newjeans_danielle
            "Haerin" -> R.drawable.idol_newjeans_haerin
            "Hyein" -> R.drawable.idol_newjeans_hyein
            "Yunah" -> R.drawable.idol_illit_yunah
            "Minju" -> R.drawable.idol_illit_minju
            "Moka" -> R.drawable.idol_illit_moka
            "Wonhee" -> R.drawable.idol_illit_wonhee
            "Iroha" -> R.drawable.idol_illit_iroha
            "Karina" -> R.drawable.idol_aespa_karina
            "Giselle" -> R.drawable.idol_aespa_giselle
            "Winter" -> R.drawable.idol_aespa_winter
            "Ningning" -> R.drawable.idol_aespa_ningning
            "Carmen" -> R.drawable.idol_hearts2hearts_carmen
            "Jiwoo" -> R.drawable.idol_hearts2hearts_jiwoo
            "Stella" -> R.drawable.idol_hearts2hearts_stella
            "Yuha" -> R.drawable.idol_hearts2hearts_yuha
            "Ana" -> R.drawable.idol_hearts2hearts_ana
            "Ian" -> R.drawable.idol_hearts2hearts_ian
            "Yeon" -> R.drawable.idol_hearts2hearts_yeon
            "Juun" -> R.drawable.idol_hearts2hearts_juun
            "RM" -> R.drawable.idol_bts_rm
            "Jin" -> R.drawable.idol_bts_jin
            "Suga" -> R.drawable.idol_bts_suga
            "J-Hope" -> R.drawable.idol_bts_jhope
            "Jimin" -> R.drawable.idol_bts_jimin
            "V" -> R.drawable.idol_bts_v
            "Jungkook" -> R.drawable.idol_bts_jungkook
            "Taeyeon" -> R.drawable.idol_girls_generation_taeyeon
            "Sunny" -> R.drawable.idol_girls_generation_sunny
            "Tiffany" -> R.drawable.idol_girls_generation_tiffany
            "Hyoyeon" -> R.drawable.idol_girls_generation_hyoyeon
            "Yuri" -> R.drawable.idol_girls_generation_yuri
            "Sooyoung" -> R.drawable.idol_girls_generation_sooyoung
            "Yoona" -> R.drawable.idol_girls_generation_yoona
            "Seohyun" -> R.drawable.idol_girls_generation_seohyun
            "Moon Hee-joon" -> R.drawable.idol_hot_moon_hee_joon
            "Jang Woo-hyuk" -> R.drawable.idol_hot_jang_woo_hyuk
            "Tony An" -> R.drawable.idol_hot_tony_an
            "Kangta" -> R.drawable.idol_hot_kangta
            "Lee Jae-won" -> R.drawable.idol_hot_lee_jae_won
            else -> null
        }
        if (resId != null) return localDrawable(resId)

        val encoded = name.replace(" ", "+")
        return "https://ui-avatars.com/api/?name=$encoded&background=7B61FF&color=FFFFFF&bold=true&size=320"
    }

    private fun localDrawable(resId: Int): String {
        return "android.resource://com.example.kfandomhub/$resId"
    }
}
