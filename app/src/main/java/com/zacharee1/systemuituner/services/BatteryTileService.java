package com.zacharee1.systemuituner.services;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.BatteryManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 5/21/2017.
 */

@TargetApi(24)
public class BatteryTileService extends TileService {

    private final IntentFilter mFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    private BC mReceiver;
    private Intent mBatteryStatus;

    @SuppressWarnings("FieldCanBeLocal")
    private boolean mIsCharging;
    @SuppressWarnings("FieldCanBeLocal")
    private int mBatteryPercent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBatteryStatus = registerReceiver(mReceiver = new BC(), mFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStartListening() {
        Tile battery = getQsTile();
        mBatteryStatus = registerReceiver(mReceiver = new BC(), mFilter);

        battery.setState(Tile.STATE_ACTIVE);
        setBat(mBatteryStatus);
        battery.updateTile();
    }

    @Override
    public void onClick() {
        Intent intentBatteryUsage = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        startActivity(intentBatteryUsage);

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(it);
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void setBat(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        mIsCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

//        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        mBatteryPercent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        Tile battery = getQsTile();

        int resId;

        if (mBatteryPercent >= 95) {
             resId = mIsCharging ? R.drawable.ic_battery_charging_full : R.drawable.ic_battery;
        } else if (mBatteryPercent >= 85) {
            resId = mIsCharging ? R.drawable.ic_battery_charging_90 : R.drawable.ic_battery_90;
        } else if (mBatteryPercent >= 70) {
            resId = mIsCharging ? R.drawable.ic_battery_charging_80 : R.drawable.ic_battery_80;
        } else if (mBatteryPercent >= 55) {
            resId = mIsCharging ? R.drawable.ic_battery_charging_60 : R.drawable.ic_battery_60;
        } else if (mBatteryPercent >= 40) {
            resId = mIsCharging ? R.drawable.ic_battery_charging_50 : R.drawable.ic_battery_50;
        } else if (mBatteryPercent >= 25) {
            resId = mIsCharging ? R.drawable.ic_battery_charging_30 : R.drawable.ic_battery_30;
        } else if (mBatteryPercent >= 15) {
            resId = mIsCharging ? R.drawable.ic_battery_charging_20 : R.drawable.ic_battery_20;
        } else {
            resId = mIsCharging ? R.drawable.ic_battery_charging_20 : R.drawable.ic_battery_alert;
        }

        battery.setIcon(Icon.createWithResource(this, resId));
        battery.setLabel(String.valueOf(mBatteryPercent).concat("%"));
        battery.updateTile();
    }

    private class BC extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setBat(intent);
        }
    }
}
