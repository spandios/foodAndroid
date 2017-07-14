package com.example.heojuyeong.foodandroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.heojuyeong.foodandroid.common.CommonLocationApplication;
import com.example.heojuyeong.foodandroid.common.CommonLocationApplication2;
import com.example.heojuyeong.foodandroid.fragment.CurrentOrderFragment;
import com.example.heojuyeong.foodandroid.fragment.HomeFragment;
import com.example.heojuyeong.foodandroid.fragment.MapFragment;
import com.example.heojuyeong.foodandroid.fragment.MenuFragment;
import com.example.heojuyeong.foodandroid.fragment.SearchFragment;
import com.example.heojuyeong.foodandroid.util.TedPermissionUtil;
import com.example.heojuyeong.foodandroid.http.GeocodingThread;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {
    public String locationName;
    public double lat;
    public double lng;


    private boolean homeFragmentFlag=true;
    FragmentManager fragmentManager=getFragmentManager();
    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

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
                    fragmentTransaction.replace(R.id.homeContent, new MapFragment());
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Logger.addLogAdapter(new AndroidLogAdapter());

        // 만약, 개발자가 action bar없이 full screen을 원하는 경우에는 다음을 추가한다
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //permission setting
        TedPermissionUtil tedPermissionHandler=new TedPermissionUtil(this);


        GeocodingThread geocodingThread=new GeocodingThread(this,"37.519211,127.029024");

        //location setting
        CommonLocationApplication2 commonLocationApplication=(CommonLocationApplication2)getApplication();
        commonLocationApplication.settingLocation(getBaseContext());


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


