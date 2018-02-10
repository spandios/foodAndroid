package com.example.fooddeuk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.fooddeuk.R;
import com.example.fooddeuk.fragment.CurLocationRestaurantFragment;
import com.example.fooddeuk.fragment.DanGolFragment;
import com.example.fooddeuk.fragment.HomeFragment;
import com.example.fooddeuk.fragment.OrderHistoryFragment;
import com.example.fooddeuk.fragment.UserFragment;
import com.example.fooddeuk.staticval.StaticVal;
import com.example.fooddeuk.util.GPS;
import com.example.fooddeuk.util.NetworkUtil;
import com.example.fooddeuk.util.SettingActivityUtil;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.kakao.util.helper.log.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private boolean homeFragmentFlag = true;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    @BindView(R.id.homeButton)
    Button homeButton;
    @BindView(R.id.currentOrderButton)
    Button currentOrderButton;
    @BindView(R.id.main_dangol)
    Button dangolButton;
    @BindView(R.id.mapButton)
    Button mapButton;
    @BindView(R.id.menuButton)
    Button menuButton;

    @OnClick({R.id.homeButton, R.id.currentOrderButton, R.id.mapButton, R.id.main_dangol, R.id.menuButton})
    public void click(Button view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.homeButton:
                homeFragmentFlag = true;
                fragmentTransaction.replace(R.id.homeContent, new HomeFragment());

                break;

            case R.id.main_dangol:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new DanGolFragment());
                break;
            case R.id.mapButton:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new CurLocationRestaurantFragment());
                break;
            case R.id.currentOrderButton:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new OrderHistoryFragment());
                break;
            case R.id.menuButton:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new UserFragment());
                break;
        }
        fragmentTransaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        Logger.d("lat : "+ getLat() +" lng : "+ getLng());
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.homeContent, new HomeFragment());
        fragmentTransaction.commit();
        LocationCallback locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
//        GPS_Util gps_util = new GPS_Util(getApplicationContext());

//        RealmResults<UserItemRealm> userItemRealms= RealmUtil.findDataAll(UserItemRealm.class);
//        Logger.d(userItemRealms.get(0).email);


    }

    @Override
    public void onBackPressed() {



        if (homeFragmentFlag) {
            super.onBackPressed();
        } else {
            homeFragmentFlag = true;
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.homeContent, new HomeFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().getBoolean("isOrder")){
                Logger.d("e");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homeContent,new OrderHistoryFragment());
                fragmentTransaction.commit();
                homeFragmentFlag=false;

            }
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == StaticVal.INSTANCE.getGpsSettingActivityRequestCode()) {
            if (NetworkUtil.isGpsPossible(this)) {
                new GPS(this).getGPS();

            } else {
                SettingActivityUtil.settingGPS(this);
            }
        }
    }





}


