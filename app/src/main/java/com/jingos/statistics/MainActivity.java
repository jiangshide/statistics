package com.jingos.statistics;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import java.util.Properties;


public class MainActivity extends Activity {

    private volatile int mRetryNum = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String model = Build.MODEL;
        String brand = Build.BRAND;
        String display = Build.DISPLAY;

       test();
    }

    private void test(){
        mRetryNum--;
        LogUtil.e("----------------->result:",1 << 1," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",2 << 2," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",3 << 3," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",4 << 4," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",5 << 5," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",6 << 6," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",7 << 7," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",8 << 8," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",9 << 9," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",10 << 10," | retryNum:",mRetryNum);
        LogUtil.e("----------------->result:",mRetryNum);
//        TimeUtil.getInstance().setListener(new TimeUtil.OnTimeListener() {
//            @Override
//            public void format(String time) {
//                LogUtil.e("----------------->result:",time);
//            }
//
//            @Override
//            public void stop() {
//                LogUtil.e("----------------->result:");
//            }
//        }).setSecond(10).start(TimeUtil.Mode.COUNTDOWNSECOND);
//        if(mRetryNum > 0){
//            test();
//        }
    }
}