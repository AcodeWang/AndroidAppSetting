package com.piinktecknology.chenyu.androidwiiovision

import android.preference.PreferenceFragment
import android.os.Bundle



/**
 * Created by chenyu on 20/03/2018.
 */

class SettingFragment : PreferenceFragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference)
    }
}