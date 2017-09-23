package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import io.realm.Realm;


public class CommonValueApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        Realm.init(getApplicationContext());

        super.onCreate();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
