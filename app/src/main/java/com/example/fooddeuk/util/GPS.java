package com.example.fooddeuk.util;

import android.content.Context;

import com.example.fooddeuk.model.restaurant.LocationItem;

import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by heojuyeong on 2017. 10. 3..
 */

public class GPS {
    final private Context context;

    public GPS(Context context) {
        this.context = context;
    }

    public void getGPS() {
        SmartLocation.with(context).location()
                .oneFix()
                .start(location -> {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();
                            String locationName = GeoUtil.getInstance(context).getLocationName(location.getLatitude(), location.getLongitude());
                            if(lat==0.0||lng==0.0||locationName==null){
                                GPS_Util gps_util=new GPS_Util(context);
                                return;
                            }
                            LocationItem locationItem = new LocationItem(locationName, lat, lng);
                            RealmUtil.insertData(locationItem);
                        }
                );
    }
}
