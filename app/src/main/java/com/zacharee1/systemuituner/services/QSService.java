package com.zacharee1.systemuituner.services;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.zacharee1.systemuituner.Exceptions;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 5/21/2017.
 */

@TargetApi(24)
public class QSService extends TileService {

    public static QSService service;
    private Intent mToggleIntent;
    private BroadcastReceiver mToggleReceiver;

    @Override
    public void onStartListening() {
        final Tile nightMode = getQsTile();
        boolean isActive;

        if (Build.VERSION.SDK_INT == 24) isActive = Settings.Secure.getInt(getContentResolver(), "twilight_mode", 0) == 1;
        else {
            isActive = Settings.Secure.getInt(getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, 0) == 1;
            nightMode.setLabel(getResources().getText(R.string.night_display));
        }

        nightMode.setState(isActive ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        nightMode.updateTile();

        service = this;

        mToggleIntent = new Intent("toggle_night");

        mToggleReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("toggle_night")) {
                    boolean state = intent.getBooleanExtra("state", false);
                    nightMode.setState(state ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
                    nightMode.updateTile();
                }
            }
        };

        IntentFilter filter = new IntentFilter("toggle_night");
        registerReceiver(mToggleReceiver, filter);
    }

    @Override
    public void onClick() {
        Tile nightMode = getQsTile();
        int state;

        try {
            state = nightMode.getState();
            if (state == Tile.STATE_ACTIVE) {
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

            mToggleIntent.putExtra("state", state == Tile.STATE_INACTIVE);
            sendBroadcast(mToggleIntent);
        } catch (Exception e) {
            Exceptions exceptions = new Exceptions();
            exceptions.secureSettings(this, getApplicationContext(), e.getMessage(), "QS");
        }

        nightMode.updateTile();
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(mToggleReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
