package com.wiio.androidwiiovision

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.fragment_header.*
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import android.graphics.Bitmap.CompressFormat
import java.io.*
import com.wiio.androidwiiovision.R


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

        var quality = 100

        override fun onPreExecute() {
            super.onPreExecute()

            try{
                quality = sharedPreference.getString("photo_quality", "100").toInt()
            }
            catch (e : Exception){

            }

            if (!(quality >= 0 && quality <= 100)){
                quality = 100
            }
        }

        @Override
        override fun doInBackground(vararg params: ArrayList<String>): Boolean {
            val photoPathList = params[0]

            try {
                ftpClient.connect(ipAddress, 21)
            }
            catch (e:Exception){
                Log.e("exp", e.toString())
                return false
            }

            val user = sharedPreference.getString("login_user","")
            val password = sharedPreference.getString("login_password","")

            try{
                if(ftpClient.login(user, password)){

                    ftpClient.enterLocalPassiveMode()
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                    //Check the root directory exist and change work directory

                    checkRomotePahtCWD(ftpClient,transferRootPath)
                    checkRomotePahtCWD(ftpClient, fullPath.split("/").last())

                    for(photoPath in photoPathList){
                        val photoFile = File(photoPath)

                        var bmp = BitmapFactory.decodeFile(photoPath)

                        bmp = drawTextAtBitmap(bmp,photoPath.split("/").last().split(".").first(),100f,200f,160f);

                        val bos = ByteArrayOutputStream()
                        bmp.compress(CompressFormat.JPEG, quality, bos)
                        val bitmapData = bos.toByteArray()
                        val bis = ByteArrayInputStream(bitmapData)

//                        val fileStream = FileInputStream(photoFile)
//                        val result =ftpClient.storeFile(photoFile.name, fileStream)
                        val result =ftpClient.storeFile(photoFile.name, bis)

                        if(result){
                            Thread(Runnable {
                                copyFile(photoFile, File(archivePath + "/" + photoFile.name))
                                photoFile.delete()
                            }).start()
                        }
//                        fileStream.close()
                        bis.close()

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
            } catch (e:Exception){
                Log.e("exp", e.toString())
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

                var trashPath = "Archive" + "/" + sharedPreference.getString("transfer_root", "") + "/" + sharedPreference.getString("fullPath","").split("/").last() + "/" + "Trash"

                if( checkPathMakeDir(trashPath)){

                    trashPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + trashPath).path

                    for (file in getExternalFilesDir(fullPath).listFiles()){
                        try{
                            copyFile(file, File(trashPath + "/"+ file.name))
                            file.delete()
                        }
                        catch (e:Exception){

                        }
                    }
                }

                getExternalFilesDir(fullPath).delete()

                val intent = Intent(this@TransferActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

                Toast.makeText(this@TransferActivity, R.string.transfer_success, Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this@TransferActivity, GalleryActivity::class.java)
                startActivity(intent)
                finish()

                Toast.makeText(this@TransferActivity, R.string.transfer_failed, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun drawTextAtBitmap(bitmap: Bitmap, text: String,x:Float,y:Float, fontsize : Float): Bitmap {

        var name = text.split("_")

        val width = bitmap.width
        val height = bitmap.height

        // 创建一个和原图同样大小的位图
        val newbit = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(newbit)

        val paint = Paint()

        // 在原始位置0，0插入原图
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        paint.color = Color.RED
        paint.setTextSize(fontsize)

        // 在原图指定位置写上字
        canvas.drawText(name[0],  x, height - y, paint)

        paint.setTextSize(200f)
        canvas.drawText(name[1]+"_"+name[2],  x, height - y - 300, paint)


        canvas.save(Canvas.ALL_SAVE_FLAG)

        // 存储
        canvas.restore()
        return newbit
    }

}
