package com.example.fooddeuk.util;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 11..
 */

public class TedPermissionUtil {

    public TedPermissionUtil(final Context context){
        PermissionListener permissionListener=new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                GPS gps=new GPS(context);
                gps.getGPS();

            }


            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "권한거부: \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE)
                .check();


    }


}




