package com.piinktecknology.chenyu.androidwiiovision

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    val REQUEST_TAKE_PHOTO= 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingButton.setOnClickListener(){
            val intent = Intent(applicationContext, SettingActivity::class.java)
            startActivity(intent)
        }

        photoButton.setOnClickListener(){

            var path = pathEditText.text.toString()
            if(path.equals(null)){
                path = "Default"
            }

            checkPathMakeDir(path)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            takePhoto()
        }
        else{

        }
    }

    fun checkPathMakeDir(path: String) {

        val fullPathFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + path)

        if(fullPathFile.exists()){
            if(!fullPathFile.isDirectory){
                throw PathException("path exsit but not a directory")
            }
        }
        else{
            val newFile = File(Environment.DIRECTORY_PICTURES + "/" + path)
        }
    }

    fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            try{
                CameraActivity.photoFile = createPhotoFile()
                val photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        CameraActivity.photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
            catch (e: IOException){

            }
        }
    }

    @Throws(IOException::class)
    fun createPhotoFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        var mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }

    class PathException(override var message:String): Exception(message)

}
