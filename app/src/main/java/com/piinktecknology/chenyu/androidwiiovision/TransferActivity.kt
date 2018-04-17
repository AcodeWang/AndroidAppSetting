package com.piinktecknology.chenyu.androidwiiovision

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.support.annotation.UiThread
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.fragment_header.*
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.concurrent.thread

class TransferActivity : AppCompatActivity() {

    lateinit var photoPathList : ArrayList<String>

    lateinit var sharedPreference : SharedPreferences
    lateinit var archivePath :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        settingButton2.setOnClickListener(){
            LoginDialogFragment().show(fragmentManager,"LoginDialog")
        }

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        photoPathList = intent.getStringArrayListExtra("photoPathList")

        if(checkPathMakeDir("Archive" + "/" + sharedPreference.getString("transfer_root", "") + "/"
                        + sharedPreference.getString("fullPath","").split("/").last())){

            archivePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "Archive/" + sharedPreference.getString("transfer_root", "") + "/" +
                    sharedPreference.getString("fullPath","").split("/").last()).path
        }

        val transferMode = sharedPreference.getString("transfer_mode","")

        when(transferMode){
            "FTP" ->
                println("mode FTP")
            "HTTP"->
                println("mode HTTP")

        }

        if(transferMode == "FTP"){
            TransferFTP().execute(photoPathList)
        }
    }

    fun checkPathMakeDir(path : String):Boolean{

        if(!path.contains("/")){
            Log.e("LocalPathCheck", "not goog path format")
            return false
        }

        val paths = path.split("/")

        var tempPath = ""

        for (path in paths){
            tempPath += "/" + path

            val pathFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES + tempPath)

            if(pathFile.exists()){
                if(!pathFile.isDirectory){
                    return false
                }
                continue
            }
            else{
                File(Environment.DIRECTORY_PICTURES + "/" + path)
                continue
            }
        }

        return true
    }

    fun checkRomotePahtCWD(ftpClient: FTPClient, path : String){

        if(!ftpClient.changeWorkingDirectory(path)){
            if(ftpClient.makeDirectory(path)){
                ftpClient.cwd(path)
            }
            else{
                println("creat root directory failed")
            }
        }
    }


    fun copyFile(src:File, dest :File){
        val fileIn = FileInputStream(src)
        val fileOut = FileOutputStream(dest)

        var buf = ByteArray(1024)
        var len : Int = fileIn.read(buf)
        while (len > 0){
            fileOut.write(buf,0 ,len)
            len = fileIn.read(buf)
        }
        fileIn.close()
        fileOut.close()
    }



    inner class TransferFTP : AsyncTask<ArrayList<String>, Int, Boolean>(){

        val ftpClient = FTPClient()
        val ipAddress = sharedPreference.getString("transfer_ip","")
        val transferRootPath = sharedPreference.getString("transfer_root", "")
        val fullPath = sharedPreference.getString("fullPath","")

        val photosNum = photoPathList.size
        var photoCount = 0

        override fun onPreExecute() {
            super.onPreExecute()
        }

        @Override
        override fun doInBackground(vararg params: ArrayList<String>): Boolean {
            val photoPathList = params[0]

            try {
                ftpClient.connect(ipAddress, 21)
            }
            catch (e:Exception){
                Log.e("exp", e.toString())
            }

            val user = sharedPreference.getString("login_user","")
            val password = sharedPreference.getString("login_password","")

            if(ftpClient.login(user, password)){

                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                //Check the root directory exist and change work directory

                checkRomotePahtCWD(ftpClient,transferRootPath)
                checkRomotePahtCWD(ftpClient, fullPath.split("/").last())



                for(photoPath in photoPathList){
                    val photoFile = File(photoPath)
                    val fileStream = FileInputStream(photoFile)
                    val result =ftpClient.storeFile(photoFile.name, fileStream)

                    if(result){
                        Thread(Runnable {
                            copyFile(photoFile, File(archivePath + "/" + photoFile.name))
                            photoFile.delete()
                        }).start()
                    }
                    fileStream.close()
                    photoCount++
                    onProgressUpdate(100 * photoCount / photosNum)
                }

                ftpClient.logout();
                ftpClient.disconnect();
                return true
            }
            else{
                return false
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)

            transferProgressBar.progress = values[0] as Int

            runOnUiThread({
                transferCount.visibility = View.VISIBLE
                transferCount.setText(photoCount.toString() + " / " + photosNum.toString())
            })
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if(result){
                val intent = Intent(this@TransferActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

                Toast.makeText(this@TransferActivity,"Transfer success", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this@TransferActivity,"Transfer failed", Toast.LENGTH_LONG).show()
            }
        }
    }

}
