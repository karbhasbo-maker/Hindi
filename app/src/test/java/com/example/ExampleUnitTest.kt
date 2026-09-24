package com.example

import com.example.data.model.BarahkhadiData
import com.example.data.model.HindiWordData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun barahkhadi_consonantsAndMatrasCount_isCorrect() {
        assertEquals(36, BarahkhadiData.CONSONANTS.size)
        assertEquals(12, BarahkhadiData.MATRAS.size)
        assertEquals(9, BarahkhadiData.VARGA_CATEGORIES.size)
    }

    @Test
    fun barahkhadi_totalCombinations_is432() {
        val all = BarahkhadiData.getAllCombinations()
        assertEquals(36 * 12, all.size)
        assertEquals(432, all.size)
    }

    @Test
    fun barahkhadi_specialCases_handledCorrectly() {
        val ra = BarahkhadiData.CONSONANTS.first { it.char == "र" }
        val matraU = BarahkhadiData.MATRAS.first { it.vowel == "उ" }
        val matraOo = BarahkhadiData.MATRAS.first { it.vowel == "ऊ" }

        val combRu = BarahkhadiData.getCombination(ra, matraU)
        val combRoo = BarahkhadiData.getCombination(ra, matraOo)

        assertEquals("रु", combRu.hindi)
        assertEquals("रू", combRoo.hindi)
    }

    @Test
    fun barahkhadi_kaCombinations_isAccurate() {
        val ka = BarahkhadiData.CONSONANTS.first { it.char == "क" }
        val matraAa = BarahkhadiData.MATRAS.first { it.vowel == "आ" }
        val combKaa = BarahkhadiData.getCombination(ka, matraAa)

        assertEquals("का", combKaa.hindi)
        assertEquals("Kaa", combKaa.english)
    }

    @Test
    fun wordMaker_presetWords_arePopulatedAndValid() {
        assertTrue(HindiWordData.PRESET_WORDS.size >= 50)
        val kamal = HindiWordData.findWord("कमल")
        assertNotNull(kamal)
        assertEquals("Lotus Flower", kamal?.meaning)
        assertEquals(listOf("क", "म", "ल"), kamal?.syllables)
    }

    @Test
    fun wordMaker_approximateTransliteration_works() {
        val trans = HindiWordData.approximateTransliteration(listOf("कि", "ता", "ब"))
        assertEquals("Kitaab", trans)
    }
}
