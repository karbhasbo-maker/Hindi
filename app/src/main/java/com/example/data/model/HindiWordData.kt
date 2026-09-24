package com.example.data.model

data class HindiWord(
    val hindi: String,
    val english: String,
    val meaning: String,
    val category: String,
    val emoji: String,
    val syllables: List<String>
)

object HindiWordData {

    val CATEGORIES = listOf(
        "All Words",
        "2-Letter (दो अक्षर)",
        "3-Letter (तीन अक्षर)",
        "Matra Words (मात्राएँ)",
        "Animals (पशु-पक्षी)",
        "Nature & Daily"
    )

    val PRESET_WORDS: List<HindiWord> = listOf(
        // 2-Letter Simple Words (बिना मात्रा)
        HindiWord("जल", "Jal", "Water", "2-Letter (दो अक्षर)", "💧", listOf("ज", "ल")),
        HindiWord("घर", "Ghar", "Home", "2-Letter (दो अक्षर)", "🏠", listOf("घ", "र")),
        HindiWord("फल", "Phal", "Fruit", "2-Letter (दो अक्षर)", "🍎", listOf("फ", "ल")),
        HindiWord("नल", "Nal", "Water Tap", "2-Letter (दो अक्षर)", "🚰", listOf("न", "ल")),
        HindiWord("बस", "Bas", "Bus", "2-Letter (दो अक्षर)", "🚌", listOf("ब", "स")),
        HindiWord("जग", "Jag", "Jug / World", "2-Letter (दो अक्षर)", "🏺", listOf("ज", "ग")),
        HindiWord("खत", "Khat", "Letter / Mail", "2-Letter (दो अक्षर)", "✉️", listOf("ख", "त")),
        HindiWord("रथ", "Rath", "Chariot", "2-Letter (दो अक्षर)", "🏹", listOf("र", "थ")),
        HindiWord("वन", "Van", "Forest", "2-Letter (दो अक्षर)", "🌲", listOf("व", "न")),
        HindiWord("दस", "Das", "Ten", "2-Letter (दो अक्षर)", "🔟", listOf("द", "स")),
        HindiWord("धन", "Dhan", "Wealth", "2-Letter (दो अक्षर)", "💰", listOf("ध", "न")),
        HindiWord("सच", "Sach", "Truth", "2-Letter (दो अक्षर)", "🕊️", listOf("स", "च")),

        // 3-Letter Simple Words (बिना मात्रा)
        HindiWord("कमल", "Kamal", "Lotus Flower", "3-Letter (तीन अक्षर)", "🌸", listOf("क", "म", "ल")),
        HindiWord("कलम", "Kalam", "Pen", "3-Letter (तीन अक्षर)", "✒️", listOf("क", "ल", "म")),
        HindiWord("महल", "Mahal", "Palace", "3-Letter (तीन अक्षर)", "🏰", listOf("म", "ह", "ल")),
        HindiWord("सड़क", "Sadak", "Road", "3-Letter (तीन अक्षर)", "🛣️", listOf("स", "ड़", "क")),
        HindiWord("बटन", "Batan", "Button", "3-Letter (तीन अक्षर)", "🔘", listOf("ब", "ट", "न")),
        HindiWord("पवन", "Pawan", "Wind / Breeze", "3-Letter (तीन अक्षर)", "💨", listOf("प", "व", "न")),
        HindiWord("गगन", "Gagan", "Sky", "3-Letter (तीन अक्षर)", "☁️", listOf("ग", "ग", "न")),
        HindiWord("शहद", "Shahad", "Honey", "3-Letter (तीन अक्षर)", "🍯", listOf("श", "ह", "द")),
        HindiWord("मटर", "Matar", "Green Peas", "3-Letter (तीन अक्षर)", "🫛", listOf("म", "ट", "र")),
        HindiWord("नगर", "Nagar", "Town / City", "3-Letter (तीन अक्षर)", "🏙️", listOf("न", "ग", "र")),

        // Aa Matra (आ की मात्रा)
        HindiWord("आम", "Aam", "Mango", "Matra Words (मात्राएँ)", "🥭", listOf("आ", "म")),
        HindiWord("राजा", "Raaja", "King", "Matra Words (मात्राएँ)", "👑", listOf("रा", "जा")),
        HindiWord("तारा", "Taara", "Star", "Matra Words (मात्राएँ)", "⭐", listOf("ता", "रा")),
        HindiWord("ताला", "Taala", "Lock", "Matra Words (मात्राएँ)", "🔒", listOf("ता", "ला")),
        HindiWord("नाव", "Naav", "Boat", "Matra Words (मात्राएँ)", "⛵", listOf("ना", "व")),
        HindiWord("कार", "Kaar", "Car", "Matra Words (मात्राएँ)", "🚗", listOf("का", "र")),
        HindiWord("माला", "Maala", "Garland", "Matra Words (मात्राएँ)", "📿", listOf("मा", "ला")),
        HindiWord("बादल", "Baadal", "Cloud", "Matra Words (मात्राएँ)", "☁️", listOf("बा", "द", "ल")),
        HindiWord("भारत", "Bhaarat", "India", "Matra Words (मात्राएँ)", "🇮🇳", listOf("भा", "र", "त")),

        // Chhoti I Matra (इ की मात्रा)
        HindiWord("किताब", "Kitaab", "Book", "Matra Words (मात्राएँ)", "📖", listOf("कि", "ता", "ब")),
        HindiWord("चिड़िया", "Chidiya", "Bird", "Animals (पशु-पक्षी)", "🐦", listOf("चि", "ड़ि", "या")),
        HindiWord("दिन", "Din", "Day", "Nature & Daily", "☀️", listOf("दि", "न")),
        HindiWord("सिर", "Sir", "Head", "Nature & Daily", "👤", listOf("सि", "र")),
        HindiWord("दिल", "Dil", "Heart", "Nature & Daily", "❤️", listOf("दि", "ल")),
        HindiWord("किसान", "Kisaan", "Farmer", "Nature & Daily", "🌾", listOf("कि", "सा", "न")),
        HindiWord("सितार", "Sitaar", "Sitar (Lute)", "Matra Words (मात्राएँ)", "🪕", listOf("सि", "ता", "र")),
        HindiWord("गिलास", "Gilaas", "Water Glass", "Nature & Daily", "🥛", listOf("गि", "ला", "स")),

        // Badi Ee Matra (ई की मात्रा)
        HindiWord("हाथी", "Haathi", "Elephant", "Animals (पशु-पक्षी)", "🐘", listOf("हा", "थी")),
        HindiWord("पानी", "Paani", "Drinking Water", "Nature & Daily", "💧", listOf("पा", "नी")),
        HindiWord("मछली", "Machhli", "Fish", "Animals (पशु-पक्षी)", "🐟", listOf("म", "छ", "ली")),
        HindiWord("चाबी", "Chaabi", "Key", "Nature & Daily", "🔑", listOf("चा", "बी")),
        HindiWord("खीर", "Kheer", "Sweet Pudding", "Nature & Daily", "🥣", listOf("खी", "र")),
        HindiWord("दीपक", "Deepak", "Oil Lamp", "Nature & Daily", "🪔", listOf("दी", "प", "क")),
        HindiWord("तितली", "Titli", "Butterfly", "Animals (पशु-पक्षी)", "🦋", listOf("ति", "त", "ली")),
        HindiWord("तीर", "Teer", "Arrow", "Nature & Daily", "🏹", listOf("ती", "र")),

        // Chhoti U Matra (उ की मात्रा)
        HindiWord("गुलाब", "Gulaab", "Rose", "Nature & Daily", "🌹", listOf("गु", "ला", "ब")),
        HindiWord("पुल", "Pul", "Bridge", "Nature & Daily", "🌉", listOf("पु", "ल")),
        HindiWord("साबुन", "Saabun", "Soap", "Nature & Daily", "🧼", listOf("सा", "बु", "न")),
        HindiWord("धनुष", "Dhanush", "Bow", "Nature & Daily", "🏹", listOf("ध", "नु", "ष")),
        HindiWord("मुख", "Mukh", "Face", "Nature & Daily", "😊", listOf("मु", "ख")),
        HindiWord("सुबह", "Subah", "Morning", "Nature & Daily", "🌅", listOf("सु", "ब", "ह")),

        // Badi Oo Matra (ऊ की मात्रा)
        HindiWord("सूरज", "Sooraj", "Sun", "Nature & Daily", "☀️", listOf("सू", "र", "ज")),
        HindiWord("दूध", "Doodh", "Milk", "Nature & Daily", "🥛", listOf("दू", "ध")),
        HindiWord("फूल", "Phool", "Flower", "Nature & Daily", "🌺", listOf("फू", "ल")),
        HindiWord("भालू", "Bhaalu", "Bear", "Animals (पशु-पक्षी)", "🐻", listOf("भा", "लू")),
        HindiWord("चूहा", "Chooha", "Mouse", "Animals (पशु-पक्षी)", "🐭", listOf("चू", "हा")),
        HindiWord("तरबूज", "Tarbooj", "Watermelon", "Nature & Daily", "🍉", listOf("त", "र", "बू", "ज")),

        // E Matra (ए की मात्रा)
        HindiWord("सेब", "Seb", "Apple", "Nature & Daily", "🍎", listOf("से", "ब")),
        HindiWord("केला", "Kela", "Banana", "Nature & Daily", "🍌", listOf("के", "ला")),
        HindiWord("शेर", "Sher", "Lion", "Animals (पशु-पक्षी)", "🦁", listOf("शे", "र")),
        HindiWord("पेड़", "Ped", "Tree", "Nature & Daily", "🌳", listOf("पे", "ड़")),
        HindiWord("रेल", "Rel", "Train", "Nature & Daily", "🚆", listOf("रे", "ल")),

        // Ai Matra (ऐ की मात्रा)
        HindiWord("बैल", "Bail", "Bull / Ox", "Animals (पशु-पक्षी)", "🐂", listOf("बै", "ल")),
        HindiWord("पैर", "Pair", "Foot / Leg", "Nature & Daily", "🦶", listOf("पै", "र")),
        HindiWord("पैसा", "Paisa", "Money / Coin", "Nature & Daily", "🪙", listOf("पै", "सा")),
        HindiWord("सैनिक", "Sainik", "Soldier", "Nature & Daily", "🪖", listOf("सै", "नि", "क")),
        HindiWord("मैना", "Maina", "Myna Bird", "Animals (पशु-पक्षी)", "🐦", listOf("मै", "ना")),

        // O Matra (ओ की मात्रा)
        HindiWord("मोर", "Mor", "Peacock", "Animals (पशु-पक्षी)", "🦚", listOf("मो", "र")),
        HindiWord("तोता", "Tota", "Parrot", "Animals (पशु-पक्षी)", "🦜", listOf("तो", "ता")),
        HindiWord("ढोल", "Dhol", "Drum", "Nature & Daily", "🥁", listOf("ढो", "ल")),
        HindiWord("कोट", "Kot", "Coat", "Nature & Daily", "🧥", listOf("को", "ट")),
        HindiWord("समोसा", "Samosa", "Samosa", "Nature & Daily", "🥟", listOf("स", "मो", "सा")),

        // Au Matra (औ की मात्रा)
        HindiWord("पौधा", "Paudha", "Plant", "Nature & Daily", "🪴", listOf("पौ", "धा")),
        HindiWord("नौका", "Nauka", "Small Boat", "Nature & Daily", "⛵", listOf("नौ", "का")),
        HindiWord("औरत", "Aurat", "Woman", "Nature & Daily", "👩", listOf("औ", "र", "त")),
        HindiWord("खिलौना", "Khilauna", "Toy", "Nature & Daily", "🧸", listOf("खि", "लौ", "ना")),

        // An & Ah (अं / अः)
        HindiWord("पतंग", "Patang", "Kite", "Nature & Daily", "🪁", listOf("प", "तं", "ग")),
        HindiWord("बंदर", "Bandar", "Monkey", "Animals (पशु-पक्षी)", "🐒", listOf("बं", "द", "र")),
        HindiWord("अंडा", "Anda", "Egg", "Nature & Daily", "🥚", listOf("अं", "डा")),
        HindiWord("शंख", "Shankh", "Conch Shell", "Nature & Daily", "🐚", listOf("शं", "ख")),
        HindiWord("नमः", "Namah", "Salutation / Bow", "Nature & Daily", "🙏", listOf("न", "मः"))
    )

    fun findWord(hindi: String): HindiWord? {
        val clean = hindi.trim()
        return PRESET_WORDS.firstOrNull { it.hindi == clean }
    }

    /**
     * Attempts simple phonetic transliteration for any custom made Hindi word.
     */
    fun approximateTransliteration(syllables: List<String>): String {
        if (syllables.isEmpty()) return ""

        val fullWord = syllables.joinToString("")
        val matched = findWord(fullWord)
        if (matched != null) return matched.english

        val parts = mutableListOf<String>()
        for (syl in syllables) {
            // Check in barahkhadi combinations
            val comb = BarahkhadiData.getAllCombinations().firstOrNull { it.hindi == syl }
            if (comb != null) {
                parts.add(comb.english.lowercase().replaceFirstChar { it.uppercase() })
            } else {
                val vowel = BarahkhadiData.MATRAS.firstOrNull { it.vowel == syl }
                if (vowel != null) {
                    parts.add(vowel.english)
                } else {
                    parts.add(syl)
                }
            }
        }
        return parts.joinToString("")
    }
}
