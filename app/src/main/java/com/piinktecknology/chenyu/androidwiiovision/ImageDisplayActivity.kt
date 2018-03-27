package com.piinktecknology.chenyu.androidwiiovision

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.View.OnTouchListener
import kotlinx.android.synthetic.main.activity_image_display.*
import java.io.File
import android.view.MotionEvent.INVALID_POINTER_ID
import android.R.attr.y
import android.R.attr.x
import android.support.v4.view.MotionEventCompat.getPointerCount
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.v4.view.MotionEventCompat.getActionMasked





class ImageDisplayActivity : AppCompatActivity(), OnTouchListener{

    var mLastTouchX = 0.0f
    var mLastTouchY = 0.0f
    var mPosX = 0.0f
    var mPosY = 0.0f
    var mActivePointerId = INVALID_POINTER_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        val photoPath = intent.getStringExtra("PHOTO")

        displayImageView.setImageBitmap(BitmapFactory.decodeFile(photoPath))

        displayImageView.setOnTouchListener(this)
    }

    override fun onTouch(v: View, e: MotionEvent): Boolean {

//        val actionMask  = event?.actionMasked
//
//        when(actionMask){
//
//            MotionEvent.ACTION_DOWN -> {
//                val pointerIndex = event.actionIndex
//                var x = event.x
//                var y = event.y
//
//                mLastTouchX = x
//                mLastTouchY = y
//
//                mActivePointerId = event.getPointerId(0)
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                // Find the index of the active pointer and fetch its position
//                val pointerIndex = event.findPointerIndex(mActivePointerId)
//
//                var x = event.x
//                var y = event.y
//
//                // Calculate the distance moved
//                var dx = x - mLastTouchX
//                var dy = y - mLastTouchY
//
//                mPosX += dx
//                mPosY += dy
//
//                // Remember this touch position for the next move event
//                mLastTouchX = x
//                mLastTouchY = y
//            }
//
//            MotionEvent.ACTION_UP -> {
//                mActivePointerId = INVALID_POINTER_ID
//            }
//
//            MotionEvent.ACTION_CANCEL -> {
//                mActivePointerId = INVALID_POINTER_ID
//            }
//
//            MotionEvent.ACTION_POINTER_UP -> {
//                var pointerIndex = event.actionIndex
//                var pointerId = event.getPointerId(pointerIndex)
//
//                if(pointerId == mActivePointerId){
//
//                    var newPointerIndex = if(pointerIndex ==0) 1 else 0
//                }
//            }
//
//        }
//
        return true
    }


}
