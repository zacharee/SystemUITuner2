package com.zacharee1.systemuituner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

/**
 * Created by Zacha on 4/18/2017.
 */

@SuppressWarnings("ALL")
public class Exceptions {
    private final int alertRed = R.drawable.ic_warning_red;

    public void secureSettings(Context context, final Context appContext, String message, String page) { //exception function for a Secure Settings problem
        Log.e(page, message); //log the error
        new AlertDialog.Builder(context) //show a dialog with the error and prompt user to set up permissions again
                .setIcon(alertRed)
                .setTitle(Html.fromHtml("<font color='#ff0000'>ERROR</font>"))
                .setMessage("Looks like you may not have set up permissions correctly. For reference, here's the error:\n\n\"" + message + "\"\n\nWould you like to enter setup now?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(appContext, SetupActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        appContext.startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void systemSettings(Context context, final Context appContext, String message, String page) { //exceptions for System Settings (should only be needed on API > 22)
        Log.e(page, message); //log error
        new AlertDialog.Builder(context) //show details dialog
                .setIcon(alertRed)
                .setTitle(Html.fromHtml("<font color='#ff0000'>ERROR</font>"))
                .setMessage("Didn't work :/. For reference, here's the error:\n\n\"" + message + "\"")
                .setPositiveButton("OK", null)
                .show();
    }
}
