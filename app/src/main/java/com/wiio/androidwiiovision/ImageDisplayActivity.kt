package com.wiio.androidwiiovision

import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wiio.androidwiiovision.R
import com.wiio.androidwiiovision.R.id.photo_view
import kotlinx.android.synthetic.main.activity_image_display.*
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.R.attr.paddingTop
import android.R.attr.paddingLeft
import android.R.attr.src
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.graphics.Color.LTGRAY
import android.support.v4.view.ViewCompat.setAlpha
import android.graphics.Typeface
import android.graphics.Paint.DEV_KERN_TEXT_FLAG
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.R.attr.bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.graphics.Color.LTGRAY
import android.support.v4.view.ViewCompat.setAlpha
import android.graphics.Paint.Align
import android.graphics.Paint.DEV_KERN_TEXT_FLAG
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Canvas.ALL_SAVE_FLAG









class ImageDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        val photoPath = intent.getStringExtra("PHOTO")

        val bitmap = BitmapFactory.decodeFile(photoPath)
        var newbit = drawTextAtBitmap(bitmap,photoPath.split("/").last().split(".").first(),100f,200f,160f)
        photo_view.setImageBitmap(newbit)
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
