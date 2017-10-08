package com.example.fooddeuk.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.KakaoLoginActivity;
import com.example.fooddeuk.activity.NaverLoginActivity;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.LoginUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;

public class HomeFragment extends Fragment {
    public LoginButton loginButton;
    public CallbackManager callbackManager;
    public ProfileTracker profileTracker;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setFacebookLogin(view);

        Button button=(Button)view.findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(getActivity(), KakaoLoginActivity.class);
            }
        });
        Button button2=(Button)view.findViewById(R.id.naverlogin);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(getActivity(), NaverLoginActivity.class);
            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccessToken.getCurrentAccessToken() == null) {
//            Logger.d("Not Login");
        } else {
//            Logger.d("Login");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void setFacebookLogin(View view){
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        // If using in a fragment
        callbackManager = CallbackManager.Factory.create();
        LoginUtil.FaceBookLogin(loginButton,callbackManager);
    }


}
