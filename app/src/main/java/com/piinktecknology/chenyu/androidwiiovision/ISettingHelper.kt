package com.piinktecknology.chenyu.androidwiiovision

import android.content.Context

/**
 * Created by chenyu on 21/03/2018.
 */
interface ISettingHelper{

    //Get all setting entry by sharedPreferences
    fun getSettingEntryFromSharedPreference(context: Context):SettingEntry

    //Set sharedPreferences entry value
    fun setSettingEntry(rootPath:String, ip:String, transferMode:String, context: Context)

}
