package com.dicoding.asclepius.view.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.remote.NewsRepository
import com.dicoding.asclepius.di.NewsInjection


class NewsViewModelFactory private constructor(private val newsRepository: NewsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class" + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: NewsViewModelFactory? = null
        fun getInstance(): NewsViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsViewModelFactory(NewsInjection.provideRepository())
            }.also { INSTANCE = it }
    }
}