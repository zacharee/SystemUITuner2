package com.zacharee1.systemuituner

import android.app.Activity
import android.provider.Settings

/**
 * Created by Zacha on 6/22/2017.
 */


class WriteSettings(activity: Activity) {
    private val mActivity: Activity = activity

    @Throws(Exception::class)
    fun writeSystem(key: String, value: String) {
        Settings.System.putString(mActivity.contentResolver, key, value)
    }
}