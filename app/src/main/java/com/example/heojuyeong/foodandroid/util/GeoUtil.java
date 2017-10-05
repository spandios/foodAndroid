package com.example.heojuyeong.foodandroid.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by heojuyeong on 2017. 9. 23..
 */

//위치->주소명
public class GeoUtil {
    private static GeoUtil geoUtil;
    private static Geocoder geocoder;

    private GeoUtil(Context context){
        geocoder=new Geocoder(context, Locale.KOREA);
    }

    public static GeoUtil getInstance(Context context){
        if(geocoder==null){
            geoUtil =new GeoUtil(context);
            return geoUtil;
        }else{
            return geoUtil;
        }
    }
    public LatLng getLocation(String locationName){
        try{
            List<Address> addressList=geocoder.getFromLocationName(locationName,1);
            LatLng latLng=new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
            return latLng;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public String getTempLocationName(double lat, double lng){
        try{
            //좌표->주소
            Address addresses;
            addresses=geocoder.getFromLocation(lat,lng,1).get(0);
            return addresses.getAddressLine(0);

        }catch (Exception e){
            Logger.d(e);

        }
        return null;
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


         }
         return null;

    }





}
