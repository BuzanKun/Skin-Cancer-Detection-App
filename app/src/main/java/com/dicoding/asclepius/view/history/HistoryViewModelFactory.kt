package com.dicoding.asclepius.view.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.local.ResultRepository
import com.dicoding.asclepius.di.ResultInjection

class HistoryViewModelFactory private constructor(private val resultRepository: ResultRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(resultRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryViewModelFactory? = null
        fun getInstance(context: Context): HistoryViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HistoryViewModelFactory(ResultInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}