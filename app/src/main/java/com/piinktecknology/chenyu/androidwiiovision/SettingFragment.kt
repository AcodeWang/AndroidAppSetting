package com.piinktecknology.chenyu.androidwiiovision

import android.content.SharedPreferences
import android.preference.PreferenceFragment
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import java.util.*
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import android.R.attr.path
import java.io.IOException


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

                    resources.updateConfiguration(resources.configuration,resources.displayMetrics)

                }
                key.equals("clear_memory") -> {
                    findPreference(key).setOnPreferenceClickListener(Preference.OnPreferenceClickListener {

                        val file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        if (file.exists()) {
                            val deleteCmd = "rm -r " + file.path
                            val runtime = Runtime.getRuntime()
                            try {
                                runtime.exec(deleteCmd)
                            } catch (e: IOException) {
                            }
                        }
                        Toast.makeText(activity, R.string.clear_memory_success, Toast.LENGTH_LONG).show()
                        true
                    })
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
            key.equals("clear_memory") -> {

            }
            else ->
                changedPreferenced.setSummary(sharedPreferences.getString(key,""))
        }
    }

}