package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.ResultEntity

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertResult(result: ResultEntity)

    @Query("SELECT * FROM result WHERE id = :id")
    fun getInsertResult(id: Int): LiveData<ResultEntity>

    @Query("SELECT * FROM result ORDER BY id ")
    fun getAllResult(): LiveData<ResultEntity>
}