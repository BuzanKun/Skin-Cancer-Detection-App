package com.dicoding.asclepius.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.local.entity.ResultEntity
import com.dicoding.asclepius.data.local.room.ResultDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResultRepository private constructor(
    private val resultDao: ResultDao
) {
    fun getAllEvents(): LiveData<Result<List<ResultEntity>>> = liveData {
        emit(Result.Loading)
        val localData: LiveData<Result<List<ResultEntity>>> =
            resultDao.getAllResult().map { Result.Success(it) }
        emitSource(localData)
    }

    suspend fun deleteResult(result: ResultEntity) {
        withContext(Dispatchers.IO) {
            resultDao.deleteResult(result)
        }
    }

    suspend fun insertResult(result: ResultEntity) {
        withContext(Dispatchers.IO) {
            resultDao.insertResult(result)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ResultRepository? = null
        fun getInstance(
            resultDao: ResultDao
        ): ResultRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ResultRepository(resultDao)
            }.also { INSTANCE = it }
    }
}