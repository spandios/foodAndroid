package com.example.fooddeuk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.fooddeuk.R;
import com.example.fooddeuk.util.login.NAVER;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;


public class NaverLoginActivity extends AppCompatActivity {
    OAuthLoginButton mOAuthLoginButton;
    OAuthLogin mOAuthLoginModule;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_login);
        mContext = this;
        mOAuthLoginModule = NAVER.getNaverLoginModule(this);
        Button logoutButton = (Button) findViewById(R.id.naverlogiut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginModule.logout(mContext);
            }
        });
        OAuthLoginButton naverLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        naverLoginButton.setBgResourceId(R.drawable.img_loginbtn_usercustom);
        naverLoginButton.setOAuthLoginHandler(NAVER.getNaverLoginHandler(this,mOAuthLoginModule));


    }
}
