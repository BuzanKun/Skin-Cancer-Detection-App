package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.ResultEntity

@Database(entities = [ResultEntity::class], version = 1, exportSchema = false)
abstract class ResultDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile
        private var INSTANCE: ResultDatabase? = null
        fun getInstance(context: Context): ResultDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ResultDatabase::class.java, "Result.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

    }
}