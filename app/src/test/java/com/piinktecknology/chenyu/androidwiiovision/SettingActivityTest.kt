package com.piinktecknology.chenyu.androidwiiovision

import android.preference.PreferenceManager
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith
import org.robolectric.*
import org.junit.Test

/**
 * Created by chenyu on 22/03/2018.
 */

@RunWith(RobolectricTestRunner::class)
class SettingActivityTest(){

    val IP_KEY = "transfer_ip"
    val newIP = "192.168.1.101"

    val settingActivity = Robolectric.setupActivity(SettingActivity::class.java)
    val settingFragment = settingActivity.getPreferencesFragment()
    val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(settingActivity.applicationContext)



    @Before
    fun initSetting(){

//        Assert.assertEquals(true,resualt)
    }

    @Test
    fun getSettingFromSharedPreferencesTest(){
        val settingEntry = settingActivity.getSettingEntryFromSharedPreference()
        Assert.assertEquals(settingActivity.applicationContext.getString(R.string.transfer_mode), settingEntry.ip)
    }


    //Test the preferences changed by user in setting activity and update the new value & summary
    @Test
    fun preferencesValueChangedTest(){

        var ipPreference = settingFragment.findPreference(IP_KEY)
        var resualt = mSharedPreferences.edit().putString(IP_KEY,newIP).apply()

        Assert.assertEquals("If the value is changed",newIP,mSharedPreferences.getString(IP_KEY,""))
        Assert.assertEquals("If the summary is changed", newIP,ipPreference.summary)

    }

}