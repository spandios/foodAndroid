package com.example.heojuyeong.foodandroid.util;

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
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "위치정보 권한이 거부되었습니다 \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(context)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }
    }




