package com.wiio.androidwiiovision

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceFragment
import android.os.Bundle
import android.preference.Preference
import java.util.*
import android.content.Intent
import android.os.Environment
import android.preference.PreferenceManager
import android.preference.PreferenceScreen
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wiio.androidwiiovision.R
import kotlinx.android.synthetic.main.activity_transfer.*
import java.io.*




/**
 * Created by chenyu on 20/03/2018.
 */

class SettingFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference)

        if(!preferenceManager.sharedPreferences.contains("clear_memory")){
            preferenceManager.sharedPreferences.edit().putString("clear_memory", "default").commit()
        }

        if(!preferenceManager.sharedPreferences.contains("export_config")){
            preferenceManager.sharedPreferences.edit().putString("export_config", "default").commit()
        }

        if(!preferenceManager.sharedPreferences.contains("import_config")){
            preferenceManager.sharedPreferences.edit().putString("import_config", "default").commit()
        }

        for((key, value) in preferenceScreen.sharedPreferences.all){

//            println(key)

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
                key.equals("export_config") -> {
                    findPreference(key).setOnPreferenceClickListener(Preference.OnPreferenceClickListener {

                        val languageSettingData = LanguageSettingData("")
                        val imageSettingData = ImageSettingData("")
                        val transferSettingData = TransferSettingData("","","","","")

                        for((key, value) in preferenceScreen.sharedPreferences.all){
                            when{
                                key.equals("language") ->{
                                    languageSettingData.language = value.toString()
                                    println(languageSettingData.language)
                                }
                                key.equals("photo_quality") ->{
                                    imageSettingData.quality = value.toString()
                                }
                                key.equals("login_user")->{
                                    transferSettingData.user = value.toString()
                                }
                                key.equals("login_password")->{
                                    transferSettingData.password = value.toString()
                                }
                                key.equals("transfer_root")->{
                                    transferSettingData.root = value.toString()
                                }
                                key.equals("transfer_ip")->{
                                    transferSettingData.ip = value.toString()
                                }
                                key.equals("transfer_mode")->{
                                    transferSettingData.mode = value.toString()
                                }
                            }
                        }

                        val configData = ConfigData(transferSettingData,languageSettingData,imageSettingData)

                        val gson = GsonBuilder().create()
                        val jsonString = gson.toJson(configData)
                        println(jsonString)

                        try{

                            val configFile = File(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString(), "config.json")

                            val fos = FileOutputStream(configFile)
                            fos.write(jsonString.toByteArray())
                            fos.close()

                            Toast.makeText(activity, R.string.export_settings_success, Toast.LENGTH_LONG).show()
                        }
                        catch (e:Exception){
                            println(e)
                        }
                        true
                    })
                }
                key.equals("import_config") -> {
                    findPreference(key).setOnPreferenceClickListener(Preference.OnPreferenceClickListener {

                        try{

                            val configFile = File(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString(), "config.json")

                            val fis = FileInputStream(configFile)

                            val reader = BufferedReader(InputStreamReader(fis))
                            val sb = StringBuilder()
                            var line: String? = null
                            line = reader.readLine()
                            while (line != null) {
                                sb.append(line).append("\n")
                                line = reader.readLine()
                            }
                            reader.close()

                            val jsonString:String = sb.toString()
                            fis.close()
                            val gson = GsonBuilder().create()
                            val configdata = gson.fromJson<ConfigData>(jsonString, ConfigData::class.java)

                            println(configdata)

                            val editor = preferenceScreen.sharedPreferences.edit()
                            editor.putString("language", configdata.languageSetting.language)
                            editor.putString("photo_quality", configdata.imageSettingData.quality)
                            editor.putString("login_user", configdata.transferSettingData.user)
                            editor.putString("login_password",configdata.transferSettingData.password)
                            editor.putString("transfer_root", configdata.transferSettingData.root)
                            editor.putString("transfer_ip", configdata.transferSettingData.ip)
                            editor.putString("transfer_mode",configdata.transferSettingData.mode)
                            editor.commit()

                            Toast.makeText(activity, R.string.import_settings_success, Toast.LENGTH_LONG).show()
                        }
                        catch (e:Exception){
                            println(e)
                        }

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