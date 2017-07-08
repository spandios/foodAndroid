package com.example.heojuyeong.foodandroid.handler;

import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;



public class GPS_Handler extends Service implements LocationListener {
    private final Context mContext;

    boolean isGPSEnabled=false;
    boolean isNetworkEnabled=false;
    boolean isGetLocation=false;

    Location location;
    double lat;
    double lng;

    //최소 GPS정보 업데이트 거리 10미터
    private static final long MIN_DISTNACE_CHANGE_FOR_UPDATES=10;

    protected LocationManager locationManager;

    public GPS_Handler(Context context){
        this.mContext=context;
        getLocation();
    }

    public Location getLocation(){
        try{
            //locationManger객체설정
            locationManager=(LocationManager)mContext.getSystemService(LOCATION_SERVICE);

            //gps가능한지
            isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //network가능한지
            isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        }
    }

}
