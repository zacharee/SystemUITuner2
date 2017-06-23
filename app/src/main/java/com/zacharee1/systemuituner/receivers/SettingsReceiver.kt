package com.zacharee1.systemuituner.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast

//noinspection deprecation
class SettingsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "CUSTOM_SETTINGS_GLOBAL") {
            Toast.makeText(context, "Settings.Global -- " + intent.dataString, Toast.LENGTH_SHORT).show()
            Log.i("Settings.Global", intent.dataString)
            try {
                val arr: ArrayList<String> = ArrayList(intent.dataString.split(":"))
                if (arr.size < 2) arr[1] = ""
                Settings.Global.putString(context.contentResolver, arr[0], arr[1])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (intent.action == "CUSTOM_SETTINGS_SECURE") {
            Toast.makeText(context, "Settings.Secure -- " + intent.dataString, Toast.LENGTH_SHORT).show()
            Log.i("Settings.Secure", intent.dataString)
            try {
                val arr: ArrayList<String> = ArrayList(intent.dataString.split(":"))
                if (arr.size < 2) arr[1] = ""
                Settings.Secure.putString(context.contentResolver, arr[0], arr[1])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (intent.action == "CUSTOM_SETTINGS_SYSTEM") {
            Toast.makeText(context, "Settings.System -- " + intent.dataString, Toast.LENGTH_SHORT).show()
            Log.i("Settings.System", intent.dataString)
            try {
                val arr: ArrayList<String> = ArrayList(intent.dataString.split(":"))
                if (arr.size < 2) arr[1] = ""
                Settings.System.putString(context.contentResolver, arr[0], arr[1])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
