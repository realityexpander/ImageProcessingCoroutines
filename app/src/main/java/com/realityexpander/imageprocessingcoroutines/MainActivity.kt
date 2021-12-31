package com.realityexpander.imageprocessingcoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val IMAGE_URL = "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coroutineScope.launch {

            val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                getOriginalBitmap(IMAGE_URL)
            }
            val originalBitmap = originalDeferred.await()
            loadImage(originalBitmap)

            val filterDeferred = coroutineScope.async(Dispatchers.Default) {
                applyFilter(originalBitmap)
            }
            val filteredImage = filterDeferred.await()
            loadImage(filteredImage)
        }
    }

    // Load bitmap from newotk
    private fun getOriginalBitmap(url: String)  =
        URL(url).openStream()
            .use {
                BitmapFactory.decodeStream(it)
            }

    private fun loadImage(bmp: Bitmap) {
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bmp)
        imageView.visibility = View.VISIBLE
    }

    private fun applyFilter(originalBitmap: Bitmap) = Filter.apply(originalBitmap)
}
