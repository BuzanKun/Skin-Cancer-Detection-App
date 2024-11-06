package com.dicoding.asclepius.view.news

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.remote.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getNews() = newsRepository.getNews()
}