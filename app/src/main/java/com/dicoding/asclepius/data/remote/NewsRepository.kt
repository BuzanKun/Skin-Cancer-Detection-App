package com.dicoding.asclepius.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.BuildConfig.API_KEY
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService

class NewsRepository private constructor(
    private val apiService: ApiService
) {

    fun getNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews(API_KEY)
            val newsData = response.articles?.filterNotNull()
            val news = newsData?.filter { result ->
                result.title != "[Removed]"
            }
            if (news != null) {
                emit(Result.Success(news))
            } else {
                emit(Result.Error("No News available"))
            }
        } catch (e: Exception) {
            Log.e("News Repository", "getNews: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null
        fun getInstance(apiService: ApiService): NewsRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsRepository(apiService)
            }.also { INSTANCE = it }
    }
}