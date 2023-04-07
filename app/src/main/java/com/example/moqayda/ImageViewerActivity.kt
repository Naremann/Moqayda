package com.example.moqayda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moqayda.databinding.ActivityHomeBinding
import com.example.moqayda.databinding.ActivityImageViewerBinding

class ImageViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val imageUrl = intent.getStringExtra("image_url")
        bindImage(binding.imageViewer, imageUrl)
    }
}