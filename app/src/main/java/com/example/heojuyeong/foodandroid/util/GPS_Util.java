package com.example.heojuyeong.foodandroid.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class GPS_Util extends Service implements LocationListener {
    private final Context mContext;

    boolean isGPSEnabled=false;
    boolean isNetworkEnabled=false;
    boolean isGetLocation=false;

    Location location;
    public double lat;
    public double lng;

    //최소 GPS정보 업데이트 거리 10미터
    private static final long MIN_DISTNACE_CHANGE_FOR_UPDATES=10;
    //GPS update 간격 5분
    private static final long MIN_TIME_BW_UPDATES=1000*60*5;

    protected LocationManager locationManager;

    public GPS_Util(Context context){
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

            if(!isGPSEnabled&&!isNetworkEnabled){//gps, network불가능하다면
                Toast.makeText(mContext,"현재 gps정보 일기 실패", Toast.LENGTH_SHORT).show();

            }else{
                this.isGetLocation=true;
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTNACE_CHANGE_FOR_UPDATES,this);
                    if(locationManager!=null){
                        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location!=null){
                            lat=location.getLatitude();
                            lng=location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled){
                    if(location==null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTNACE_CHANGE_FOR_UPDATES,this);
                        if(locationManager!=null){
                            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location!=null){
                                lat=location.getLatitude();
                                lng=location.getLongitude();
                            }
                        }
                    }

                }

                return location;

            }


        }catch (SecurityException e){
            e.printStackTrace();
        }
    return location;
    }
    public void stopUsingGPS(){
        if(locationManager!=null){
            locationManager.removeUpdates(GPS_Util.this);
        }
    }

    public double getLatitude(){
        if(location!=null){
            lat=location.getLatitude();
        }
        return lat;
    }
    public double getLongitude(){
        if(location!=null){
            lng=location.getLongitude();
        }
        return lng;
    }

    public String getLatLng(){
        return String.valueOf(lat) + "," + String.valueOf(lng);
    }

    public boolean isGetLocation() {
        return this.isGetLocation;
    }

    public void showSettingAlert(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무 셋팅");
        alertDialog.setMessage("GPS 셋팅 설정 창으로 가시겠습니까");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

