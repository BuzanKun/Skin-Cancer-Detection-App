package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val classifyResult = intent.getStringExtra(EXTRA_RESULT)
        val classifyPercentage = intent.getStringExtra(EXTRA_RESULT_PERCENTAGE)

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }
        classifyResult?.let {
            binding.resultText.text = classifyResult
        }
        classifyPercentage?.let {
            binding.percentageText.text = classifyPercentage
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