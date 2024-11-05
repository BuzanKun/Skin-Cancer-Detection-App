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
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class   MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var classifyResult: String? = null
    private var classifyPercentage: String? = null

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("Photo Picker", "Selected Uri: $uri")
            startUCrop(uri)
        } else {
            Log.e("Photo Picker", "No Image Selected")
        }
    }

    private val cropImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultImage ->
        if (resultImage.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(resultImage.data!!)
            resultUri?.let {
                currentImageUri = it
                showImage()
                analyzeImage(currentImageUri)
            }
        } else if (resultImage.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(resultImage.data!!)
            cropError?.let {
                showToast(it.message.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object: ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResult(result: List<Classifications>?, inferenceTime: Long) {
                    result?.firstOrNull()?.categories?.maxByOrNull { it.score }?.let { topCategory ->
                        classifyResult = topCategory.label
                        classifyPercentage = NumberFormat.getPercentInstance().format(topCategory.score).trim()
                    } ?: run {
                        classifyResult = "Inference Failed"
                        classifyPercentage = "0%"
                    }

                }
            }
        )

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

    private fun startUCrop(sourceUri: Uri) {
        try {
            val resultUri = Uri.fromFile(File(this.cacheDir, "crop_image.png"))
            val uCrop = UCrop.of(sourceUri, resultUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(244,244)

            val uCropIntent = uCrop.getIntent(this)
            cropImage.launch(uCropIntent)
        } catch (e: Exception) {
            showToast("Failed to Crop File")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(null)
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(imageUri: Uri?) {
        currentImageUri?.let {
            imageClassifierHelper.classifyStaticImage(imageUri)
        }
    }

    private fun moveToResult() {
        currentImageUri?.let {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
            intent.putExtra(ResultActivity.EXTRA_RESULT, classifyResult)
            intent.putExtra(ResultActivity.EXTRA_RESULT_PERCENTAGE, classifyPercentage)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}