package com.example.heojuyeong.foodandroid.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Locale;


public class geocoderUtil {
    private Geocoder geocoder;
    private String adressName;

    public geocoderUtil(Context context, double lat, double lng) {
        geocoder = new Geocoder(context, Locale.KOREA);
        try {
            StringBuilder sb = new StringBuilder();
            Address tempAdress = geocoder.getFromLocation(lat, lng, 1).get(0);

            if (tempAdress != null) {
                sb.append(tempAdress.getLocality());
                sb.append(" ");
                sb.append(tempAdress.getThoroughfare());
                this.adressName = sb.toString();
            }else{
                Logger.d("adress fail");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getAdressName() {
        return this.adressName;
    }


}
