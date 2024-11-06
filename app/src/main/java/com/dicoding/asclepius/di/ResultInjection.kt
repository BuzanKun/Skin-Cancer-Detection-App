package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.ResultRepository
import com.dicoding.asclepius.data.local.room.ResultDatabase

object ResultInjection {
    fun provideRepository(context: Context): ResultRepository {
        val database = ResultDatabase.getInstance(context)
        val dao = database.resultDao()
        return ResultRepository.getInstance(dao)
    }
}