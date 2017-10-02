package com.example.heojuyeong.foodandroid.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.example.heojuyeong.foodandroid.model.restaurant.LocationItem;
import com.orhanobut.logger.Logger;

/**
 * Created by heojuyeong on 2017. 10. 2..
 */

public class GpsUtil {

    public static void getGPS(Context context){

        Location location;
        try{
            LocationManager locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            boolean gpsProvider=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkProvider=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!gpsProvider&&!networkProvider){

            }else{
                if(networkProvider){
                    location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location!=null){
                        double lat=location.getLatitude();
                        double lng=location.getLongitude();
                        if(lat!=0&&lng!=0){
                            String locationName=GeoCoding.getInstance(context).getLocationName(lat,lng);
                            if(locationName!=null){
                                LocationItem locationItem=new LocationItem(locationName,lat,lng);
                                Logger.d(locationItem.getLat());
                                RealmUtil.insertData(locationItem);
                            }else{
                                Logger.d("Location Name Error");
                            }
                        }else{
                            Logger.d("LAT LNG Error");
                        }
                    }else{
                        Logger.d("GET Location Provider ERRor");
                        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    return;
                }

                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location!=null){
                    double lat=location.getLatitude();
                    double lng=location.getLongitude();
                    if(lat!=0&&lng!=0){
                        String locationName=GeoCoding.getInstance(context).getLocationName(lat,lng);
                        if(locationName!=null){
                            LocationItem locationItem=new LocationItem(locationName,lat,lng);
                            RealmUtil.insertData(locationItem);
                        }else{
                            Logger.d("Location Name Error");
                        }
                    }else{
                        Logger.d("LAT LNG Error");
                    }
                }else{
                    Logger.d("GET Location Provider ERRor");
                    location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }
}
