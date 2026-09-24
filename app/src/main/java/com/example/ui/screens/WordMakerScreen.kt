package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BarahkhadiData
import com.example.data.model.Consonant
import com.example.data.model.HindiWord
import com.example.data.model.HindiWordData
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.IndicDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBg

enum class WordMakerMode {
    BUILDER,
    VOCABULARY,
    PUZZLE
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordMakerScreen(
    syllables: List<String>,
    onAddSyllable: (String) -> Unit,
    onRemoveLastSyllable: () -> Unit,
    onRemoveSyllableAt: (Int) -> Unit,
    onClearBuilder: () -> Unit,
    onLoadPresetWord: (HindiWord) -> Unit,
    onSpeakWord: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    // Puzzle Props
    puzzleTargetWord: HindiWord?,
    puzzleOptions: List<String>,
    puzzleCurrentInput: List<String>,
    puzzleScore: Int,
    puzzleStreak: Int,
    puzzleIsSolved: Boolean,
    onTapPuzzleOption: (String) -> Unit,
    onResetPuzzleInput: () -> Unit,
    onNextPuzzle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubMode by remember { mutableStateOf(WordMakerMode.BUILDER) }
    var selectedConsonantForMatras by remember { mutableStateOf(BarahkhadiData.CONSONANTS[0]) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val constructedHindi = syllables.joinToString("")
    val matchedWord = HindiWordData.findWord(constructedHindi)
    val approxEnglish = HindiWordData.approximateTransliteration(syllables)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Submode Selector Tabs
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    WordMakerMode.BUILDER to "🔨 Word Builder",
                    WordMakerMode.VOCABULARY to "📚 Library",
                    WordMakerMode.PUZZLE to "🧩 Word Puzzle"
                ).forEach { (mode, title) ->
                    val isSelected = currentSubMode == mode
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SaffronPrimary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentSubMode = mode }
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        when (currentSubMode) {
            WordMakerMode.BUILDER -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Constructed Word Display Card
                    item {
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.5.dp, SaffronPrimary.copy(alpha = 0.5f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("word_builder_canvas")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SaffronPrimary.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "CONSTRUCTED HINDI WORD",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SaffronPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Row {
                                        if (constructedHindi.isNotEmpty()) {
                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(constructedHindi))
                                                    Toast.makeText(context, "Copied: $constructedHindi", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(34.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy Word",
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = onRemoveLastSyllable,
                                            enabled = syllables.isNotEmpty(),
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                                contentDescription = "Backspace",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = onClearBuilder,
                                            enabled = syllables.isNotEmpty(),
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Main Hindi Word Typography
                                if (constructedHindi.isEmpty()) {
                                    Text(
                                        text = "Tap syllables below to make words",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.padding(vertical = 20.dp)
                                    )
                                } else {
                                    Text(
                                        text = constructedHindi,
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Black,
                                        color = SaffronPrimary,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // English Pronunciation & Meaning
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = matchedWord?.english ?: approxEnglish,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        if (matchedWord != null) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = AmberGold.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = "${matchedWord.emoji} ${matchedWord.meaning}",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AmberGold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Listen Pronunciation Button
                                    Button(
                                        onClick = { onSpeakWord(constructedHindi) },
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Pronounce Word",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Pronounce Word", fontWeight = FontWeight.Bold)
                                    }
                                }

                                // Interactive Syllable Chips
                                if (syllables.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        syllables.forEachIndexed { index, syl ->
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f)),
                                                modifier = Modifier.clickable { onRemoveSyllableAt(index) }
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = syl,
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = SaffronPrimary
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Icon(
                                                        imageVector = Icons.Default.Clear,
                                                        contentDescription = "Remove Syllable",
                                                        tint = MaterialTheme.colorScheme.outline,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Syllable Picker Section (Barahkhadi Keyboard)
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "1. CHOOSE CONSONANT (व्यंजन):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Consonants Horizontal Ribbon
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(BarahkhadiData.CONSONANTS) { consonant ->
                                        val isSelected = selectedConsonantForMatras.char == consonant.char
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable {
                                                selectedConsonantForMatras = consonant
                                            }
                                        ) {
                                            Text(
                                                text = "${consonant.char} (${consonant.english})",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "2. TAP 12 BARAHKHADI COMBINATIONS FOR '${selectedConsonantForMatras.char}':",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // 12 Barahkhadi Matra Buttons (3 rows x 4 cols)
                                val combinations = BarahkhadiData.getCombinationsForConsonant(selectedConsonantForMatras)
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    for (r in 0 until 3) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            for (c in 0 until 4) {
                                                val comb = combinations.getOrNull(r * 4 + c)
                                                if (comb != null) {
                                                    Surface(
                                                        shape = RoundedCornerShape(10.dp),
                                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                                        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.2f)),
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .clickable {
                                                                onAddSyllable(comb.hindi)
                                                            }
                                                    ) {
                                                        Column(
                                                            modifier = Modifier.padding(vertical = 8.dp),
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            Text(
                                                                text = comb.hindi,
                                                                fontSize = 20.sp,
                                                                fontWeight = FontWeight.Black,
                                                                color = SaffronPrimary
                                                            )
                                                            Text(
                                                                text = comb.english,
                                                                fontSize = 10.sp,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Quick Modifiers & Independent Vowels
                                Text(
                                    text = "INDEPENDENT VOWELS & MODIFIERS:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val specials = listOf("अ", "आ", "इ", "ई", "उ", "ऊ", "ए", "ऐ", "ओ", "औ", "अं", "्", "ँ")
                                    items(specials) { char ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                            modifier = Modifier.clickable { onAddSyllable(char) }
                                        ) {
                                            Text(
                                                text = char,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick Suggested Word Inspirations
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "✨ QUICK WORD INSPIRATIONS",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronPrimary
                                    )
                                    Text(
                                        text = "Tap to load",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    HindiWordData.PRESET_WORDS.take(12).forEach { word ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable { onLoadPresetWord(word) }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = word.emoji, fontSize = 12.sp)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${word.hindi} (${word.english})",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            WordMakerMode.VOCABULARY -> {
                // Vocabulary Library Explorer
                val filteredWords = remember(searchQuery, selectedCategory) {
                    HindiWordData.PRESET_WORDS.filter { word ->
                        val matchesCat = selectedCategory == "All Words" || word.category == selectedCategory
                        val matchesSearch = searchQuery.isBlank() ||
                                word.hindi.contains(searchQuery, ignoreCase = true) ||
                                word.english.contains(searchQuery, ignoreCase = true) ||
                                word.meaning.contains(searchQuery, ignoreCase = true)
                        matchesCat && matchesSearch
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search words (e.g. Kamal, किताब, Water...)") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )

                    // Category Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        items(HindiWordData.CATEGORIES) { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { onCategoryChange(cat) },
                                label = { Text(cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaffronPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    // Word Cards List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredWords) { word ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onLoadPresetWord(word)
                                        currentSubMode = WordMakerMode.BUILDER
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(SaffronPrimary.copy(alpha = 0.12f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = word.emoji, fontSize = 24.sp)
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = word.hindi,
                                                    fontSize = 22.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = SaffronPrimary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = word.english,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }

                                            Text(
                                                text = "Meaning: ${word.meaning}",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )

                                            // Syllable pills
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                word.syllables.forEach { syl ->
                                                    Surface(
                                                        shape = RoundedCornerShape(4.dp),
                                                        color = MaterialTheme.colorScheme.surfaceVariant
                                                    ) {
                                                        Text(
                                                            text = syl,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = SaffronPrimary,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    IconButton(
                                        onClick = { onSpeakWord(word.hindi) },
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(SaffronPrimary.copy(alpha = 0.15f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Speak Word",
                                            tint = SaffronPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            WordMakerMode.PUZZLE -> {
                // Interactive Word Maker Challenge Puzzle
                if (puzzleTargetWord != null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Score Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = AmberGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Streak: $puzzleStreak",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SaffronPrimary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "Score: $puzzleScore",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SaffronPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Target Word Card Prompt
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.5.dp, SaffronPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = puzzleTargetWord.emoji,
                                    fontSize = 48.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Assemble Hindi Word for:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    text = "\"${puzzleTargetWord.meaning}\" (${puzzleTargetWord.english})",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SaffronPrimary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Assembled Slots Display
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            for (i in 0 until puzzleTargetWord.syllables.size) {
                                val entered = puzzleCurrentInput.getOrNull(i)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (entered != null) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(
                                        1.5.dp,
                                        if (entered != null) SaffronPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                    ),
                                    modifier = Modifier.size(54.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = entered ?: "_",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (entered != null) Color.White else MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }

                        // Syllable Choice Tiles
                        Text(
                            text = "TAP SYLLABLES IN CORRECT ORDER:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            puzzleOptions.forEach { opt ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(1.5.dp, SaffronPrimary.copy(alpha = 0.5f)),
                                    tonalElevation = 2.dp,
                                    shadowElevation = 2.dp,
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clickable(enabled = !puzzleIsSolved) {
                                            onTapPuzzleOption(opt)
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = opt,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Black,
                                            color = SaffronPrimary
                                        )
                                    }
                                }
                            }
                        }

                        // Success / Retry Banner
                        if (puzzleIsSolved) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SuccessGreenBg,
                                border = BorderStroke(1.dp, SuccessGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Success",
                                            tint = SuccessGreen
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "शाबाश! Correct!",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SuccessGreen
                                            )
                                            Text(
                                                text = "${puzzleTargetWord.hindi} = ${puzzleTargetWord.english}",
                                                fontSize = 12.sp,
                                                color = SuccessGreen
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = onNextPuzzle,
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Next Word", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        } else if (puzzleCurrentInput.size == puzzleTargetWord.syllables.size) {
                            // Incorrect attempt
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Not quite! Try again.",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    OutlinedButton(onClick = onResetPuzzleInput) {
                                        Text("Reset")
                                    }
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = onResetPuzzleInput,
                                shape = RoundedCornerShape(10.dp),
                                enabled = puzzleCurrentInput.isNotEmpty()
                            ) {
                                Text("Reset Input")
                            }
                        }
                    }
                }
            }
        }
    }
}
