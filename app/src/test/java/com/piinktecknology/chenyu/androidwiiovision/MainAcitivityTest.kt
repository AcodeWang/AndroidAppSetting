package com.piinktecknology.chenyu.androidwiiovision

import android.content.Intent
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
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

    var mainActivity = Robolectric.setupActivity(MainActivity::class.java)

    @Test
    fun settingBtnClickTest(){

        var settingBtn = mainActivity.settingButton
        settingBtn.performClick()

        val intent = Shadows.shadowOf(mainActivity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(intent)

        Assert.assertEquals("",SettingActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun photoBtnClickTest(){
        var photoBtn = mainActivity.photoButton
        photoBtn.performClick()

        Assert.assertEquals(mainActivity.profileName, mainActivity.profileNameEditText.text.toString())
        Assert.assertTrue(mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + mainActivity.rootPath + "/" + mainActivity.photoPath).isDirectory)

        val shadowAcitivity = Shadows.shadowOf(mainActivity)
        Assert.assertEquals(Intent(MediaStore.ACTION_IMAGE_CAPTURE).toString(), shadowAcitivity.nextStartedActivityForResult.intent.toString())
    }

}