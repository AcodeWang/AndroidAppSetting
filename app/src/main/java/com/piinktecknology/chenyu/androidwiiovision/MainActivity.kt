package com.piinktecknology.chenyu.androidwiiovision

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display the fragment as the main content.
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingFragment())
                .commit()
    }
}
