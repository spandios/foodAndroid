package com.example.fooddeuk.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.multidex.MultiDex;

import com.example.fooddeuk.network.HttpService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.IPushConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class CommonValueApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static volatile CommonValueApplication instance;
    @SuppressLint("StaticFieldLeak")
    private static volatile Activity currentActivity;
    public static double lat;
    public static double lng;
    public static GoogleApiClient googleApiClient;
    public static String locationName;
    public static String rest_id;
    public static HttpService httpService;
    public static LocationManager locationManager;
    public static String fcmToken;
    public static SharedPreferences pref;
    



    private class KakaoSDKAdapter extends KakaoAdapter {

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

    public static void setRest_id(String rest_id){
        CommonValueApplication.rest_id=rest_id;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        instance=this;
        initHTTP();
        initRealm();
        Logger.addLogAdapter(new AndroidLogAdapter());
        KakaoSDK.init(new KakaoSDKAdapter());
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        fcmToken= FirebaseInstanceId.getInstance().getToken();



//        SmartLocation.with(this).location()
//                .oneFix()
//                .start(location -> {
//                            lat = location.getLatitude();
//
//                            lng = location.getLongitude();
//                            locationName = GeoUtil.getInstance(this).getLocationName(lat, lng);
//                            if(lat==0.0||lng==0.0||locationName==null){
//                                Logger.d("GPS recall");
//                                GPS_Util gps_util=new GPS_Util(this);
//                                lat=gps_util.getLatitude();
//                                lng=gps_util.getLongitude();
//                                locationName=GeoUtil.getInstance(this).getLocationName(lat, lng);
//                                LocationItem locationItem = new LocationItem(locationName, lat, lng);
//                                RealmUtil.insertData(locationItem);
//                                return;
//                            }else{
//                                LocationItem locationItem = new LocationItem(locationName, lat, lng);
//                                RealmUtil.insertData(locationItem);
//                            }
//
//                        }
//                );
    }




    private void initHTTP(){
        Retrofit retrofit = new Retrofit.Builder()
                //                .baseUrl("http://13.124.159.166")
                .baseUrl("http://10.0.2.2:3000")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }


}
