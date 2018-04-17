package com.piinktecknology.chenyu.androidwiiovision

import android.app.Fragment
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class GalleryActivity : AppCompatActivity() {

    private lateinit var gridLayoutManager : GridLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val photoDir = getExternalFilesDir(PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("fullPath",""))
        val photoFiles = photoDir.listFiles()
        val photoList = ArrayList<File>(photoFiles.asList())

        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        adapter = RecyclerAdapter(photoList)
        recyclerView.adapter = adapter

        galleryPhotoButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.setAction("clickPhotoBtn")
            startActivity(intent)
            finish()
        }

        galleryTrashButton.setOnClickListener{
            adapter.romoveChoosenItem()
        }

        galleryTransferButton.setOnClickListener{

            if(adapter.choosenPhotos.size != 0){
                Toast.makeText(this,"Please remove or release the selected photo before transfer",Toast.LENGTH_LONG).show()
            }
            else{
                val photoPathList = ArrayList<String>()
                for (file in photoList){
                    photoPathList.add(file.absolutePath)
                }

                val intent = Intent(applicationContext, TransferActivity::class.java)
                intent.putStringArrayListExtra("photoPathList", photoPathList)
                startActivity(intent)
                finish()
            }
        }
    }

}
