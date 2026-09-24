package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BarahkhadiData
import com.example.data.model.Matra
import com.example.ui.theme.AmberGold
import com.example.ui.theme.SaffronPrimary

@Composable
fun VowelsGuideScreen(
    onSpeakVowel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Explanatory Banner Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SaffronPrimary.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AmberGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Rule",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "How Barahkhadi Works (बारहखड़ी का नियम)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Barahkhadi (हिंदी बारहखड़ी) literally translates to \"12 variations\". In the Devanagari script, every consonant inherits the short /a/ vowel sound. By applying 12 different vowel marks (मात्राएँ), the consonant transforms into 12 phonetic syllables. Master these 12 patterns to read, spell, and pronounce any Hindi word with confidence!",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Text(
                text = "12 VOWEL SOUNDS & MATRA SYMBOLS (स्वर एवं मात्राएँ)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
        }

        // 12 Matra Guide Cards
        itemsIndexed(BarahkhadiData.MATRAS) { index, matra ->
            MatraGuideCard(
                matra = matra,
                index = index,
                onSpeak = { onSpeakVowel(matra.vowel) }
            )
        }
    }
}

@Composable
fun MatraGuideCard(
    matra: Matra,
    index: Int,
    onSpeak: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSpeak() }
            .testTag("matra_card_$index")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Swar label & Speaker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SaffronPrimary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "स्वर ${index + 1}: ${matra.swarName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Listen ${matra.vowel}",
                        tint = SaffronPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Glyphs Row: Independent Vowel + Matra Symbol -> Ka Example
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Independent Vowel
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = matra.vowel,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Swar (स्वर)",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "+",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )

                // Matra Symbol
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = matra.symbol.ifEmpty { "∅ (none)" },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = SaffronPrimary
                    )
                    Text(
                        text = "Matra (मात्रा)",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "=",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )

                // Example Combination
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val exHindi = if (matra.symbol.isEmpty()) "क" else "क" + matra.symbol
                    Text(
                        text = exHindi,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = AmberGold
                    )
                    Text(
                        text = matra.exampleKa,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Transliteration & Phonetics
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "English Sound: ${matra.english}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
                Text(
                    text = "🔊 ${matra.phonetic}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = matra.explanation,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
