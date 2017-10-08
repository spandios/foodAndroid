package com.example.fooddeuk.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.fooddeuk.util.GPS;
import com.example.fooddeuk.util.TedPermissionUtil;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.IPushConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class CommonValueApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static volatile CommonValueApplication instance = null;
    private static volatile Activity currentActivity = null;


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
        instance=this;
        new TedPermissionUtil(this);
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Logger.addLogAdapter(new AndroidLogAdapter());
        new GPS(this).getGPS();
        KakaoSDK.init(new KakaoSDKAdapter());


    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }


}
