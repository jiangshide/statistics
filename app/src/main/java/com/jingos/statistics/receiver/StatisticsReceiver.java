package com.jingos.statistics.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.jingos.statistics.service.LogService;

public class StatisticsReceiver extends BroadcastReceiver {

    private final String TAG = "StatisticsReceiver";

    private final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "onReceiver:" + action);
        if (action.equals(ACTION)) {
            Intent serviceIntent = new Intent(context, LogService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }
    }
}
