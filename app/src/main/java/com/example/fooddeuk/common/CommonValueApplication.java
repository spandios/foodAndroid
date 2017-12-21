package com.example.fooddeuk.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.example.fooddeuk.model.restaurant.LocationItem;
import com.example.fooddeuk.util.GPS_Util;
import com.example.fooddeuk.util.GeoUtil;
import com.example.fooddeuk.util.RealmUtil;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.IPushConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tsengvn.typekit.Typekit;

import io.fabric.sdk.android.Fabric;
import io.nlopez.smartlocation.SmartLocation;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class CommonValueApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static volatile CommonValueApplication instance = null;
    private static volatile Activity currentActivity = null;
    public static double lat;
    public static double lng;
    public static String locationName;



    private static class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public ISessionConfig getSessionConfig() {
            return super.getSessionConfig();
        }

        @Override
        public IPushConfig getPushConfig() {
            return super.getPushConfig();
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return getInstance();
                }
            };
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


    public static CommonValueApplication getInstance() {
        return instance;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        CommonValueApplication.currentActivity = currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance=this;

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Logger.addLogAdapter(new AndroidLogAdapter());

        KakaoSDK.init(new KakaoSDKAdapter());
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/Spoqa Han Sans Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/Spoqa Han Sans Bold.ttf"))
                .add("Light", Typekit.createFromAsset(this,"fonts/Spoqa Han Sans Light.ttf"))
                .add("Thin", Typekit.createFromAsset(this,"fonts/Spoqa Han Sans Thin.ttf"));

        SmartLocation.with(this).location()
                .oneFix()
                .start(location -> {
                            lat = location.getLatitude();

                            lng = location.getLongitude();

                            locationName = GeoUtil.getInstance(this).getLocationName(lat, lng);
                            if(lat==0.0||lng==0.0||locationName==null){
                                Logger.d("GPS recall");
                                GPS_Util gps_util=new GPS_Util(this);
                                lat=gps_util.getLatitude();
                                lng=gps_util.getLongitude();
                                locationName=GeoUtil.getInstance(this).getLocationName(lat, lng);
                                LocationItem locationItem = new LocationItem(locationName, lat, lng);
                                RealmUtil.insertData(locationItem);
                                return;
                            }else{
                                LocationItem locationItem = new LocationItem(locationName, lat, lng);
                                RealmUtil.insertData(locationItem);
                            }

                        }
                );
    }







    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }


}
