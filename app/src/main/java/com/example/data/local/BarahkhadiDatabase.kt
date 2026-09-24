package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BarahkhadiDao
import com.example.data.local.entity.FavoriteSyllable
import com.example.data.local.entity.LearningStatsEntity
import com.example.data.local.entity.QuizScoreRecord

@Database(
    entities = [
        FavoriteSyllable::class,
        QuizScoreRecord::class,
        LearningStatsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BarahkhadiDatabase : RoomDatabase() {

    abstract fun barahkhadiDao(): BarahkhadiDao

    companion object {
        @Volatile
        private var INSTANCE: BarahkhadiDatabase? = null

        fun getInstance(context: Context): BarahkhadiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarahkhadiDatabase::class.java,
                    "barahkhadi_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
