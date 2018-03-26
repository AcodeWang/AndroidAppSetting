package com.piinktecknology.chenyu.androidwiiovision

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val headerFragment = Fragment.instantiate(applicationContext, HeaderFragment::javaClass.name)

        //val headerFragment = HeaderFragment() as Fragment
        fragmentManager.beginTransaction().replace(R.id.galleryFragment,  HeaderFragment.newInstaance() as Fragment).commit()
    }
}
