package com.example.kfandomhub.data.model

object MemberBioData {
    fun parse(csv: String): Map<String, MemberBio> {
        val rows = parseCsv(csv).filter { row -> row.any { it.isNotBlank() } }
        if (rows.isEmpty()) return emptyMap()

        val headers = rows.first()
        val headerIndex = headers.withIndex().associate { it.value to it.index }

        fun List<String>.value(header: String): String {
            val index = headerIndex[header] ?: return ""
            return getOrNull(index).orEmpty()
        }

        return rows.drop(1).associate { row ->
            row.value("Nama Idol") to MemberBio(
                stageName = row.value("Nama Panggung"),
                birthName = row.value("Nama Lahir"),
                birthday = row.value("Tanggal Lahir"),
                zodiacSign = row.value("Zodiak"),
                chineseZodiacSign = row.value("Zodiak China"),
                height = row.value("Tinggi"),
                weight = row.value("Berat"),
                bloodType = row.value("Golongan Darah"),
                mbtiType = row.value("MBTI"),
                nationality = row.value("Kewarganegaraan"),
                birthplace = row.value("Asal/Kelahiran"),
                instagram = row.value("Instagram"),
                sourceUrl = row.value("Sumber")
            )
        }
    }

    fun attach(member: Member, bioByName: Map<String, MemberBio>): Member {
        val bio = bioByName[member.name] ?: return member
        return member.copy(
            stageName = bio.stageName,
            birthName = bio.birthName,
            birthday = bio.birthday,
            zodiacSign = bio.zodiacSign,
            chineseZodiacSign = bio.chineseZodiacSign,
            height = bio.height,
            weight = bio.weight,
            bloodType = bio.bloodType,
            mbtiType = bio.mbtiType,
            nationality = bio.nationality,
            birthplace = bio.birthplace,
            instagram = bio.instagram,
            sourceUrl = bio.sourceUrl
        )
    }

    private fun parseCsv(csv: String): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        val row = mutableListOf<String>()
        val cell = StringBuilder()
        var inQuotes = false
        var index = 0

        while (index < csv.length) {
            val char = csv[index]
            when {
                char == '"' && inQuotes && csv.getOrNull(index + 1) == '"' -> {
                    cell.append('"')
                    index += 1
                }
                char == '"' -> inQuotes = !inQuotes
                char == ',' && !inQuotes -> {
                    row.add(cell.toString())
                    cell.clear()
                }
                (char == '\n' || char == '\r') && !inQuotes -> {
                    if (char == '\r' && csv.getOrNull(index + 1) == '\n') index += 1
                    row.add(cell.toString())
                    cell.clear()
                    rows.add(row.toList())
                    row.clear()
                }
                else -> cell.append(char)
            }
            index += 1
        }

        if (cell.isNotEmpty() || row.isNotEmpty()) {
            row.add(cell.toString())
            rows.add(row.toList())
        }

        return rows
    }
}

data class MemberBio(
    val stageName: String,
    val birthName: String,
    val birthday: String,
    val zodiacSign: String,
    val chineseZodiacSign: String,
    val height: String,
    val weight: String,
    val bloodType: String,
    val mbtiType: String,
    val nationality: String,
    val birthplace: String,
    val instagram: String,
    val sourceUrl: String
)
