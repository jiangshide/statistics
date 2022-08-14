package com.jingos.statistics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class Constants {

    /**
     * the umeng appkey
     */
    public static final String UMENG_APPKEY = "610a483a26e9627944b5c74e";

    /**
     * the channel name
     */
    public static final String CHANNEL = "test";

    /**
     * the umeng is init
     */
    private static volatile boolean mIsInit = false;

    /**
     * the cus event
     */
    public static String FLAG = "JingOs";

    /**
     * the wait with register
     */
    private static volatile boolean mIsWait = false;

    /**
     * device id
     */
    private static String KEY_UUID = "jingos_uuid";

    private static Context mContext;

    @SuppressLint("StaticFieldLeak")
    private static volatile Application sApplication;

    public static Application getApplicationContext() {
        if (sApplication == null) {
            synchronized (Constants.class) {
                if (sApplication == null) {
                    try {
                        sApplication = (Application) Class.forName("android.app.ActivityThread")
                                .getMethod("currentApplication")
                                .invoke(null, (Object[]) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sApplication;
    }

    /**
     * Return the application's package name.
     *
     * @return the application's package name
     */
    public static String getPackageName() {
        return getApplicationContext().getPackageName();
    }

    public static void registerUM(Context context, String tag) {
        mContext = context;
        if ((UMConfigure.getInitStatus() && UMConfigure.isInit) || (mContext == null)) return;
        UMConfigure.preInit(mContext, Constants.UMENG_APPKEY, Constants.CHANNEL);
        UMConfigure.init(mContext, Constants.UMENG_APPKEY, Constants.CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        MobclickAgent.setSessionContinueMillis(30);
        Location.getInstance().setDefaultPrivacy(context).initLocation(context);
        if (!mIsWait) {
            waitRegisterUM(context, tag);
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static void waitRegisterUM(Context context, final String tag) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                while (!mIsInit) {
                    try {
                        Boolean initStatus = UMConfigure.getInitStatus();
                        UMConfigure.needSendZcfgEnv(context);
                        Boolean isInit = UMConfigure.isInit;
                        String[] testInfos = UMConfigure.getTestDeviceInfo(context);
                        String umengZID = UMConfigure.getUmengZID(context);
                        String umidString = UMConfigure.getUMIDString(context);
                        LogUtil.e("-----uemngZID:", umengZID, " | umidString:", umidString);
                        for (String testInfo : testInfos) {
                            LogUtil.e("----testInfo:", testInfo);
                        }
                        String display = Build.DISPLAY;
                        StringBuilder stringBuilder = new StringBuilder(display);
                        String deviceId = stringBuilder.toString();
                        Log.i("--jjjjsd--", "log~initStatus:" + initStatus + " | isInit:" + isInit + " | tag:" + tag + " | deviceId:" + deviceId);
                        if (!isNetworkAvailable(context)) {
                            return;
                        }
                        if(!Location.getInstance().isRegisterRealNet){
                            Location.getInstance().startLocation();
                        }
                        if (!initStatus || !isInit) {
                            registerUM(mContext, tag);
                        } else {
//                            mIsInit = true;
                            mIsWait = true;
                            MobclickAgent.onResume(context);
                            MobclickAgent.onPause(context);
                            MobclickAgent.onEvent(context, deviceId);
                        }
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        Log.i("--jsd--", "log~e:" + e.getMessage() + " | tag:" + tag);
                    }
                }
            }
        }).start();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            return network != null;
        }
        return false;
    }

    public static Network getNetwork(Context context) {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetwork();
        }
        return null;
    }

    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() == 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * get Ip
     *
     * @return
     */
    private static String getHostIP() {
        String hostIp = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("--jsd--", "log~e:" + e.getMessage());
        }
        return hostIp;
    }

    public static String getCpuAbi() {
        try {
            String osCpuAbi = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
            if (osCpuAbi.contains("x86")) {
                return "x86";
            } else if (osCpuAbi.contains("armeabi-v7a") || osCpuAbi.contains("arm64-v8a")) {
                return "armeabi-v7a";
            } else {
                return "armeabi";
            }
        } catch (Exception e) {
            return "armeabi";
        }
    }

    public static boolean putString(String key, String value) {
        LogUtil.e("key:", key, " | value:", value);
        return getEdit().putString(key, value).commit();
    }

    public static String getString(String key) {
        String value = getSharedPreferences().getString(key, "");
        return value;
    }

    public static SharedPreferences.Editor getEdit() {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences() {
        return getApplicationContext()
                .getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    public static String getIMEIDeviceId(Context context) {

        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.e("--jsd--o", "---deviceId:" + deviceId);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                    LogUtil.e("---------imei:", deviceId);
                } else {
                    deviceId = mTelephony.getDeviceId();
                    LogUtil.e("-----------deviceId:", deviceId);
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }
}
