package com.jingos.statistics.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jingos.statistics.Constants;
import com.jingos.statistics.Location;
import com.jingos.statistics.LogUtil;
import com.jingos.statistics.net.listeners.IHttpListener;
import com.umeng.commonsdk.UMConfigure;

public class LogService extends Service implements Location.ILocation, IHttpListener {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Location.getInstance().setDefaultPrivacy(getBaseContext()).setRequestCallback(this).initLocation(getBaseContext(), this);
        if (!UMConfigure.isInit || !UMConfigure.getInitStatus()) {
            Constants.registerUM(this, "service");
        }
        Location.getInstance().startLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocation(Location.LocationData locationData) {
        LogUtil.e("onLocation~locationData:", locationData);
    }

    @Override
    public void onLocationFail(int code, String info) {
        LogUtil.e("onLocationFail~code:", code, " | info:", info);
    }

    @Override
    public void onSuccess(String response) {
        LogUtil.e("-----onSuccess~response:", response);
    }

    @Override
    public void onFail(Exception e) {
        LogUtil.e("--------onFail~e:", e);
    }
}
