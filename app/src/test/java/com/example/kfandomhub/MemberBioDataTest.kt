package com.example.kfandomhub

import com.example.kfandomhub.data.model.Member
import com.example.kfandomhub.data.model.MemberBioData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MemberBioDataTest {

    @Test
    fun parse_mapsCsvColumnsToMemberBio() {
        val csv = """
            Nama Idol,Nama Panggung,Nama Lahir,Tanggal Lahir,Zodiak,Zodiak China,Tinggi,Berat,Golongan Darah,MBTI,Kewarganegaraan,Asal/Kelahiran,Instagram,Sumber
            Karina,Karina,Yu Ji-min,2000-04-11,Aries,Dragon,167 cm,45 kg,B,ENFP,Korea Selatan,"Suwon, Korea Selatan",@katarinabluu,https://example.com/karina
        """.trimIndent()

        val result = MemberBioData.parse(csv)

        assertTrue(result.containsKey("Karina"))
        assertEquals("Karina", result["Karina"]?.stageName)
        assertEquals("Yu Ji-min", result["Karina"]?.birthName)
        assertEquals("Suwon, Korea Selatan", result["Karina"]?.birthplace)
        assertEquals("@katarinabluu", result["Karina"]?.instagram)
    }

    @Test
    fun attach_returnsMemberWithBiodataWhenNameMatches() {
        val member = Member(
            id = 1,
            groupId = 1,
            name = "Karina",
            position = "Leader",
            wikiTitle = "Karina_(singer)",
            photoUrl = "android.resource://photo"
        )
        val bioByName = MemberBioData.parse(
            """
                Nama Idol,Nama Panggung,Nama Lahir,Tanggal Lahir,Zodiak,Zodiak China,Tinggi,Berat,Golongan Darah,MBTI,Kewarganegaraan,Asal/Kelahiran,Instagram,Sumber
                Karina,Karina,Yu Ji-min,2000-04-11,Aries,Dragon,167 cm,45 kg,B,ENFP,Korea Selatan,Suwon,@katarinabluu,https://example.com/karina
            """.trimIndent()
        )

        val result = MemberBioData.attach(member, bioByName)

        assertEquals("Yu Ji-min", result.birthName)
        assertEquals("2000-04-11", result.birthday)
        assertEquals("ENFP", result.mbtiType)
        assertEquals("https://example.com/karina", result.sourceUrl)
    }
}
