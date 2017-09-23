package com.example.heojuyeong.foodandroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.heojuyeong.foodandroid.fragment.CurLocationFragment;
import com.example.heojuyeong.foodandroid.fragment.CurrentOrderFragment;
import com.example.heojuyeong.foodandroid.fragment.HomeFragment;
import com.example.heojuyeong.foodandroid.fragment.MenuFragment;
import com.example.heojuyeong.foodandroid.fragment.SearchFragment;
import com.example.heojuyeong.foodandroid.util.TedPermissionUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private boolean homeFragmentFlag=true;
    FragmentManager fragmentManager=getFragmentManager();
    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();


    Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (view.getId()) {
                case R.id.homeButton:
                    homeFragmentFlag=true;
                    fragmentTransaction.replace(R.id.homeContent, new HomeFragment());
                    fragmentTransaction.commit();
                    break;
                case R.id.currentOrderButton:
                    homeFragmentFlag=false;
                    fragmentTransaction.replace(R.id.homeContent, new CurrentOrderFragment());
                    fragmentTransaction.commit();
                    break;
                case R.id.mapButton:
                    homeFragmentFlag=false;
                    fragmentTransaction.replace(R.id.homeContent, new CurLocationFragment());
                    fragmentTransaction.commit();
                    break;
                case R.id.mainSearchButton:
                    homeFragmentFlag=false;
                    fragmentTransaction.replace(R.id.homeContent, new SearchFragment());
                    fragmentTransaction.commit();
                    break;
                case R.id.menuButton:
                    homeFragmentFlag=false;
                    fragmentTransaction.replace(R.id.homeContent, new MenuFragment());
                    fragmentTransaction.commit();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder().inMemory().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Logger.addLogAdapter(new AndroidLogAdapter());
        ConnectivityManager manager =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()){

        }else{
            Logger.d("networkError", "Network connect fail");
        }


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //permission setting
        TedPermissionUtil tedPermissionUtil=new TedPermissionUtil(getBaseContext());
//
//        CommonLocationApplication commonLocationApplication=(CommonLocationApplication)getApplicationContext();
//        commonLocationApplication.settingLocation(getBaseContext());

        fragmentTransaction.add(R.id.homeContent,new HomeFragment());
        fragmentTransaction.commit();

        Button homeButton=(Button)findViewById(R.id.homeButton);
        Button currentOrderButton=(Button)findViewById(R.id.currentOrderButton);
        Button searchButton=(Button)findViewById(R.id.mainSearchButton);
        Button mapButton=(Button)findViewById(R.id.mapButton);
        Button menuButton=(Button)findViewById(R.id.menuButton);

        homeButton.setOnClickListener(onClickListener);
        currentOrderButton.setOnClickListener(onClickListener);
        searchButton.setOnClickListener(onClickListener);
        mapButton.setOnClickListener(onClickListener);
        menuButton.setOnClickListener(onClickListener);



    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        if (homeFragmentFlag) {
            super.onBackPressed();
        } else {
            homeFragmentFlag=true;
            fragmentTransaction.replace(R.id.homeContent, new HomeFragment());
            fragmentTransaction.commit();
        }
    }


}


