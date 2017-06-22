package com.zacharee1.systemuituner.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import android.widget.Toast

//noinspection deprecation
class SettingsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("RECEIVED", "REC")
        if (intent.action == "com.zacharee1.systemuituner.CUSTOM_SETTINGS_GLOBAL") {
            Toast.makeText(context, "Global", Toast.LENGTH_SHORT).show()
        }
        if (intent.action == "com.zacharee1.systemuituner.CUSTOM_SETTINGS_SECURE") {
            Toast.makeText(context, "Secure", Toast.LENGTH_SHORT).show()
        }
        if (intent.action == "com.zacharee1.systemuituner.CUSTOM_SETTINGS_SYSTEM") {
            Toast.makeText(context, "System", Toast.LENGTH_SHORT).show()
        }
    }
}
