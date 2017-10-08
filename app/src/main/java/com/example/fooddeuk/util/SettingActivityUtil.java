package com.example.fooddeuk.util;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

import com.afollestad.materialdialogs.MaterialDialog;

import static com.example.fooddeuk.staticval.StaticVal.gpsSettingActivityRequestCode;

/**
 * Created by heojuyeong on 2017. 10. 1..
 */

public class SettingActivityUtil {

    public static void settingGPS(Activity activity){
        new MaterialDialog.
                Builder(activity)
                .content("위치정보를 활성화하셔야 서비스를 이용하실 수 있습니다. 설정 창으로 가시겠습니까?")
                .positiveText("예")
                //GPS 설정창 이동
                .onPositive((dialog, which) -> activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),gpsSettingActivityRequestCode))
                .negativeText("아니요(종료)")
                .onNegative((dialog, which) -> {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                })
                .build().show();
    }

    public static void notPossibleNetwork(Activity activity){
        new MaterialDialog.Builder(activity).content("인터넷이 연결되어있지않습니다 확인 후 실행해주세요").positiveText("확인").dismissListener(dialog -> {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }).onPositive((dialog, which) -> {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }).build().show();
    }





}
