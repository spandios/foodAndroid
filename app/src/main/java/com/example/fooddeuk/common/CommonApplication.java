package com.example.fooddeuk.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
<<<<<<< HEAD:app/src/main/java/com/example/fooddeuk/common/CommonValueApplication.java
import android.location.LocationManager;
import android.support.multidex.MultiDex;

import com.example.fooddeuk.network.HttpService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
=======
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.example.fooddeuk.model.restaurant.LocationItem;
import com.example.fooddeuk.network.HTTP;
import com.example.fooddeuk.util.GPS_Util;
import com.example.fooddeuk.util.GeoUtil;
import com.example.fooddeuk.util.RealmUtil;
import com.google.gson.GsonBuilder;
>>>>>>> origin/master:app/src/main/java/com/example/fooddeuk/common/CommonApplication.java
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.IPushConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

<<<<<<< HEAD:app/src/main/java/com/example/fooddeuk/common/CommonValueApplication.java
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
=======
import io.nlopez.smartlocation.SmartLocation;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
>>>>>>> origin/master:app/src/main/java/com/example/fooddeuk/common/CommonApplication.java
import retrofit2.converter.gson.GsonConverterFactory;


public class CommonApplication extends android.app.Application {
    @SuppressLint("StaticFieldLeak")
<<<<<<< HEAD:app/src/main/java/com/example/fooddeuk/common/CommonValueApplication.java
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
=======
    private static volatile CommonApplication instance = null;
    private static volatile Activity currentActivity = null;
    private static double lat;
    private static double lng;
    private static String locationName;
    private static String rest_id;
    private static HTTP http;
    public static SharedPreferences pref;
>>>>>>> origin/master:app/src/main/java/com/example/fooddeuk/common/CommonApplication.java



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

<<<<<<< HEAD:app/src/main/java/com/example/fooddeuk/common/CommonValueApplication.java
    public static CommonValueApplication getInstance() {
=======
    public static double getLat() {
        return lat;
    }

    public static double getLng() {
        return lng;
    }

    public static String getLocationName() {
        return locationName;
    }

    public static String getRest_id() {
        return rest_id;
    }


    public static HTTP getHttp(){return http;}

    public static CommonApplication getInstance() {
>>>>>>> origin/master:app/src/main/java/com/example/fooddeuk/common/CommonApplication.java
        return instance;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setInstance(CommonValueApplication instance) {
        CommonValueApplication.instance = instance;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        CommonApplication.currentActivity = currentActivity;
    }

    public static void setRest_id(String rest_id){
        CommonApplication.rest_id=rest_id;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        instance=this;
<<<<<<< HEAD:app/src/main/java/com/example/fooddeuk/common/CommonValueApplication.java
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



=======
        initHttp();
        pref=this.getSharedPreferences("LoginPref",Activity.MODE_PRIVATE);

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Logger.addLogAdapter(new AndroidLogAdapter());
        KakaoSDK.init(new KakaoSDKAdapter());
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


>>>>>>> origin/master:app/src/main/java/com/example/fooddeuk/common/CommonApplication.java
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    private void initHttp() {
        GsonBuilder gson=new GsonBuilder().setLenient();
        HttpLoggingInterceptor logging=new HttpLoggingInterceptor();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        http= new retrofit2.Retrofit.Builder()
                //AWS
//                .baseUrl("http://13.124.159.166")
                .baseUrl("http://10.0.2.2:3000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .build().create(HTTP.class);
    }


}
