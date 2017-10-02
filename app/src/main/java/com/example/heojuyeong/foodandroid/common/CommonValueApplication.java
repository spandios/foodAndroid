package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.heojuyeong.foodandroid.util.GpsUtil;
import com.example.heojuyeong.foodandroid.util.TedPermissionUtil;
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


        TedPermissionUtil tedPermissionUtil=new TedPermissionUtil(this);
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Logger.addLogAdapter(new AndroidLogAdapter());
        GpsUtil.getGPS(this);





    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
