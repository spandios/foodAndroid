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
                //권한체크 된 후 위치 가져오기
                GPS_Util gps_util=new GPS_Util(context);
                gps_util.insertDB();
//                final CommonLocationApplication commonLocationApplication=(CommonLocationApplication)context.getApplicationContext();
//                commonLocationApplication.setLatLng(gps_util.getLatitude(),gps_util.getLongitude());
//                new AsyncTask<String, Void, String>() {
//
//                    @Override
//                    protected String doInBackground(String... params) {
//                       Call<GeocodingService> call=GeocodingService.Geolcation.getCall(params[0]);
//                        try {
//                                return call.execute().body().getResults().get(3).formatted_address.substring(11);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        commonLocationApplication.setLocationName(s);
//                        super.onPostExecute(s);
//
//
//                    }
//                }.execute(gps_util.getLatLng());
            }


            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "권한거부: \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(context)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE)
                .setDeniedMessage("권한을 거부하시면 서비스를 정상적으로 이용하실수 없습니다.")
                .setGotoSettingButton(true)
                .setGotoSettingButtonText("권한 설정하기")
                .check();
    }


}




