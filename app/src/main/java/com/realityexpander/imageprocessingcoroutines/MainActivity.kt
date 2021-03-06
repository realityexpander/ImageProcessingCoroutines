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
    private val coroutineScopeMain = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coroutineScopeMain.launch {

            val originalDeferred = coroutineScopeMain.async(Dispatchers.IO) {
                getOriginalBitmap(IMAGE_URL)
            }
            val originalBitmap = originalDeferred.await()
            loadBitmapIntoUi(originalBitmap)

            val filterDeferred = coroutineScopeMain.async(Dispatchers.Default) {
                applyFilter(originalBitmap)
            }
            progressBar.visibility = View.VISIBLE
            val filteredBitmap = filterDeferred.await()
            loadBitmapIntoUi(filteredBitmap)
        }
    }

    // Load bitmap from newotk
    private fun getOriginalBitmap(url: String)  =
        URL(url).openStream()
            .use {
                BitmapFactory.decodeStream(it)
            }

    private fun loadBitmapIntoUi(bmp: Bitmap) {
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bmp)
        imageView.visibility = View.VISIBLE
    }

    private fun applyFilter(originalBitmap: Bitmap) = Filter.apply(originalBitmap)
}
