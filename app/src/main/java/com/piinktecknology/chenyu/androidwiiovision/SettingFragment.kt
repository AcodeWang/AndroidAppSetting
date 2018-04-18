package com.piinktecknology.chenyu.androidwiiovision

import android.content.SharedPreferences
import android.preference.PreferenceFragment
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import java.util.*
import android.content.Intent




/**
 * Created by chenyu on 20/03/2018.
 */

class SettingFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference)

        for((key, value) in preferenceScreen.sharedPreferences.all){

            when{
                key.contains("password") ->
                        findPreference(key).setSummary("********")
                key.equals("language") ->
                {
                    if(value.toString().equals("en")){
                        resources.configuration.setLocale(Locale.ENGLISH)
                        findPreference(key).setSummary("English")

                    }
                    else if (value.toString().equals("fr")){
                        resources.configuration.setLocale(Locale.FRANCE)
                        findPreference(key).setSummary("Français")
                    }
                }
                else ->
                    findPreference(key).setSummary(value.toString())
            }

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

        when{
            key?.contains("password")!! ->
                changedPreferenced.setSummary("********")
            key?.equals("language") ->
            {
                var language = sharedPreferences.getString(key,"")

                if(language.equals("en")){
                    resources.configuration.setLocale(Locale.ENGLISH)
                    changedPreferenced.setSummary("English")

                }
                else if (language.equals("fr")){
                    resources.configuration.setLocale(Locale.FRANCE)
                    changedPreferenced.setSummary("Français")
                }

                activity.finish()
                val intent = Intent(activity,activity::class.java)
                startActivity(intent)

                resources.updateConfiguration(resources.configuration,resources.displayMetrics)
            }
            else ->
                changedPreferenced.setSummary(sharedPreferences.getString(key,""))
        }
    }

}