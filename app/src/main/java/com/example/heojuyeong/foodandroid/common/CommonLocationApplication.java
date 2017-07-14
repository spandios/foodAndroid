package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;

import com.example.heojuyeong.foodandroid.util.geocoderUtil;
import com.example.heojuyeong.foodandroid.util.GPS_Util;

/**
 * Created by heojuyeong on 2017. 7. 12..
 */

public class CommonLocationApplication extends Application {
    private String locationName;
    private double lat;
    private double lng;
    private GPS_Util gps;

    @Override
    public void onCreate() {
        locationName="";
        lat=0;
        lng=0;
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void settingLocation(Context context){
        try{
            gps=new GPS_Util(context);
            if(gps.isGetLocation()){
                lat=gps.getLatitude();
                lng=gps.getLongitude();


                locationName=new geocoderUtil(context,lat,lng).getAdressName();
                if(locationName==null){
                    locationName="올바르지 않은 이름";
                }
            }else{
                gps.showSettingAlert();
            }



        }catch (Exception e){
            e.printStackTrace();
        }




    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
