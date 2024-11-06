package com.dicoding.asclepius.view.history

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.ResultRepository
import com.dicoding.asclepius.data.local.entity.ResultEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val resultRepository: ResultRepository) : ViewModel() {
    fun getAllResult() = resultRepository.getAllEvents()

    fun insertResult(
        imageUri: Uri,
        resultName: String,
        resultScore: String
    ) {
        val result = ResultEntity(
            imageUri = imageUri.toString(),
            resultName = resultName,
            resultScore = resultScore
        )

        viewModelScope.launch {
            resultRepository.insertResult(result)
        }
    }

    fun deleteResult(result: ResultEntity) {
        viewModelScope.launch {
            resultRepository.deleteResult(result)
        }
    }
}