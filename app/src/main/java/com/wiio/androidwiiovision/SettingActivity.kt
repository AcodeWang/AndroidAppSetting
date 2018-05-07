package com.wiio.androidwiiovision

import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.wiio.androidwiiovision.R

/**
 * Created by chenyu on 20/03/2018.
 */

class SettingActivity : AppCompatActivity(){

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_setting)

//        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().clear().commit()

        // Display the fragment as the main content.
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingFragment()).commit()

    }

    fun getPreferencesFragment():PreferenceFragment{
        return fragmentManager.findFragmentById(android.R.id.content) as PreferenceFragment
    }

    fun getSettingEntryFromSharedPreference(): SettingEntry {

        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        var rootPath = mSharedPreferences.getString("transfer_root",applicationContext.getString(R.string.transfer_root))
        var ip = mSharedPreferences.getString("transfer_ip",applicationContext.getString(R.string.transfer_ip))
        var transferMode = mSharedPreferences.getString("transfer_mode",applicationContext.getString(R.string.transfer_mode))

        var settingEntry = SettingEntry(rootPath, ip, transferMode)
        return settingEntry
    }

}

