package com.example.heojuyeong.foodandroid.common;

import android.app.Application;

/**
 * Created by heojuyeong on 2017. 9. 12..
 */

public class CommonTotalPriceApplication extends Application {
    private int totalPrice;
    private String totalPriceString;
    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
