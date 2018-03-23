package com.piinktecknology.chenyu.androidwiiovision

import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowApplication

/**
 * Created by chenyu on 23/03/2018.
 */

@RunWith(RobolectricTestRunner::class)
class MainAcitivityTest(){

    var mainAcitivity = Robolectric.setupActivity(MainActivity::class.java)

    @Test
    fun settingBtnClickTest(){

        var settingBtn = mainAcitivity.settingButton
        settingBtn.performClick()

        val intent = Shadows.shadowOf(mainAcitivity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(intent)

        Assert.assertEquals("",SettingActivity::class.java, shadowIntent.intentClass)

    }

}