package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FavoriteSyllable
import com.example.data.local.entity.LearningStatsEntity
import com.example.data.local.entity.QuizScoreRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface BarahkhadiDao {

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteSyllable>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE hindi = :hindi LIMIT 1)")
    fun isFavoriteFlow(hindi: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE hindi = :hindi LIMIT 1)")
    suspend fun isFavorite(hindi: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteSyllable)

    @Query("DELETE FROM favorites WHERE hindi = :hindi")
    suspend fun deleteFavoriteByHindi(hindi: String)

    @Query("SELECT * FROM quiz_records ORDER BY timestamp DESC LIMIT 20")
    fun getQuizHistory(): Flow<List<QuizScoreRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizScore(record: QuizScoreRecord)

    @Query("SELECT * FROM learning_stats WHERE id = 1")
    fun getLearningStats(): Flow<LearningStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStats(stats: LearningStatsEntity)
}
