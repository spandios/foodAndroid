package com.example.heojuyeong.foodandroid.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.heojuyeong.foodandroid.util.GPS;
import com.example.heojuyeong.foodandroid.util.NetworkUtil;
import com.example.heojuyeong.foodandroid.util.SettingActivityUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.heojuyeong.foodandroid.staticval.StaticVal.gpsSettingActivityRequestCode;

public class MainActivity extends AppCompatActivity {
    private boolean homeFragmentFlag = true;
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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

    @OnClick({R.id.homeButton,R.id.currentOrderButton,R.id.mapButton,R.id.mainSearchButton,R.id.menuButton})
    public void click(Button view){
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
                new Handler().postDelayed(() -> {
                    fragmentTransaction1.replace(R.id.homeContent, new CurLocationFragment());
                    fragmentTransaction1.commit();
                }, 200);// 0.5초 정도 딜레이를 준 후 시작
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
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        NetworkUtil.CheckNetGps(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        Logger.d(token);
        fragmentTransaction.add(R.id.homeContent, new CurLocationFragment());
        new Handler().postDelayed(() -> {
            fragmentTransaction.commit();
        }, 3000);


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


