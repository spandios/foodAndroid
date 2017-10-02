package com.example.heojuyeong.foodandroid.util;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by heojuyeong on 2017. 10. 1..
 */

public class NetworkUtil {

    public static void CheckandGetLocation(Activity activity){
        if (NetworkUtil.isNetworkConnected(activity)) {
            //위치기능 비활성화한다면
            if(!NetworkUtil.isGpsPossible(activity)){
                SettingActivityUtil.settingGPS(activity);
            }

            GPS_Util gps_util = new GPS_Util(activity.getApplicationContext());
        } else {
            SettingActivityUtil.notPossibleNetwork(activity);
        }
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static boolean isGpsPossible(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        //GpsUtil, network불가능하다면
        return !(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }
}
