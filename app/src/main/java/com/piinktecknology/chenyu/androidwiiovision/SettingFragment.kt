package com.piinktecknology.chenyu.androidwiiovision

import android.content.SharedPreferences
import android.preference.PreferenceFragment
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log


/**
 * Created by chenyu on 20/03/2018.
 */

class SettingFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference)

        for((key, value) in preferenceScreen.sharedPreferences.all){
            findPreference(key).setSummary(value.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    //When preference changed set the UI summary
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {

        val changedPreferenced = this.findPreference(key)
        changedPreferenced.setSummary(sharedPreferences.getString(key,""))
    }
}