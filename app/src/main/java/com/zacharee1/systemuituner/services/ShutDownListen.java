package com.zacharee1.systemuituner.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.zacharee1.systemuituner.receivers.ShutDownReceiver;

public class ShutDownListen extends Service {
    public ShutDownListen() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        BroadcastReceiver mReceiver = new ShutDownReceiver();
        registerReceiver(mReceiver, filter);
        return super.onStartCommand(intent, flags, startId);
    }
}
