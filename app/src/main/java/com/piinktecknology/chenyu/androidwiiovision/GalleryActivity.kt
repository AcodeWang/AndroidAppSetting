package com.piinktecknology.chenyu.androidwiiovision

import android.app.Fragment
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_main.*

class GalleryActivity : AppCompatActivity() {

    private lateinit var gridLayoutManager : GridLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        //val photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + intent.getStringExtra("rootPath") + "/" + intent.getStringExtra("photoPath"))
        val photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "D" + "/" + "Default")
        val photoFiles = photoDir.listFiles()

        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        adapter = RecyclerAdapter(photoFiles)
        recyclerView.adapter = adapter

        galleryPhotoButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.setAction("clickPhotoBtn")
            startActivity(intent)
            finish()
        }
    }

}
