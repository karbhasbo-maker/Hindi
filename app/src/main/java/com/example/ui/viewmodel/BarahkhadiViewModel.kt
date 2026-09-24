package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.TtsManager
import com.example.data.local.BarahkhadiDatabase
import com.example.data.local.entity.FavoriteSyllable
import com.example.data.local.entity.LearningStatsEntity
import com.example.data.local.entity.QuizScoreRecord
import com.example.data.model.BarahkhadiData
import com.example.data.model.Consonant
import com.example.data.model.Matra
import com.example.data.model.SyllableCombination
import com.example.data.repository.BarahkhadiRepository
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

enum class AppTab {
    EXPLORER,
    MASTER_CHART,
    VOWELS_GUIDE,
    PRACTICE_QUIZ,
    WRITING_PAD
}

enum class QuizSubMode {
    FLASHCARDS,
    SPEED_QUIZ
}

enum class QuizQuestionType {
    HINDI_TO_ENGLISH,
    ENGLISH_TO_HINDI,
    AUDIO_LISTEN
}

data class QuizQuestion(
    val title: String,
    val prompt: String,
    val audioText: String,
    val options: List<String>,
    val correctOption: String,
    val explanation: String,
    val type: QuizQuestionType
)

class BarahkhadiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BarahkhadiRepository
    val ttsManager: TtsManager = TtsManager(application)

    init {
        val db = BarahkhadiDatabase.getInstance(application)
        repository = BarahkhadiRepository(db.barahkhadiDao())
    }

    val favorites: StateFlow<List<FavoriteSyllable>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizHistory: StateFlow<List<QuizScoreRecord>> = repository.quizHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val learningStats: StateFlow<LearningStatsEntity?> = repository.learningStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Main Tab State
    private val _currentTab = MutableStateFlow(AppTab.EXPLORER)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Explorer State
    private val _selectedConsonantIndex = MutableStateFlow(0)
    val selectedConsonantIndex: StateFlow<Int> = _selectedConsonantIndex.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedVarga = MutableStateFlow("All")
    val selectedVarga: StateFlow<String> = _selectedVarga.asStateFlow()

    // Audio & Sequencer State
    private val _isSlowSpeed = MutableStateFlow(false)
    val isSlowSpeed: StateFlow<Boolean> = _isSlowSpeed.asStateFlow()

    private val _isPlayingSequence = MutableStateFlow(false)
    val isPlayingSequence: StateFlow<Boolean> = _isPlayingSequence.asStateFlow()

    private val _highlightedMatraIndex = MutableStateFlow<Int?>(null)
    val highlightedMatraIndex: StateFlow<Int?> = _highlightedMatraIndex.asStateFlow()

    private var sequenceJob: Job? = null

    // Quiz & Flashcard State
    private val _quizSubMode = MutableStateFlow(QuizSubMode.FLASHCARDS)
    val quizSubMode: StateFlow<QuizSubMode> = _quizSubMode.asStateFlow()

    private val _flashcardsList = MutableStateFlow<List<SyllableCombination>>(emptyList())
    val flashcardsList: StateFlow<List<SyllableCombination>> = _flashcardsList.asStateFlow()

    private val _flashcardIndex = MutableStateFlow(0)
    val flashcardIndex: StateFlow<Int> = _flashcardIndex.asStateFlow()

    private val _isFlashcardFlipped = MutableStateFlow(false)
    val isFlashcardFlipped: StateFlow<Boolean> = _isFlashcardFlipped.asStateFlow()

    private val _filterFlashcardCurrentOnly = MutableStateFlow(false)
    val filterFlashcardCurrentOnly: StateFlow<Boolean> = _filterFlashcardCurrentOnly.asStateFlow()

    // Speed Quiz Questions State
    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer.asStateFlow()

    private val _isAnswerChecked = MutableStateFlow(false)
    val isAnswerChecked: StateFlow<Boolean> = _isAnswerChecked.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _quizStreak = MutableStateFlow(0)
    val quizStreak: StateFlow<Int> = _quizStreak.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    // Canvas / Tracing Pad State
    private val _canvasChar = MutableStateFlow("क")
    val canvasChar: StateFlow<String> = _canvasChar.asStateFlow()

    private val _canvasPenColor = MutableStateFlow(SaffronPrimary)
    val canvasPenColor: StateFlow<Color> = _canvasPenColor.asStateFlow()

    private val _canvasStrokeWidth = MutableStateFlow(14f)
    val canvasStrokeWidth: StateFlow<Float> = _canvasStrokeWidth.asStateFlow()

    private val _canvasClearTrigger = MutableStateFlow(0L)
    val canvasClearTrigger: StateFlow<Long> = _canvasClearTrigger.asStateFlow()

    // Modals
    private val _showInfoDialog = MutableStateFlow(false)
    val showInfoDialog: StateFlow<Boolean> = _showInfoDialog.asStateFlow()

    private val _showFavoritesSheet = MutableStateFlow(false)
    val showFavoritesSheet: StateFlow<Boolean> = _showFavoritesSheet.asStateFlow()

    init {
        initFlashcards()
        startSpeedQuiz()
    }

    fun setTab(tab: AppTab) {
        stopAudio()
        _currentTab.value = tab
    }

    fun selectConsonant(index: Int) {
        stopAudio()
        _selectedConsonantIndex.value = index
        _canvasChar.value = BarahkhadiData.CONSONANTS[index].char
        if (_filterFlashcardCurrentOnly.value) {
            initFlashcards()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedVarga(varga: String) {
        _selectedVarga.value = varga
    }

    fun toggleSpeechSpeed() {
        val newSlow = !_isSlowSpeed.value
        _isSlowSpeed.value = newSlow
        ttsManager.setSpeechRate(if (newSlow) 0.75f else 1.0f)
    }

    fun speak(text: String, highlightIndex: Int? = null) {
        _highlightedMatraIndex.value = highlightIndex
        viewModelScope.launch {
            repository.incrementAudioCount()
        }
        ttsManager.speak(text) {
            if (_highlightedMatraIndex.value == highlightIndex) {
                _highlightedMatraIndex.value = null
            }
        }
    }

    fun stopAudio() {
        sequenceJob?.cancel()
        sequenceJob = null
        _isPlayingSequence.value = false
        _highlightedMatraIndex.value = null
        ttsManager.stop()
    }

    fun playAllCurrentSequence() {
        if (_isPlayingSequence.value) {
            stopAudio()
            return
        }

        val consonant = BarahkhadiData.CONSONANTS[_selectedConsonantIndex.value]
        _isPlayingSequence.value = true

        sequenceJob = viewModelScope.launch {
            for (i in BarahkhadiData.MATRAS.indices) {
                if (!_isPlayingSequence.value) break
                val matra = BarahkhadiData.MATRAS[i]
                val comb = BarahkhadiData.getCombination(consonant, matra, i)
                _highlightedMatraIndex.value = i

                suspendCancellableCoroutine { continuation ->
                    ttsManager.speak(comb.hindi) {
                        if (continuation.isActive) {
                            continuation.resume(Unit)
                        }
                    }
                }
                delay(if (_isSlowSpeed.value) 500 else 350)
            }
            _highlightedMatraIndex.value = null
            _isPlayingSequence.value = false
        }
    }

    // Flashcards Logic
    fun initFlashcards() {
        val source = if (_filterFlashcardCurrentOnly.value) {
            val c = BarahkhadiData.CONSONANTS[_selectedConsonantIndex.value]
            BarahkhadiData.MATRAS.mapIndexed { idx, m ->
                BarahkhadiData.getCombination(c, m, idx)
            }
        } else {
            BarahkhadiData.getAllCombinations()
        }
        _flashcardsList.value = source.shuffled()
        _flashcardIndex.value = 0
        _isFlashcardFlipped.value = false
    }

    fun toggleFilterFlashcardCurrentOnly() {
        _filterFlashcardCurrentOnly.value = !_filterFlashcardCurrentOnly.value
        initFlashcards()
    }

    fun toggleCardFlip() {
        _isFlashcardFlipped.value = !_isFlashcardFlipped.value
    }

    fun nextFlashcard() {
        if (_flashcardIndex.value < _flashcardsList.value.size - 1) {
            _flashcardIndex.value += 1
            _isFlashcardFlipped.value = false
            viewModelScope.launch { repository.incrementFlashcardsCount() }
        }
    }

    fun prevFlashcard() {
        if (_flashcardIndex.value > 0) {
            _flashcardIndex.value -= 1
            _isFlashcardFlipped.value = false
        }
    }

    fun shuffleFlashcards() {
        _flashcardsList.value = _flashcardsList.value.shuffled()
        _flashcardIndex.value = 0
        _isFlashcardFlipped.value = false
    }

    fun setQuizSubMode(mode: QuizSubMode) {
        _quizSubMode.value = mode
        if (mode == QuizSubMode.SPEED_QUIZ && _quizQuestions.value.isEmpty()) {
            startSpeedQuiz()
        }
    }

    // Speed Quiz Logic
    fun startSpeedQuiz() {
        _quizScore.value = 0
        _quizStreak.value = 0
        _currentQuizIndex.value = 0
        _selectedAnswer.value = null
        _isAnswerChecked.value = false
        _isQuizFinished.value = false

        val all = BarahkhadiData.getAllCombinations().shuffled()
        val questions = mutableListOf<QuizQuestion>()

        for (i in 0 until 10) {
            val target = all[i]
            val type = when (i % 3) {
                0 -> QuizQuestionType.HINDI_TO_ENGLISH
                1 -> QuizQuestionType.ENGLISH_TO_HINDI
                else -> QuizQuestionType.AUDIO_LISTEN
            }

            // Distractor options
            val distractors = mutableListOf<String>()
            val distractorPool = all.filter { it.hindi != target.hindi && it.english != target.english }.shuffled()

            when (type) {
                QuizQuestionType.HINDI_TO_ENGLISH -> {
                    distractors.addAll(distractorPool.take(3).map { it.english })
                    val options = (distractors + target.english).shuffled()
                    questions.add(
                        QuizQuestion(
                            title = "Identify the English transliteration",
                            prompt = target.hindi,
                            audioText = target.hindi,
                            options = options,
                            correctOption = target.english,
                            explanation = "${target.hindi} sounds like \"${target.english}\" (${target.consonant.char} + ${target.matra.vowel})",
                            type = type
                        )
                    )
                }
                QuizQuestionType.ENGLISH_TO_HINDI -> {
                    distractors.addAll(distractorPool.take(3).map { it.hindi })
                    val options = (distractors + target.hindi).shuffled()
                    questions.add(
                        QuizQuestion(
                            title = "Which Devanagari syllable is this?",
                            prompt = target.english,
                            audioText = target.hindi,
                            options = options,
                            correctOption = target.hindi,
                            explanation = "\"${target.english}\" is written as ${target.hindi} in Devanagari",
                            type = type
                        )
                    )
                }
                QuizQuestionType.AUDIO_LISTEN -> {
                    distractors.addAll(distractorPool.take(3).map { "${it.hindi} (${it.english})" })
                    val correct = "${target.hindi} (${target.english})"
                    val options = (distractors + correct).shuffled()
                    questions.add(
                        QuizQuestion(
                            title = "Listen to the sound and choose:",
                            prompt = "🔊 Tap speaker to listen",
                            audioText = target.hindi,
                            options = options,
                            correctOption = correct,
                            explanation = "The sound was ${target.hindi} (${target.english})",
                            type = type
                        )
                    )
                }
            }
        }
        _quizQuestions.value = questions
    }

    fun submitAnswer(option: String) {
        if (_isAnswerChecked.value || _isQuizFinished.value) return
        val currentQ = _quizQuestions.value.getOrNull(_currentQuizIndex.value) ?: return

        _selectedAnswer.value = option
        _isAnswerChecked.value = true

        val isCorrect = option == currentQ.correctOption
        if (isCorrect) {
            _quizScore.value += 10
            _quizStreak.value += 1
            ttsManager.speak("सही! " + currentQ.audioText)
        } else {
            _quizStreak.value = 0
            ttsManager.speak(currentQ.audioText)
        }

        viewModelScope.launch {
            delay(1600)
            if (_currentQuizIndex.value < _quizQuestions.value.size - 1) {
                _currentQuizIndex.value += 1
                _selectedAnswer.value = null
                _isAnswerChecked.value = false
            } else {
                _isQuizFinished.value = true
                repository.recordQuizScore(_quizScore.value, 100)
            }
        }
    }

    // Canvas / Tracing controls
    fun setCanvasChar(char: String) {
        _canvasChar.value = char
        _canvasClearTrigger.value = System.currentTimeMillis()
    }

    fun setCanvasPenColor(color: Color) {
        _canvasPenColor.value = color
    }

    fun setCanvasStrokeWidth(width: Float) {
        _canvasStrokeWidth.value = width
    }

    fun clearCanvas() {
        _canvasClearTrigger.value = System.currentTimeMillis()
        viewModelScope.launch {
            repository.incrementCanvasDrawingCount()
        }
    }

    fun speakCanvasChar() {
        speak(_canvasChar.value)
    }

    // Favorites
    fun toggleFavorite(
        hindi: String,
        english: String,
        consonantChar: String,
        matraIndex: Int
    ) {
        viewModelScope.launch {
            repository.toggleFavorite(hindi, english, consonantChar, matraIndex)
        }
    }

    fun removeFavorite(hindi: String) {
        viewModelScope.launch {
            repository.removeFavorite(hindi)
        }
    }

    fun setShowInfoDialog(show: Boolean) {
        _showInfoDialog.value = show
    }

    fun setShowFavoritesSheet(show: Boolean) {
        _showFavoritesSheet.value = show
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
