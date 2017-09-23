package com.example.heojuyeong.foodandroid.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by heojuyeong on 2017. 9. 23..
 */

public class GeoCoding {
    private static GeoCoding geoCoding;
    private static Geocoder geocoder;

    private GeoCoding(Context context){
        geocoder=new Geocoder(context);
    }

    public static GeoCoding getInstance(Context context){
        if(geocoder==null){
            geoCoding=new GeoCoding(context);
            return geoCoding;
        }else{
            return geoCoding;
        }
    }

    public Geocoder getGeocoder(){
        return geocoder;
    }

    public String getLocationName(double lat,double lng){

        List<Address> addresses;
         try{
             addresses=geocoder.getFromLocation(lat,lng,1);
             return addresses.get(0).getAddressLine(0);

         }catch (Exception e){
            Logger.d(e);
            return null;
         }

    }





}
