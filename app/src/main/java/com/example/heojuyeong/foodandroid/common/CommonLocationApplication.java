package com.example.heojuyeong.foodandroid.common;

import android.app.Application;



public class CommonLocationApplication extends Application {
    private String locationName;
    private double lat;
    private double lng;

    @Override
    public void onCreate() {
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
