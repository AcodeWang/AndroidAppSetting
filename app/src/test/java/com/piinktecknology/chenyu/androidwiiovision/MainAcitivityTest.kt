package com.wiio.androidwiiovision

import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import com.wiio.androidwiiovision.MainActivity
import com.wiio.androidwiiovision.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

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

        Assert.assertEquals("", SettingActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun photoBtnClickTest(){
        var photoBtn = mainActivity.photoButton
        photoBtn.performClick()

        Assert.assertEquals(mainActivity.profileName, mainActivity.profileNameEditText.text.toString())
        Assert.assertTrue(mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + mainActivity.rootPath + "/" + mainActivity).isDirectory)

        val shadowAcitivity = Shadows.shadowOf(mainActivity)
        Assert.assertEquals(Intent(MediaStore.ACTION_IMAGE_CAPTURE).toString(), shadowAcitivity.nextStartedActivityForResult.intent.toString())
    }

}