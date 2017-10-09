package com.example.fooddeuk.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.fooddeuk.R;
import com.example.fooddeuk.fragment.CurLocationRestaurantFragment;
import com.example.fooddeuk.fragment.CurrentOrderFragment;
import com.example.fooddeuk.fragment.HomeFragment;
import com.example.fooddeuk.fragment.MenuFragment;
import com.example.fooddeuk.fragment.SearchFragment;
import com.example.fooddeuk.util.GPS;
import com.example.fooddeuk.util.NetworkUtil;
import com.example.fooddeuk.util.SettingActivityUtil;
import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.nhn.android.naverlogin.OAuthLogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.fooddeuk.staticval.StaticVal.gpsSettingActivityRequestCode;

public class MainActivity extends BaseActivity {
    private boolean homeFragmentFlag = true;
    public CallbackManager callbackManager;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ProfileTracker profileTracker;
    OAuthLogin naverLoginModule;





    @BindView(R.id.homeButton)
    Button homeButton;
    @BindView(R.id.currentOrderButton)
    Button currentOrderButton;
    @BindView(R.id.mainSearchButton)
    Button searchButton;
    @BindView(R.id.mapButton)
    Button mapButton;
    @BindView(R.id.menuButton)
    Button menuButton;

    @OnClick({R.id.homeButton, R.id.currentOrderButton, R.id.mapButton, R.id.mainSearchButton, R.id.menuButton})
    public void click(Button view) {

        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.homeButton:
                homeFragmentFlag = true;
                fragmentTransaction.replace(R.id.homeContent, new HomeFragment());

                break;
            case R.id.currentOrderButton:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new CurrentOrderFragment());

                break;
            case R.id.mapButton:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new CurLocationRestaurantFragment());


//                new Handler().postDelayed(() -> {
//
//                }, 200);// 0.5초 정도 딜레이를 준 후 시작
                break;
            case R.id.mainSearchButton:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new SearchFragment());

                break;
            case R.id.menuButton:
                homeFragmentFlag = false;
                fragmentTransaction.replace(R.id.homeContent, new MenuFragment());

                break;
        }
        fragmentTransaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.homeContent, new CurLocationRestaurantFragment());
        fragmentTransaction.commit();

//        RealmResults<UserItemRealm> userItemRealms= RealmUtil.findDataAll(UserItemRealm.class);
//        Logger.d(userItemRealms.get(0).email);


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


        if (requestCode == gpsSettingActivityRequestCode) {
            if (NetworkUtil.isGpsPossible(this)) {
                new GPS(this).getGPS();

            } else {
                SettingActivityUtil.settingGPS(this);
            }
        }
    }



}


