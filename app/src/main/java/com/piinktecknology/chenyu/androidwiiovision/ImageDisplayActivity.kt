package com.piinktecknology.chenyu.androidwiiovision

import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_image_display.*

class ImageDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        val photoPath = intent.getStringExtra("PHOTO")

        val bitmap = BitmapFactory.decodeFile(photoPath)

        photo_view.setImageBitmap(bitmap)
    }

}
