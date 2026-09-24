package com.example.data.repository

import com.example.data.local.dao.BarahkhadiDao
import com.example.data.local.entity.FavoriteSyllable
import com.example.data.local.entity.LearningStatsEntity
import com.example.data.local.entity.QuizScoreRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class BarahkhadiRepository(private val dao: BarahkhadiDao) {

    val allFavorites: Flow<List<FavoriteSyllable>> = dao.getAllFavorites()
    val quizHistory: Flow<List<QuizScoreRecord>> = dao.getQuizHistory()
    val learningStats: Flow<LearningStatsEntity?> = dao.getLearningStats()

    fun isFavoriteFlow(hindi: String): Flow<Boolean> = dao.isFavoriteFlow(hindi)

    suspend fun toggleFavorite(
        hindi: String,
        english: String,
        consonantChar: String,
        matraIndex: Int
    ): Boolean {
        val exists = dao.isFavorite(hindi)
        if (exists) {
            dao.deleteFavoriteByHindi(hindi)
            return false
        } else {
            dao.insertFavorite(
                FavoriteSyllable(
                    hindi = hindi,
                    english = english,
                    consonantChar = consonantChar,
                    matraIndex = matraIndex
                )
            )
            return true
        }
    }

    suspend fun removeFavorite(hindi: String) {
        dao.deleteFavoriteByHindi(hindi)
    }

    suspend fun recordQuizScore(score: Int, totalQuestions: Int) {
        dao.insertQuizScore(QuizScoreRecord(score = score, totalQuestions = totalQuestions))
        val currentStats = dao.getLearningStats().firstOrNull() ?: LearningStatsEntity()
        dao.updateStats(
            currentStats.copy(
                quizzesCompleted = currentStats.quizzesCompleted + 1
            )
        )
    }

    suspend fun incrementAudioCount() {
        val currentStats = dao.getLearningStats().firstOrNull() ?: LearningStatsEntity()
        dao.updateStats(
            currentStats.copy(
                totalAudioPlayed = currentStats.totalAudioPlayed + 1
            )
        )
    }

    suspend fun incrementFlashcardsCount() {
        val currentStats = dao.getLearningStats().firstOrNull() ?: LearningStatsEntity()
        dao.updateStats(
            currentStats.copy(
                flashcardsReviewed = currentStats.flashcardsReviewed + 1
            )
        )
    }

    suspend fun incrementCanvasDrawingCount() {
        val currentStats = dao.getLearningStats().firstOrNull() ?: LearningStatsEntity()
        dao.updateStats(
            currentStats.copy(
                canvasDrawingsCount = currentStats.canvasDrawingsCount + 1
            )
        )
    }
}
