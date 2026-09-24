package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.model.BarahkhadiData
import com.example.ui.components.BarahkhadiTopBar
import com.example.ui.screens.ExplorerScreen
import com.example.ui.screens.FavoritesAndStatsSheet
import com.example.ui.screens.InfoDialog
import com.example.ui.screens.MasterChartScreen
import com.example.ui.screens.PracticeQuizScreen
import com.example.ui.screens.WordMakerScreen
import com.example.ui.screens.WritingPadScreen
import com.example.ui.theme.SaffronPrimary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BarahkhadiViewModel

@Composable
fun MainScreen(
    viewModel: BarahkhadiViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isSlowSpeed by viewModel.isSlowSpeed.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val stats by viewModel.learningStats.collectAsState()
    val quizHistory by viewModel.quizHistory.collectAsState()
    val showInfoDialog by viewModel.showInfoDialog.collectAsState()
    val showFavoritesSheet by viewModel.showFavoritesSheet.collectAsState()

    // Explorer State
    val selectedConsonantIndex by viewModel.selectedConsonantIndex.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedVarga by viewModel.selectedVarga.collectAsState()
    val isPlayingSequence by viewModel.isPlayingSequence.collectAsState()
    val highlightedMatraIndex by viewModel.highlightedMatraIndex.collectAsState()

    // Quiz & Flashcard State
    val quizSubMode by viewModel.quizSubMode.collectAsState()
    val flashcardList by viewModel.flashcardsList.collectAsState()
    val flashcardIndex by viewModel.flashcardIndex.collectAsState()
    val isFlashcardFlipped by viewModel.isFlashcardFlipped.collectAsState()
    val filterFlashcardCurrentOnly by viewModel.filterFlashcardCurrentOnly.collectAsState()

    val quizQuestions by viewModel.quizQuestions.collectAsState()
    val currentQuizIndex by viewModel.currentQuizIndex.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val isAnswerChecked by viewModel.isAnswerChecked.collectAsState()
    val quizScore by viewModel.quizScore.collectAsState()
    val quizStreak by viewModel.quizStreak.collectAsState()
    val isQuizFinished by viewModel.isQuizFinished.collectAsState()

    // Canvas State
    val canvasChar by viewModel.canvasChar.collectAsState()
    val penColor by viewModel.canvasPenColor.collectAsState()
    val strokeWidth by viewModel.canvasStrokeWidth.collectAsState()
    val clearTrigger by viewModel.canvasClearTrigger.collectAsState()

    // Word Maker State
    val wordBuilderSyllables by viewModel.wordBuilderSyllables.collectAsState()
    val wordSearchQuery by viewModel.wordSearchQuery.collectAsState()
    val selectedWordCategory by viewModel.selectedWordCategory.collectAsState()
    val puzzleTargetWord by viewModel.challengeTargetWord.collectAsState()
    val puzzleOptions by viewModel.challengeOptions.collectAsState()
    val puzzleCurrentInput by viewModel.challengeCurrentInput.collectAsState()
    val puzzleScore by viewModel.challengeScore.collectAsState()
    val puzzleStreak by viewModel.challengeStreak.collectAsState()
    val puzzleIsSolved by viewModel.challengeIsSolved.collectAsState()

    Scaffold(
        topBar = {
            BarahkhadiTopBar(
                isSlowSpeed = isSlowSpeed,
                onToggleSpeed = { viewModel.toggleSpeechSpeed() },
                favoriteCount = favorites.size,
                onOpenFavorites = { viewModel.setShowFavoritesSheet(true) },
                onOpenInfo = { viewModel.setShowInfoDialog(true) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.EXPLORER,
                    onClick = { viewModel.setTab(AppTab.EXPLORER) },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Explorer") },
                    label = { Text("Explorer", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_explorer")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.MASTER_CHART,
                    onClick = { viewModel.setTab(AppTab.MASTER_CHART) },
                    icon = { Icon(Icons.Default.GridOn, contentDescription = "Master Chart") },
                    label = { Text("Chart", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_chart")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.WORD_MAKER,
                    onClick = { viewModel.setTab(AppTab.WORD_MAKER) },
                    icon = { Icon(Icons.Default.Spellcheck, contentDescription = "Word Maker") },
                    label = { Text("Words", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_words")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.PRACTICE_QUIZ,
                    onClick = { viewModel.setTab(AppTab.PRACTICE_QUIZ) },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "Quiz") },
                    label = { Text("Practice", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_practice")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.WRITING_PAD,
                    onClick = { viewModel.setTab(AppTab.WRITING_PAD) },
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Writing Pad") },
                    label = { Text("Writing", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_writing")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentTab, label = "tab_transition") { tab ->
                when (tab) {
                    AppTab.EXPLORER -> {
                        ExplorerScreen(
                            selectedConsonantIndex = selectedConsonantIndex,
                            searchQuery = searchQuery,
                            selectedVarga = selectedVarga,
                            isPlayingSequence = isPlayingSequence,
                            highlightedMatraIndex = highlightedMatraIndex,
                            favorites = favorites,
                            onSelectConsonant = { viewModel.selectConsonant(it) },
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onVargaChange = { viewModel.setSelectedVarga(it) },
                            onPlayAllSequence = { viewModel.playAllCurrentSequence() },
                            onStopAudio = { viewModel.stopAudio() },
                            onSpeakSyllable = { text, idx -> viewModel.speak(text, idx) },
                            onToggleFavorite = { comb ->
                                viewModel.toggleFavorite(
                                    comb.hindi,
                                    comb.english,
                                    comb.consonant.char,
                                    comb.matraIndex
                                )
                            }
                        )
                    }

                    AppTab.MASTER_CHART -> {
                        MasterChartScreen(
                            onSpeakSyllable = { viewModel.speak(it) }
                        )
                    }

                    AppTab.WORD_MAKER -> {
                        WordMakerScreen(
                            syllables = wordBuilderSyllables,
                            onAddSyllable = { viewModel.addWordSyllable(it) },
                            onRemoveLastSyllable = { viewModel.removeLastWordSyllable() },
                            onRemoveSyllableAt = { viewModel.removeWordSyllableAt(it) },
                            onClearBuilder = { viewModel.clearWordBuilder() },
                            onLoadPresetWord = { viewModel.loadPresetWord(it) },
                            onSpeakWord = { viewModel.speakFullWord(it) },
                            searchQuery = wordSearchQuery,
                            onSearchQueryChange = { viewModel.setWordSearchQuery(it) },
                            selectedCategory = selectedWordCategory,
                            onCategoryChange = { viewModel.setSelectedWordCategory(it) },
                            puzzleTargetWord = puzzleTargetWord,
                            puzzleOptions = puzzleOptions,
                            puzzleCurrentInput = puzzleCurrentInput,
                            puzzleScore = puzzleScore,
                            puzzleStreak = puzzleStreak,
                            puzzleIsSolved = puzzleIsSolved,
                            onTapPuzzleOption = { viewModel.tapChallengeOption(it) },
                            onResetPuzzleInput = { viewModel.resetChallengeCurrent() },
                            onNextPuzzle = { viewModel.startNewWordChallenge() }
                        )
                    }

                    AppTab.PRACTICE_QUIZ -> {
                        PracticeQuizScreen(
                            subMode = quizSubMode,
                            onSubModeChange = { viewModel.setQuizSubMode(it) },
                            flashcardList = flashcardList,
                            flashcardIndex = flashcardIndex,
                            isFlipped = isFlashcardFlipped,
                            filterCurrentOnly = filterFlashcardCurrentOnly,
                            onToggleCardFlip = { viewModel.toggleCardFlip() },
                            onNextCard = { viewModel.nextFlashcard() },
                            onPrevCard = { viewModel.prevFlashcard() },
                            onShuffleCards = { viewModel.shuffleFlashcards() },
                            onToggleFilterCurrentOnly = { viewModel.toggleFilterFlashcardCurrentOnly() },
                            onSpeakFlashcard = { viewModel.speak(it) },
                            quizQuestions = quizQuestions,
                            currentQuizIndex = currentQuizIndex,
                            selectedAnswer = selectedAnswer,
                            isAnswerChecked = isAnswerChecked,
                            quizScore = quizScore,
                            quizStreak = quizStreak,
                            isQuizFinished = isQuizFinished,
                            onSubmitAnswer = { viewModel.submitAnswer(it) },
                            onRestartQuiz = { viewModel.startSpeedQuiz() },
                            onSpeakQuiz = { viewModel.speak(it) }
                        )
                    }

                    AppTab.WRITING_PAD -> {
                        WritingPadScreen(
                            canvasChar = canvasChar,
                            penColor = penColor,
                            strokeWidth = strokeWidth,
                            clearTrigger = clearTrigger,
                            onSelectChar = { viewModel.setCanvasChar(it) },
                            onChangeColor = { viewModel.setCanvasPenColor(it) },
                            onChangeStrokeWidth = { viewModel.setCanvasStrokeWidth(it) },
                            onClearCanvas = { viewModel.clearCanvas() },
                            onSpeakChar = { viewModel.speakCanvasChar() }
                        )
                    }
                }
            }
        }
    }

    if (showFavoritesSheet) {
        FavoritesAndStatsSheet(
            favorites = favorites,
            stats = stats,
            quizHistory = quizHistory,
            onSpeak = { viewModel.speak(it) },
            onRemoveFavorite = { viewModel.removeFavorite(it) },
            onDismiss = { viewModel.setShowFavoritesSheet(false) }
        )
    }

    if (showInfoDialog) {
        InfoDialog(onDismiss = { viewModel.setShowInfoDialog(false) })
    }
}
