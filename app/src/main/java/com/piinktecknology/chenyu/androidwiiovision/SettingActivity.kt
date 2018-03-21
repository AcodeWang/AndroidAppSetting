package com.piinktecknology.chenyu.androidwiiovision

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by chenyu on 20/03/2018.
 */

class SettingActivity : AppCompatActivity(), ISettingHelper{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_setting)

        // Display the fragment as the main content.
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingFragment()).commit()
    }

    override fun getSettingEntryFromSharedPreference(context:Context):SettingEntry{

        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        var rootPath = mSharedPreferences.getString("transfer_root",context.getString(R.string.transfer_root))
        var ip = mSharedPreferences.getString("transfer_ip",context.getString(R.string.transfer_ip))
        var transferMode = mSharedPreferences.getString("transfer_mode",context.getString(R.string.transfer_mode))

        var settingEntry = SettingEntry(rootPath, ip, transferMode)
        return settingEntry
    }

    override fun setSettingEntry(rootPath:String, ip:String, transferMode:String, context: Context) {

        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val mEditor = mSharedPreferences.edit()

        mEditor.putString("transfer_root",rootPath)
        mEditor.putString("transfer_ip",ip)
        mEditor.putString("transfer_mode", transferMode)
        mEditor.commit()
    }
}

