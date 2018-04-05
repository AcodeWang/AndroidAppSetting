package com.piinktecknology.chenyu.androidwiiovision

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class TransferActivity : AppCompatActivity() {

    lateinit var photoPathList : ArrayList<String>
    companion object {
        lateinit var sharedPreference : SharedPreferences
        lateinit var archivePath :String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        photoPathList = intent.getStringArrayListExtra("photoPathList")

        checkPathMakeDir("Archive")
        if(checkPathMakeDir("/Archive/" + sharedPreference.getString("fullPath","").split("/").last())){
            archivePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "Archive/" + sharedPreference.getString("fullPath","").split("/").last()).path
        }

        TransferFTP().execute(photoPathList)
    }

    fun checkPathMakeDir(path : String):Boolean{
        val rootPathFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + path)
        //check the root path and make dir if not existed
        if(rootPathFile.exists()){
            if(!rootPathFile.isDirectory){
                return false
            }
            return true
        }
        else{
            File(Environment.DIRECTORY_PICTURES + "/" + path)
            return true
        }
    }


    class TransferFTP : AsyncTask<ArrayList<String>, Int, Boolean>(){

        val ftpClient = FTPClient()
        val ipAddress = sharedPreference.getString("transfer_ip","")
        val transferRootPath = sharedPreference.getString("transfer_root", "")
        val fullPath = sharedPreference.getString("fullPath","")

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

            if(ftpClient.login("testftp", "testftp")){

                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                //Check the root directory exist and change work directory


                checkRomotePahtCWD(transferRootPath)
                checkRomotePahtCWD(fullPath.split("/").last())

                for(photoPath in photoPathList){
                    val photoFile = File(photoPath)
                    val fileStream = FileInputStream(photoFile)
                    val result =ftpClient.storeFile(photoFile.name, fileStream)

                    if(result){
                        copyFile(photoFile, File(TransferActivity.archivePath + "/" + photoFile.name))
                        photoFile.delete()
                    }
                    fileStream.close()
                }

                ftpClient.logout();
                ftpClient.disconnect();
                return true
            }
            else{
                return false
            }
//            return true
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if(result){
                Log.d("FTPResult", "OK")
            }
        }

        fun checkRomotePahtCWD(path : String){

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

    }

}
