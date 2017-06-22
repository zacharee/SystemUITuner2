package com.zacharee1.systemuituner;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.zacharee1.systemuituner.services.ShutDownListen;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Zacha on 5/1/2017.
 */

public class AppService extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getText(R.string.sharedprefs_id).toString(), MODE_PRIVATE);

        if (sharedPreferences.getBoolean("useFabric", true)) {
            Fabric.with(this, new Crashlytics());
        }
        startService(new Intent(this, ShutDownListen.class).setData(Uri.parse("http://test.com")));
    }
}
