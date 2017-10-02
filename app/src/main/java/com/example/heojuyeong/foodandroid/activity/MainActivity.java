package com.example.heojuyeong.foodandroid.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.fragment.CurLocationFragment;
import com.example.heojuyeong.foodandroid.fragment.CurrentOrderFragment;
import com.example.heojuyeong.foodandroid.fragment.HomeFragment;
import com.example.heojuyeong.foodandroid.fragment.MenuFragment;
import com.example.heojuyeong.foodandroid.fragment.SearchFragment;
import com.example.heojuyeong.foodandroid.model.restaurant.LocationItem;
import com.example.heojuyeong.foodandroid.util.GpsUtil;
import com.example.heojuyeong.foodandroid.util.NetworkUtil;
import com.example.heojuyeong.foodandroid.util.RealmUtil;
import com.example.heojuyeong.foodandroid.util.SettingActivityUtil;
import com.orhanobut.logger.Logger;

import io.realm.RealmResults;

import static com.example.heojuyeong.foodandroid.staticval.StaticVal.gpsSettingActivityRequestCode;

public class MainActivity extends AppCompatActivity {

    private boolean homeFragmentFlag = true;
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


    Button.OnClickListener onClickListener = view -> {
        FragmentManager fragmentManager1 = getFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        switch (view.getId()) {
            case R.id.homeButton:
                homeFragmentFlag = true;
                fragmentTransaction1.replace(R.id.homeContent, new HomeFragment());
                fragmentTransaction1.commit();
                break;
            case R.id.currentOrderButton:
                homeFragmentFlag = false;
                fragmentTransaction1.replace(R.id.homeContent, new CurrentOrderFragment());
                fragmentTransaction1.commit();
                break;
            case R.id.mapButton:
                homeFragmentFlag = false;
                fragmentTransaction1.replace(R.id.homeContent, new CurLocationFragment());
                fragmentTransaction1.commit();
                break;
            case R.id.mainSearchButton:
                homeFragmentFlag = false;
                fragmentTransaction1.replace(R.id.homeContent, new SearchFragment());
                fragmentTransaction1.commit();
                break;
            case R.id.menuButton:
                homeFragmentFlag = false;
                fragmentTransaction1.replace(R.id.homeContent, new MenuFragment());
                fragmentTransaction1.commit();
                break;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        NetworkUtil.CheckandGetLocation(this);
        RealmResults<LocationItem> location = RealmUtil.findDataAll(LocationItem.class);
        if(location.size()>0){
            Logger.d(location.get(0).getLocationName());
        }

//  GPS_Util gps_util=new GPS_Util(this.getApplicationContext());
//        materialDialog=new MaterialDialog.Builder(this).content("GpsUtil 다시 시도").positiveText("예").onPositive(new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                GPS_Util gps_util=new GPS_Util(getApplicationContext());
//                gps_util.getLocation();
//                gps_util.insertDB();
//
//            }
//        }).build();
//
//        //인터넷 연결 체크 및 위치 정보 얻음
//        if(location.size()>1) {
//            Logger.d(location.get(0).getLat());
//            Logger.d(location.get(0).getLocationName());
//            if (location.get(0) == null) {
//                materialDialog.show();
//
//            } else {
//                materialDialog.show();
//            }
//
//        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //permission setting


//

//        commonLocationApplication.settingLocation(getBaseContext());

        fragmentTransaction.add(R.id.homeContent, new HomeFragment());
        fragmentTransaction.commit();

        Button homeButton = (Button) findViewById(R.id.homeButton);
        Button currentOrderButton = (Button) findViewById(R.id.currentOrderButton);
        Button searchButton = (Button) findViewById(R.id.mainSearchButton);
        Button mapButton = (Button) findViewById(R.id.mapButton);
        Button menuButton = (Button) findViewById(R.id.menuButton);

        homeButton.setOnClickListener(onClickListener);
        currentOrderButton.setOnClickListener(onClickListener);
        searchButton.setOnClickListener(onClickListener);
        mapButton.setOnClickListener(onClickListener);
        menuButton.setOnClickListener(onClickListener);


    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (homeFragmentFlag) {
            super.onBackPressed();
        } else {
            homeFragmentFlag = true;
            fragmentTransaction.replace(R.id.homeContent, new HomeFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if(requestCode==gpsSettingActivityRequestCode){
        if(NetworkUtil.isGpsPossible(this)){
            GpsUtil.getGPS(this);
        }else{
            SettingActivityUtil.settingGPS(this);
        }
       }
    }
}


