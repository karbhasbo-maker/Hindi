package com.example.data.model

data class Matra(
    val vowel: String,
    val symbol: String,
    val english: String,
    val phonetic: String,
    val swarName: String,
    val exampleKa: String,
    val explanation: String
)

data class Consonant(
    val char: String,
    val english: String,
    val varga: String,
    val description: String
)

data class SyllableCombination(
    val hindi: String,
    val english: String,
    val consonant: Consonant,
    val matra: Matra,
    val matraIndex: Int
)

object BarahkhadiData {

    val MATRAS: List<Matra> = listOf(
        Matra(
            vowel = "अ",
            symbol = "",
            english = "a",
            phonetic = "a as in America",
            swarName = "ह्रस्व अ (Inherent Vowel)",
            exampleKa = "Ka",
            explanation = "Default inherent sound in every Devanagari consonant. No extra matra mark."
        ),
        Matra(
            vowel = "आ",
            symbol = "ा",
            english = "aa",
            phonetic = "aa as in Father",
            swarName = "दीर्घ आ (Long A)",
            exampleKa = "Kaa",
            explanation = "Vertical stem attached to the right of the consonant. Deep open vowel sound."
        ),
        Matra(
            vowel = "इ",
            symbol = "ि",
            english = "i",
            phonetic = "i as in Sit",
            swarName = "ह्रस्व इ (Short I)",
            exampleKa = "Ki",
            explanation = "Stem on the left that curves overhead to the right. Crisp short vowel sound."
        ),
        Matra(
            vowel = "ई",
            symbol = "ी",
            english = "ee",
            phonetic = "ee as in Feet",
            swarName = "दीर्घ ई (Long EE)",
            exampleKa = "Kee",
            explanation = "Stem on the right curving overhead to the left. Prolonged high front vowel sound."
        ),
        Matra(
            vowel = "उ",
            symbol = "ु",
            english = "u",
            phonetic = "u as in Put",
            swarName = "ह्रस्व उ (Short U)",
            exampleKa = "Ku",
            explanation = "Small curve loop pointing upwards under the consonant baseline. Short rounded sound."
        ),
        Matra(
            vowel = "ऊ",
            symbol = "ू",
            english = "oo",
            phonetic = "oo as in Moon",
            swarName = "दीर्घ ऊ (Long OO)",
            exampleKa = "Koo",
            explanation = "Tail hook curving downwards under the consonant. Extended deep rounded sound."
        ),
        Matra(
            vowel = "ए",
            symbol = "े",
            english = "e",
            phonetic = "e as in Ray",
            swarName = "ए (E Sound)",
            exampleKa = "Ke",
            explanation = "Single diagonal slant (matra) placed on top of the headline (Shirorekha)."
        ),
        Matra(
            vowel = "ऐ",
            symbol = "ै",
            english = "ai",
            phonetic = "ai as in My / Cat",
            swarName = "ऐ (AI Sound)",
            exampleKa = "Kai",
            explanation = "Double diagonal slants placed above the headline for diphthong /ai/."
        ),
        Matra(
            vowel = "ओ",
            symbol = "ो",
            english = "o",
            phonetic = "o as in Go",
            swarName = "ओ (O Sound)",
            exampleKa = "Ko",
            explanation = "Combination of vertical stem on the right with a top slant above the headline."
        ),
        Matra(
            vowel = "औ",
            symbol = "ौ",
            english = "au",
            phonetic = "au as in Cow",
            swarName = "औ (AU Sound)",
            exampleKa = "Kau",
            explanation = "Vertical stem on the right with double slants overhead. Rich diphthong sound."
        ),
        Matra(
            vowel = "अं",
            symbol = "ं",
            english = "am",
            phonetic = "am as in Hum",
            swarName = "अनुस्वार (Anusvara)",
            exampleKa = "Kam",
            explanation = "Dot (bindu) placed directly above the consonant. Produces nasalized ending."
        ),
        Matra(
            vowel = "अः",
            symbol = "ः",
            english = "ah",
            phonetic = "ah as in Aha",
            swarName = "विसर्ग (Visarga)",
            exampleKa = "Kah",
            explanation = "Two vertical dots placed immediately after the consonant. Voiceless aspiration."
        )
    )

    val CONSONANTS: List<Consonant> = listOf(
        // क-वर्ग (Velars)
        Consonant("क", "K", "क-वर्ग", "Velar voiceless unaspirated"),
        Consonant("ख", "Kh", "क-वर्ग", "Velar voiceless aspirated"),
        Consonant("ग", "G", "क-वर्ग", "Velar voiced unaspirated"),
        Consonant("घ", "Gh", "क-वर्ग", "Velar voiced aspirated"),
        Consonant("ङ", "Ng", "क-वर्ग", "Velar nasal"),

        // च-वर्ग (Palatals)
        Consonant("च", "Ch", "च-वर्ग", "Palatal voiceless unaspirated"),
        Consonant("छ", "Chh", "च-वर्ग", "Palatal voiceless aspirated"),
        Consonant("ज", "J", "च-वर्ग", "Palatal voiced unaspirated"),
        Consonant("झ", "Jh", "च-वर्ग", "Palatal voiced aspirated"),
        Consonant("ञ", "Ny", "च-वर्ग", "Palatal nasal"),

        // ट-वर्ग (Retroflexes)
        Consonant("ट", "T", "ट-वर्ग", "Retroflex voiceless unaspirated"),
        Consonant("ठ", "Th", "ट-वर्ग", "Retroflex voiceless aspirated"),
        Consonant("ड", "D", "ट-वर्ग", "Retroflex voiced unaspirated"),
        Consonant("ढ", "Dh", "ट-वर्ग", "Retroflex voiced aspirated"),
        Consonant("ण", "N", "ट-वर्ग", "Retroflex nasal"),

        // त-वर्ग (Dentals)
        Consonant("त", "t", "त-वर्ग", "Dental voiceless unaspirated"),
        Consonant("थ", "th", "त-वर्ग", "Dental voiceless aspirated"),
        Consonant("द", "d", "त-वर्ग", "Dental voiced unaspirated"),
        Consonant("ध", "dh", "त-वर्ग", "Dental voiced aspirated"),
        Consonant("न", "n", "त-वर्ग", "Dental nasal"),

        // प-वर्ग (Labials)
        Consonant("प", "P", "प-वर्ग", "Labial voiceless unaspirated"),
        Consonant("फ", "Ph", "प-वर्ग", "Labial voiceless aspirated"),
        Consonant("ब", "B", "प-वर्ग", "Labial voiced unaspirated"),
        Consonant("भ", "Bh", "प-वर्ग", "Labial voiced aspirated"),
        Consonant("म", "M", "प-वर्ग", "Labial nasal"),

        // अंतःस्थ (Semivowels)
        Consonant("य", "Y", "अंतःस्थ", "Palatal approximant"),
        Consonant("र", "R", "अंतःस्थ", "Alveolar trill/tap"),
        Consonant("ल", "L", "अंतःस्थ", "Alveolar lateral approximant"),
        Consonant("व", "V", "अंतःस्थ", "Labio-dental approximant"),

        // ऊष्म (Sibilants & Fricatives)
        Consonant("श", "Sh", "ऊष्म", "Palato-alveolar sibilant"),
        Consonant("ष", "Shh", "ऊष्म", "Retroflex sibilant"),
        Consonant("स", "S", "ऊष्म", "Alveolar sibilant"),
        Consonant("ह", "H", "ऊष्म", "Glottal fricative"),

        // संयुक्त (Conjuncts)
        Consonant("क्ष", "Ksh", "संयुक्त", "Conjunct: K + Sh"),
        Consonant("त्र", "Tr", "संयुक्त", "Conjunct: T + R"),
        Consonant("ज्ञ", "Gy", "संयुक्त", "Conjunct: J + Ny")
    )

    val VARGA_CATEGORIES: List<String> = listOf(
        "All",
        "क-वर्ग",
        "च-वर्ग",
        "ट-वर्ग",
        "त-वर्ग",
        "प-वर्ग",
        "अंतःस्थ",
        "ऊष्म",
        "संयुक्त"
    )

    fun getCombination(consonant: Consonant, matra: Matra, matraIndex: Int = -1): SyllableCombination {
        val actualIndex = if (matraIndex >= 0) matraIndex else MATRAS.indexOf(matra).coerceAtLeast(0)
        
        // Special case for Ra + u/oo (रु, रू)
        val hindi = if (consonant.char == "र") {
            when (matra.vowel) {
                "उ" -> "रु"
                "ऊ" -> "रू"
                else -> consonant.char + matra.symbol
            }
        } else {
            consonant.char + matra.symbol
        }

        val rawEng = consonant.english + matra.english
        val formattedEng = rawEng.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

        return SyllableCombination(
            hindi = hindi,
            english = formattedEng,
            consonant = consonant,
            matra = matra,
            matraIndex = actualIndex
        )
    }

    fun getAllCombinations(): List<SyllableCombination> {
        val list = mutableListOf<SyllableCombination>()
        for (consonant in CONSONANTS) {
            MATRAS.forEachIndexed { index, matra ->
                list.add(getCombination(consonant, matra, index))
            }
        }
        return list
    }
}
