package com.wiio.androidwiiovision

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.wiio.androidwiiovision.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(){

    val REQUEST_CAMERA = 1;

    var profileName = ""
    var rootPath = ""
    var mCurrentPhotoPath = ""
    val REQUEST_TAKE_PHOTO= 1

    lateinit var sharedPreference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileNameEditText.setText(intent.getStringExtra("wiioparam"))

        checkAppPermission()

        settingButton.setOnClickListener(){

            LoginDialogFragment().show(fragmentManager,"LoginDialog")
        }

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        if(sharedPreference.getString("language", "").equals("en")){
            resources.configuration.setLocale(Locale.ENGLISH)
        }
        else if (sharedPreference.getString("language", "").equals("fr")){
            resources.configuration.setLocale(Locale.FRANCE)
        }

        resources.updateConfiguration(resources.configuration,resources.displayMetrics)

        //PhotoButton Click, check the root and file dir, start image capture activity
        photoButton.setOnClickListener(){

            //Set the photo path as the profile name
            profileName = profileNameEditText.text.toString()
            if(profileName.equals("")) {
                Toast.makeText(this, R.string.profile_name_null,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            checkPathMakeDir(profileName)

            val photoDir = getExternalFilesDir(sharedPreference.getString("fullPath",""))

            if(photoDir.listFiles().size > 0){
                val intent = Intent(applicationContext, GalleryActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                takePhoto(profileName)
            }
        }

        if(intent.action == "clickPhotoBtn") {
            profileName = sharedPreference.getString("fullPath","").split("/").last()
            takePhoto(profileName)
        }

        importConfigFile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
//            takePhoto(profileName)
            val intent = Intent(applicationContext, GalleryActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED){
            //Delete the very last image when return from the capture
            val lastFile = File(mCurrentPhotoPath)
            lastFile.delete()

            val photoDir = getExternalFilesDir(sharedPreference.getString("fullPath",""))

            if(photoDir.listFiles().size > 0){
                val intent = Intent(applicationContext, GalleryActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val tempFile = getExternalFilesDir(sharedPreference.getString("fullPath",""))
                tempFile.delete()
            }
        }
    }

    @TargetApi(25)
    fun checkAppPermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun checkPathMakeDir( fileName: String) {

        rootPath = sharedPreference.getString("transfer_root", "")

        if(rootPath.equals("")){
            rootPath = "DEFAULT"
        }

        val rootPathFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + rootPath)
        //check the root path and make dir if not existed
        if(rootPathFile.exists()){
            if(!rootPathFile.isDirectory){
                throw PathException("path exsit but not a directory")
            }
        }
        else{
            File(Environment.DIRECTORY_PICTURES + "/" + rootPath)
        }

        val fullPathFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + rootPath + "/" + fileName)
        //check the profile path and make dir if not existed
        if(fullPathFile.exists()){
            if(!fullPathFile.isDirectory){
                throw PathException("path exsit but not a directory")
            }
        }
        else{
            File(Environment.DIRECTORY_PICTURES + "/" + rootPath + "/" + fileName)
        }

        sharedPreference.edit().putString("fullPath", Environment.DIRECTORY_PICTURES + "/" + rootPath + "/" + fileName).apply()
    }

    //Start the IMAGE_CAPTURE activity and save the captured photo by file provider
    fun takePhoto(path: String) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            try{
                val photoFile = createPhotoFile(path)
                val photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
            catch (e: IOException){

            }
        }
    }

    //Create the photo file with time stamp
    @Throws(IOException::class)
    fun createPhotoFile(path: String): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        val imageFileName = profileName + "_" + timeStamp
        println(imageFileName)
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + sharedPreference.getString("transfer_root", "") + "/" + path)
        val image = File(storageDir.toString() + "/" + imageFileName + ".jpg")
        image.createNewFile()
//        val image = File.createTempFile(
//                imageFileName, /* prefix */
//                ".jpg", /* suffix */
//                storageDir      /* directory */
//        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }

    class PathException(override var message:String): Exception(message)


    fun importConfigFile(){
        try{

            val configFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString(), "config.json")

            if(configFile.exists()){
                val fis = FileInputStream(configFile)

                val reader = BufferedReader(InputStreamReader(fis))
                val sb = StringBuilder()
                var line: String? = null
                line = reader.readLine()
                while (line != null) {
                    sb.append(line).append("\n")
                    line = reader.readLine()
                }
                reader.close()

                val jsonString:String = sb.toString()
                fis.close()
                val gson = GsonBuilder().create()
                val configdata = gson.fromJson<ConfigData>(jsonString, ConfigData::class.java)

                println(configdata)

                val editor = sharedPreference.edit()
                editor.putString("language", configdata.languageSetting.language)
                editor.putString("photo_quality", configdata.imageSettingData.quality)
                editor.putString("login_user", configdata.transferSettingData.user)
                editor.putString("login_password",configdata.transferSettingData.password)
                editor.putString("transfer_root", configdata.transferSettingData.root)
                editor.putString("transfer_ip", configdata.transferSettingData.ip)
                editor.putString("transfer_mode",configdata.transferSettingData.mode)
                editor.commit()

                Toast.makeText(this, R.string.import_settings_success, Toast.LENGTH_LONG).show()
            }
            else{
                exportConfigFile()
                importConfigFile()
            }
        }
        catch (e:Exception){
            println(e)
        }
    }

    fun exportConfigFile(){

        val languageSettingData = LanguageSettingData("en")
        val imageSettingData = ImageSettingData("100")
        val transferSettingData = TransferSettingData("admin","password","Default","192.168.0.1","FTP")


        val configData = ConfigData(transferSettingData,languageSettingData,imageSettingData)

        val gson = GsonBuilder().create()
        val jsonString = gson.toJson(configData)
        println(jsonString)

        try{

            val configFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString(), "config.json")

            val fos = FileOutputStream(configFile)
            fos.write(jsonString.toByteArray())
            fos.close()

        }
        catch (e:Exception){
            println(e)
        }
    }
}
