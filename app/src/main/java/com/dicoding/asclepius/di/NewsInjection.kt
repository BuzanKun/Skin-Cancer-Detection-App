package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.remote.NewsRepository
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object NewsInjection {
    fun provideRepository(): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}