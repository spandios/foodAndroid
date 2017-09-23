package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import io.realm.Realm;


public class CommonLocationApplication extends Application {
    private String locationName;
    private double lat;
    private double lng;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        Realm.init(getApplicationContext());
        locationName="";
        super.onCreate();
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public String getLocationName() {
        return locationName;
    }

    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
    public void setLatLng(Double lat,double lng){
        this.lat=lat;
        this.lng=lng;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
