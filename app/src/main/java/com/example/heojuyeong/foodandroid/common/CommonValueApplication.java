package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.heojuyeong.foodandroid.util.GPS;
import com.example.heojuyeong.foodandroid.util.TedPermissionUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class CommonValueApplication extends Application {

    public double lat;
    public double lng;
    public String locationName;
    public String tempLocationName;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


    @Override
    public void onCreate() {
        super.onCreate();
        new TedPermissionUtil(this);
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Logger.addLogAdapter(new AndroidLogAdapter());
        new GPS(this).getGPS();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }



}
