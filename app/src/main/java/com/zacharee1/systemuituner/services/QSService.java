package com.zacharee1.systemuituner.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import com.zacharee1.systemuituner.Exceptions;
import com.zacharee1.systemuituner.SetThings;

import java.util.Set;

/**
 * Created by Zacha on 5/21/2017.
 */

@TargetApi(24)
public class QSService extends TileService {

    @Override
    public void onStartListening() {
        Tile nightMode = getQsTile();
        boolean isActive;

        if (Build.VERSION.SDK_INT == 24) isActive = Settings.Secure.getInt(getContentResolver(), "twilight_mode", 0) == 1;
        else isActive = Settings.Secure.getInt(getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, 0) == 1;

        nightMode.setState(isActive ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        nightMode.updateTile();
    }

    @Override
    public void onClick() {
        Tile nightMode = getQsTile();

        try {
            if (nightMode.getState() == Tile.STATE_ACTIVE) {
                if (Build.VERSION.SDK_INT == 24) {
                    Settings.Secure.putInt(getContentResolver(), "twilight_mode", 0);
                } else {
                    Settings.Secure.putInt(getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, 0);
                }
                nightMode.setState(Tile.STATE_INACTIVE);
            } else {
                if (Build.VERSION.SDK_INT == 24) {
                    Settings.Secure.putInt(getContentResolver(), "twilight_mode", 1);
                } else {
                    Settings.Secure.putInt(getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, 1);
                }
                nightMode.setState(Tile.STATE_ACTIVE);
            }
        } catch (Exception e) {
            Exceptions exceptions = new Exceptions();
            exceptions.secureSettings(this, getApplicationContext(), e.getMessage(), "QS");
        }

        nightMode.updateTile();
    }
}
