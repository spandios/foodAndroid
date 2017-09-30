package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class CommonValueApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Logger.addLogAdapter(new AndroidLogAdapter());
        GPS_Util gps_util=new GPS_Util(this);
        gps_util.insertDB();


    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
