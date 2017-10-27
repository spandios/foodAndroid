package com.example.fooddeuk.util;

import android.content.Context;

import com.example.fooddeuk.model.restaurant.LocationItem;
import com.orhanobut.logger.Logger;

import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by heojuyeong on 2017. 10. 3..
 */

public class GPS {
    private double lat;
    private double lng;
    private String locationName;
    final private Context context;

    public GPS(Context context) {
        this.context = context;
    }

    public void getGPS() {
        SmartLocation.with(context).location()
                .oneFix()
                .start(location -> {
                            lat = location.getLatitude();

                            lng = location.getLongitude();

                            locationName = GeoUtil.getInstance(context).getLocationName(lat, lng);
                            if(lat==0.0||lng==0.0||locationName==null){
                                Logger.d("GPS recall");
                                GPS_Util gps_util=new GPS_Util(context);
                                lat=gps_util.getLatitude();
                                lng=gps_util.getLongitude();
                                locationName=GeoUtil.getInstance(context).getLocationName(lat, lng);
                                LocationItem locationItem = new LocationItem(locationName, lat, lng);
                                RealmUtil.insertData(locationItem);
                                return;
                            }else{
                                LocationItem locationItem = new LocationItem(locationName, lat, lng);
                                Logger.d(locationItem);
                                RealmUtil.insertData(locationItem);
                            }

                        }
                );
    }
}
