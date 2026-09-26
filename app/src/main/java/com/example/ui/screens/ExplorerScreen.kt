package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.FavoriteSyllable
import com.example.data.model.BarahkhadiData
import com.example.data.model.Consonant
import com.example.data.model.SyllableCombination
import com.example.ui.theme.AmberGold
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SaffronSecondary

@Composable
fun ExplorerScreen(
    selectedConsonantIndex: Int,
    searchQuery: String,
    selectedVarga: String,
    isPlayingSequence: Boolean,
    highlightedMatraIndex: Int?,
    favorites: List<FavoriteSyllable>,
    onSelectConsonant: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    onVargaChange: (String) -> Unit,
    onPlayAllSequence: () -> Unit,
    onStopAudio: () -> Unit,
    onSpeakSyllable: (String, Int) -> Unit,
    onToggleFavorite: (SyllableCombination) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentConsonant = BarahkhadiData.CONSONANTS[selectedConsonantIndex]

    val filteredConsonants = remember(searchQuery, selectedVarga) {
        val query = searchQuery.trim().lowercase()
        BarahkhadiData.CONSONANTS.mapIndexed { index, consonant ->
            Pair(index, consonant)
        }.filter { (_, consonant) ->
            val vargaMatch = selectedVarga == "All" || consonant.varga == selectedVarga
            val queryMatch = query.isEmpty() ||
                    consonant.char.contains(query) ||
                    consonant.english.lowercase().contains(query)
            vargaMatch && queryMatch
        }
    }

    val currentCombinations = remember(selectedConsonantIndex) {
        BarahkhadiData.MATRAS.mapIndexed { index, matra ->
            BarahkhadiData.getCombination(currentConsonant, matra, index)
        }
    }

    val gridState = rememberLazyGridState()
    var isSearchExpanded by remember { mutableStateOf(searchQuery.isNotEmpty() || selectedVarga != "All") }

    // Scroll back to top whenever a new consonant is selected so Ka, Kaa, Ki are always immediately visible
    LaunchedEffect(selectedConsonantIndex) {
        gridState.scrollToItem(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        // Compact Unified Header Card (Consonant Selector + Search/Filter + Active Hero Bar)
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                // Row 1: Horizontal Consonant Strip + Search Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("consonants_row")
                    ) {
                        items(filteredConsonants) { (origIndex, consonant) ->
                            val isSelected = origIndex == selectedConsonantIndex
                            Box(
                                modifier = Modifier
                                    .size(width = 46.dp, height = 48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) {
                                            Brush.verticalGradient(
                                                listOf(SaffronPrimary, AmberGold)
                                            )
                                        } else {
                                            Brush.verticalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.surfaceVariant,
                                                    MaterialTheme.colorScheme.surfaceVariant
                                                )
                                            )
                                        }
                                    )
                                    .clickable { onSelectConsonant(origIndex) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = consonant.char,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = consonant.english,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Search / Filter Toggle Icon Button
                    IconButton(
                        onClick = { isSearchExpanded = !isSearchExpanded },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSearchExpanded || searchQuery.isNotEmpty() || selectedVarga != "All") {
                                    SaffronPrimary.copy(alpha = 0.15f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                    ) {
                        Icon(
                            imageVector = if (isSearchExpanded) Icons.Default.Clear else Icons.Default.Search,
                            contentDescription = "Search and Filter",
                            tint = if (isSearchExpanded || searchQuery.isNotEmpty() || selectedVarga != "All") {
                                SaffronPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Collapsible Search & Filter Area
                AnimatedVisibility(
                    visible = isSearchExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("consonant_search_input"),
                            placeholder = {
                                Text(
                                    text = "Search consonant (e.g. Ka, Kha, ग)...",
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchChange("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(BarahkhadiData.VARGA_CATEGORIES) { varga ->
                                val isSelected = varga == selectedVarga
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onVargaChange(varga) },
                                    label = {
                                        Text(
                                            text = varga,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SaffronPrimary,
                                        selectedLabelColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Row 2: Selected Consonant Info & Play All Controls (Compact Hero Bar)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(SaffronPrimary, AmberGold)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentConsonant.char,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${currentConsonant.char} की बारहखड़ी",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SaffronPrimary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${currentConsonant.english}a",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${currentConsonant.varga} • 12 Variations",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Play All Sequence Controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = onPlayAllSequence,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPlayingSequence) AmberGold else SaffronPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("play_all_sequence_button")
                        ) {
                            Icon(
                                imageVector = if (isPlayingSequence) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlayingSequence) "Pause" else "Play All",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isPlayingSequence) "Pause" else "Play All 12",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isPlayingSequence) {
                            IconButton(
                                onClick = onStopAudio,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "Stop",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 12 Barahkhadi Cards Grid - Starting cleanly at index 0 (Ka, Kaa, Ki...)
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .testTag("barahkhadi_cards_grid")
        ) {
            itemsIndexed(currentCombinations) { index, comb ->
                val isHighlighted = highlightedMatraIndex == index
                val isFavorited = favorites.any { it.hindi == comb.hindi }

                BarahkhadiCardItem(
                    combination = comb,
                    index = index,
                    isHighlighted = isHighlighted,
                    isFavorited = isFavorited,
                    onSpeak = { onSpeakSyllable(comb.hindi, index) },
                    onToggleFavorite = { onToggleFavorite(comb) }
                )
            }
        }
    }
}

@Composable
fun BarahkhadiCardItem(
    combination: SyllableCombination,
    index: Int,
    isHighlighted: Boolean,
    isFavorited: Boolean,
    onSpeak: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isHighlighted) SaffronPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        label = "border"
    )

    val containerColor = if (isHighlighted) {
        SaffronPrimary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isHighlighted) 2.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 4.dp else 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .scale(if (isHighlighted) pulseScale else 1.0f)
            .clickable { onSpeak() }
            .testTag("card_item_$index")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Swar symbol & Favorite star
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${combination.matra.vowel} (${combination.matra.symbol.ifEmpty { "∅" }})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorited) AmberGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Syllable Character (Devanagari)
            Text(
                text = combination.hindi,
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = if (isHighlighted) SaffronPrimary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            // English transliteration
            Text(
                text = combination.english,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary,
                textAlign = TextAlign.Center
            )

            // Phonetic helper
            Text(
                text = combination.matra.phonetic,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Speaker Icon button
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (isHighlighted) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Listen",
                    tint = if (isHighlighted) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
