package com.piinktecknology.chenyu.androidwiiovision

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created by chenyu on 21/03/2018.
 */

@RunWith(MockitoJUnitRunner::class)
class SettingTest{

    val TEST_Root = "MPF"
    val TEST_IP = "192.168.1.31"
    val TEST_TransferMode = "HTTP"

    var settingEntry = SettingEntry(TEST_Root,TEST_IP,TEST_TransferMode)

    @Mock
    lateinit var setting : SettingActivity

    @Mock
    lateinit var appContext : Context

    @Mock
    lateinit var settingHelper:ISettingHelper

    @Mock
    lateinit var mSharedPreferences:SharedPreferences
    @Mock
    lateinit var mEditor:SharedPreferences.Editor

    @Before
    fun initMock() {

    }

    @Test
    fun sharedPreferencesSetTest(){

        Mockito.`when`(mSharedPreferences.getString("transfer_root","default")).thenReturn(TEST_Root)

        Assert.assertEquals("If get transfer_root then got it right", TEST_Root, mSharedPreferences.getString("transfer_root","default"))


//        Mockito.`when`(mEditor.commit()).thenReturn(true)
//
//        Assert.assertEquals(true,mEditor.commit())
    }

    @Test
    fun settingGetPreferecesTest(){

        Mockito.`when`(setting.getSettingEntryFromSharedPreference(appContext)).thenReturn(settingEntry)

        Assert.assertEquals(settingEntry, setting.getSettingEntryFromSharedPreference(appContext))

    }

}