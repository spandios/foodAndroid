package com.example.fooddeuk.object;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.support.multidex.MultiDex;
import com.example.fooddeuk.user.KakaoSDKAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.iwedding.app.helper.RecentPref;
import com.iwedding.app.helper.UserPrefUtil;
import com.kakao.auth.KakaoSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import net.danlew.android.joda.JodaTimeAndroid;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class GlobalApplication extends Application {

  public static final String AWSURL = "http://13.124.159.166";
  public static final String LOCALHOST = "http://10.0.2.2:3000";
  public static double lat;
  public static double lng;
  public static LocationManager locationManager;
  public static String fcmToken;
  @SuppressLint("StaticFieldLeak")
  private static volatile GlobalApplication instance;
  @SuppressLint("StaticFieldLeak")
  private static volatile Activity currentActivity;
  public Retrofit retrofit;
  public Realm realm;
  public Realm inRealm;

  public static GlobalApplication getInstance() {
    return instance;
  }

  public static Activity getCurrentActivity() {
    return currentActivity;
  }

  public static void setCurrentActivity(Activity currentActivity) {
    GlobalApplication.currentActivity = currentActivity;
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override
  public void onCreate() {
    super.onCreate();

//        Fabric.with(this, new Crashlytics());
    instance = this;
    FirebaseApp.initializeApp(this);
    fcmToken = FirebaseInstanceId.getInstance().getToken();
    initHTTP();
    initRealm();
//        LeakCanary.install(this);
    Logger.addLogAdapter(new AndroidLogAdapter());
    KakaoSDK.init(new KakaoSDKAdapter());
    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    UserPrefUtil.Companion.setUserPref(this, "user");
    RecentPref.Companion.setRecentPref(this, "recent");
    JodaTimeAndroid.init(this);

  }


  private void initHTTP() {
    retrofit = new Retrofit.Builder()
        .baseUrl(LOCALHOST)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

  }

  private void initRealm() {
    Realm.init(this);
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("memory")
        .inMemory().build();
    RealmConfiguration inRealmConfiguration = new RealmConfiguration.Builder().name("In").build();
    realm = Realm.getInstance(realmConfiguration);
    inRealm = Realm.getInstance(inRealmConfiguration);
  }


  @Override
  public void onTerminate() {
    super.onTerminate();
    instance = null;
  }


}
