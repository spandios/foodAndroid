package com.example.heojuyeong.foodandroid.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.orhanobut.logger.Logger;

import java.util.Locale;

/**
 * Created by heojuyeong on 2017. 9. 23..
 */

//위치->주소명
public class GeoCoding {
    private static GeoCoding geoCoding;
    private static Geocoder geocoder;

    private GeoCoding(Context context){
        geocoder=new Geocoder(context, Locale.KOREA);
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
         try{
             //좌표->주소
             Address addresses;
             addresses=geocoder.getFromLocation(lat,lng,1).get(0);
             //구 없으면 시
             String subLocality=(addresses.getSubLocality()!=null)? addresses.getSubLocality():addresses.getLocality();
             //동 없으면 리?
             String thoroughFare=(addresses.getThoroughfare()!=null)? addresses.getThoroughfare():addresses.getSubThoroughfare();


             if(subLocality!=null&thoroughFare!=null){

                 return subLocality+" "+thoroughFare;

             }else{
                    Logger.d(addresses.getAddressLine(0));
                 return addresses.getAddressLine(0).substring(12);
             }





         }catch (Exception e){
            Logger.d(e);

            return "지역을 읽을수 없습니다.";
         }

    }





}
