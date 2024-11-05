package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var classifyResult: String? = null

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("Photo Picker", "Selected Uri: $uri")
            currentImageUri = uri
            showImage()
            analyzeImage(currentImageUri)
        } else {
            Log.d("Photo Picker", "No Image Selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            moveToResult()
        }
    }

    private fun startGallery() {
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(imageUri: Uri?) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object: ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResult(result: List<Classifications>?, inferenceTime: Long) {
                    result?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            val sortedCategories = it[0].categories.sortedByDescending { it?.score }
                            val displayResult = sortedCategories.joinToString("\n") {
                                "${it.label} " + NumberFormat.getPercentInstance()
                                    .format(it.score).trim()
                            }
                            classifyResult = displayResult
                        } else {
                            classifyResult = "Inference Failed"
                        }
                        Log.d("GoTo Activity", classifyResult.toString())
                    }

                }
            }
        )

        currentImageUri?.let {
            imageClassifierHelper.classifyStaticImage(imageUri)
        }
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT_SCORE, classifyResult)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}