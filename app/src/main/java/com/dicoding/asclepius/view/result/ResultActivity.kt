package com.dicoding.asclepius.view.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.MainActivity
import com.dicoding.asclepius.view.history.HistoryViewModel
import com.dicoding.asclepius.view.history.HistoryViewModelFactory

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory: HistoryViewModelFactory = HistoryViewModelFactory.getInstance(this)
        val viewModel: HistoryViewModel by viewModels {
            factory
        }

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
        val classifyResult = intent.getStringExtra(EXTRA_RESULT) ?: "No Result"
        val classifyPercentage = intent.getStringExtra(EXTRA_RESULT_PERCENTAGE) ?: "0%"

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }
        classifyResult.let {
            binding.resultText.text = classifyResult
        }
        classifyPercentage.let {
            binding.percentageText.text = classifyPercentage
        }

        binding.btSave.setOnClickListener {
            viewModel.insertResult(imageUri!!, classifyResult, classifyPercentage)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("HISTORY", 2)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_RESULT_PERCENTAGE = "extra_result_percentage"
    }
}