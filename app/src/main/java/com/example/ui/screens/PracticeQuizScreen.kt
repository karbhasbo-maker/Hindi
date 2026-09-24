package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SyllableCombination
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.ErrorRoseBg
import com.example.ui.theme.IndicDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBg
import com.example.ui.viewmodel.QuizQuestion
import com.example.ui.viewmodel.QuizSubMode

@Composable
fun PracticeQuizScreen(
    subMode: QuizSubMode,
    onSubModeChange: (QuizSubMode) -> Unit,
    // Flashcard args
    flashcardList: List<SyllableCombination>,
    flashcardIndex: Int,
    isFlipped: Boolean,
    filterCurrentOnly: Boolean,
    onToggleCardFlip: () -> Unit,
    onNextCard: () -> Unit,
    onPrevCard: () -> Unit,
    onShuffleCards: () -> Unit,
    onToggleFilterCurrentOnly: () -> Unit,
    onSpeakFlashcard: (String) -> Unit,
    // Quiz args
    quizQuestions: List<QuizQuestion>,
    currentQuizIndex: Int,
    selectedAnswer: String?,
    isAnswerChecked: Boolean,
    quizScore: Int,
    quizStreak: Int,
    isQuizFinished: Boolean,
    onSubmitAnswer: (String) -> Unit,
    onRestartQuiz: () -> Unit,
    onSpeakQuiz: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode Selector: Flashcards vs Speed Quiz
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val isFlashcard = subMode == QuizSubMode.FLASHCARDS
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isFlashcard) SaffronPrimary else Color.Transparent,
                    modifier = Modifier.clickable { onSubModeChange(QuizSubMode.FLASHCARDS) }
                ) {
                    Text(
                        text = "🎴 Flashcards",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFlashcard) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (!isFlashcard) SaffronPrimary else Color.Transparent,
                    modifier = Modifier.clickable { onSubModeChange(QuizSubMode.SPEED_QUIZ) }
                ) {
                    Text(
                        text = "⚡ Speed Quiz",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isFlashcard) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
        }

        if (subMode == QuizSubMode.FLASHCARDS) {
            FlashcardSubView(
                flashcardList = flashcardList,
                flashcardIndex = flashcardIndex,
                isFlipped = isFlipped,
                filterCurrentOnly = filterCurrentOnly,
                onToggleCardFlip = onToggleCardFlip,
                onNextCard = onNextCard,
                onPrevCard = onPrevCard,
                onShuffleCards = onShuffleCards,
                onToggleFilterCurrentOnly = onToggleFilterCurrentOnly,
                onSpeak = onSpeakFlashcard
            )
        } else {
            SpeedQuizSubView(
                quizQuestions = quizQuestions,
                currentQuizIndex = currentQuizIndex,
                selectedAnswer = selectedAnswer,
                isAnswerChecked = isAnswerChecked,
                quizScore = quizScore,
                quizStreak = quizStreak,
                isQuizFinished = isQuizFinished,
                onSubmitAnswer = onSubmitAnswer,
                onRestartQuiz = onRestartQuiz,
                onSpeak = onSpeakQuiz
            )
        }
    }
}

@Composable
fun FlashcardSubView(
    flashcardList: List<SyllableCombination>,
    flashcardIndex: Int,
    isFlipped: Boolean,
    filterCurrentOnly: Boolean,
    onToggleCardFlip: () -> Unit,
    onNextCard: () -> Unit,
    onPrevCard: () -> Unit,
    onShuffleCards: () -> Unit,
    onToggleFilterCurrentOnly: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val currentCard = flashcardList.getOrNull(flashcardIndex)

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "cardFlip"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Controls: Shuffle & Filter toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = filterCurrentOnly,
                onClick = onToggleFilterCurrentOnly,
                label = {
                    Text(
                        text = if (filterCurrentOnly) "Selected Consonant (12)" else "All Syllables (432)",
                        fontSize = 11.sp
                    )
                }
            )

            IconButton(onClick = onShuffleCards) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle Cards",
                    tint = SaffronPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3D Flip Flashcard Container
        if (currentCard != null) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFlipped) IndicDark else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.5.dp,
                    if (isFlipped) AmberGold else SaffronPrimary.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { onToggleCardFlip() }
                    .testTag("flashcard_container")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // FRONT SIDE: Hindi Syllable
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SaffronPrimary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "Devanagari Syllable",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentCard.hindi,
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Black,
                                color = SaffronPrimary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            IconButton(
                                onClick = { onSpeak(currentCard.hindi) },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Pronounce",
                                    tint = SaffronPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Tap card to flip for English sound",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        // BACK SIDE: English Transliteration (mirrored to read correctly)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.graphicsLayer { rotationY = 180f }
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AmberGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "English Transliteration",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentCard.english,
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Black,
                                color = AmberGold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${currentCard.consonant.char} (${currentCard.consonant.english}) + ${currentCard.matra.vowel} (${currentCard.matra.english})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.9f)
                            )

                            Text(
                                text = currentCard.matra.phonetic,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Tap to flip back",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPrevCard,
                    enabled = flashcardIndex > 0,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Previous")
                }

                Text(
                    text = "${flashcardIndex + 1} / ${flashcardList.size}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = onNextCard,
                    enabled = flashcardIndex < flashcardList.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SpeedQuizSubView(
    quizQuestions: List<QuizQuestion>,
    currentQuizIndex: Int,
    selectedAnswer: String?,
    isAnswerChecked: Boolean,
    quizScore: Int,
    quizStreak: Int,
    isQuizFinished: Boolean,
    onSubmitAnswer: (String) -> Unit,
    onRestartQuiz: () -> Unit,
    onSpeak: (String) -> Unit
) {
    if (isQuizFinished) {
        // Quiz Finished Celebration View
        ElevatedCard(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(AmberGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Quiz Completed!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your Score: $quizScore / 100",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = SaffronPrimary
                )

                Text(
                    text = if (quizScore >= 80) "🌟 शाबाश! Outstanding Hindi mastery!" else "👍 Keep practicing, you're doing great!",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onRestartQuiz,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Restart",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Play Again (New Questions)", fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        val currentQ = quizQuestions.getOrNull(currentQuizIndex)
        if (currentQ != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Score & Progress Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = AmberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Streak: $quizStreak",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SaffronPrimary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "Score: $quizScore",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = SaffronPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = "Question ${currentQuizIndex + 1} / ${quizQuestions.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Question Prompt Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentQ.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentQ.prompt,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Black,
                            color = SaffronPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        IconButton(
                            onClick = { onSpeak(currentQ.audioText) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SaffronPrimary.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen Sound",
                                tint = SaffronPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4 Multiple Choice Options (2x2 Grid)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (row in 0 until 2) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (col in 0 until 2) {
                                val optIndex = row * 2 + col
                                val option = currentQ.options.getOrNull(optIndex)
                                if (option != null) {
                                    val isSelected = selectedAnswer == option
                                    val isCorrect = option == currentQ.correctOption

                                    val btnBg = when {
                                        isAnswerChecked && isCorrect -> SuccessGreen
                                        isAnswerChecked && isSelected && !isCorrect -> ErrorRose
                                        else -> MaterialTheme.colorScheme.surface
                                    }

                                    val textColor = when {
                                        isAnswerChecked && (isCorrect || isSelected) -> Color.White
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }

                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = btnBg),
                                        border = BorderStroke(
                                            1.5.dp,
                                            if (isAnswerChecked && isCorrect) SuccessGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(64.dp)
                                            .clickable(enabled = !isAnswerChecked) {
                                                onSubmitAnswer(option)
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = option,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Answer explanation banner when checked
                AnimatedVisibility(
                    visible = isAnswerChecked,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val isCorrect = selectedAnswer == currentQ.correctOption
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCorrect) SuccessGreenBg else ErrorRoseBg,
                        border = BorderStroke(1.dp, if (isCorrect) SuccessGreen else ErrorRose),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Result",
                                tint = if (isCorrect) SuccessGreen else ErrorRose,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isCorrect) "Correct! ${currentQ.explanation}" else "Incorrect. ${currentQ.explanation}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) SuccessGreen else ErrorRose
                            )
                        }
                    }
                }
            }
        }
    }
}
