package com.example.fooddeuk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.util.LoginUtil;
import com.example.fooddeuk.util.NetworkUtil;
import com.example.fooddeuk.util.login.KAKAO;
import com.example.fooddeuk.util.login.NAVER;
import com.facebook.AccessToken;
import com.nhn.android.naverlogin.OAuthLogin;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class IntroActivity extends BaseActivity{

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView introImageView = (ImageView) findViewById(R.id.introImageView);
        Picasso.with(this)
                .load(R.drawable.fm)
                .into(introImageView);
        init();
        handler.postDelayed(runnable, 500);

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }

    public void init() {
        NetworkUtil.CheckNetGps(this);
        handler = new Handler();
        //facebook
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginUtil.initUser(AccessToken.getCurrentAccessToken().getUserId());
            return;
        }
        //KAKAO
        KAKAO.kakaoAccessTokenInfo();

        //NAVER
        OAuthLogin naverLoginModule= NAVER.getNaverLoginModule(this);
        if(naverLoginModule.getState(this).toString().equals("OK")){
            new Thread(() -> {
                String response = naverLoginModule.requestApi(this, naverLoginModule.getAccessToken(this), "https://openapi.naver.com/v1/nid/me");
                try {
                    JSONObject jsonResult = new JSONObject(response).getJSONObject("response");
                    String provider_id = jsonResult.getString("enc_id");
                    LoginUtil.initUser(provider_id);
                    // 액티비티 이동 등 원하는 함수 호출
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).start();
        }



    }

    Runnable runnable = () -> {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    };
}



