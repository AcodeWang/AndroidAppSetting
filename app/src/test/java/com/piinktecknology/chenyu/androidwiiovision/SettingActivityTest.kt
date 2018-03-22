package com.piinktecknology.chenyu.androidwiiovision

import org.junit.Assert
import org.junit.runner.RunWith
import org.robolectric.*
import org.junit.Test

/**
 * Created by chenyu on 22/03/2018.
 */

@RunWith(RobolectricTestRunner::class)
class SettingActivityTest(){

    @Test
    fun getSettingFromSharedPreferencesTest(){
        val settingActivity = Robolectric.setupActivity(SettingActivity::class.java)

        val settingEntry = settingActivity.getSettingEntryFromSharedPreference()
        Assert.assertEquals(settingActivity.applicationContext.getString(R.string.transfer_ip), settingEntry.ip)
    }

    @Test
    fun addTest(){
        Assert.assertEquals("4",2+2)
    }

}