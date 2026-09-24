package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteSyllable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hindi: String,
    val english: String,
    val consonantChar: String,
    val matraIndex: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_records")
data class QuizScoreRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val score: Int,
    val totalQuestions: Int,
    val accuracyPercent: Int = if (totalQuestions > 0) (score * 100) / totalQuestions else 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "learning_stats")
data class LearningStatsEntity(
    @PrimaryKey
    val id: Int = 1,
    val totalAudioPlayed: Int = 0,
    val flashcardsReviewed: Int = 0,
    val quizzesCompleted: Int = 0,
    val canvasDrawingsCount: Int = 0
)
