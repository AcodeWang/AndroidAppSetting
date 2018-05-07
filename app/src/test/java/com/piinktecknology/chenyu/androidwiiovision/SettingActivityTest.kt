package com.wiio.androidwiiovision

import android.preference.PreferenceManager
import com.wiio.androidwiiovision.R
import com.wiio.androidwiiovision.SettingActivity
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.*
import org.junit.Test

/**
 * Created by chenyu on 22/03/2018.
 */

@RunWith(RobolectricTestRunner::class)
class SettingActivityTest(){

    val IP_KEY = "transfer_ip"
    val ROOT_KEY = "transfer_root"
    val newIP = "192.168.1.101"

    val settingActivity = Robolectric.setupActivity(SettingActivity::class.java)
    val settingFragment = settingActivity.getPreferencesFragment()
    val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(settingActivity.applicationContext)



    @Before
    fun initSetting(){

    }

    @Test
    fun initSummaryTest(){
        Assert.assertEquals(mSharedPreferences.getString(ROOT_KEY,""), settingFragment.findPreference(ROOT_KEY).summary)
    }

    @Test
    fun getSettingFromPreferencesFragmentTest(){
        val settingEntry = settingActivity.getSettingEntryFromSharedPreference()
        Assert.assertEquals(settingActivity.applicationContext.getString(R.string.transfer_mode), settingEntry.transfermode)
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