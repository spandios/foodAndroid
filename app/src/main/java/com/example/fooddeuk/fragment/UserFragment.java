package com.example.fooddeuk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class UserFragment extends Fragment {
    public LoginButton loginButton;
    public CallbackManager callbackManager;
    public ProfileTracker profileTracker;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        setFacebookLogin(view);

        Button button= view.findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(getActivity(), KakaoLoginActivity.class);
            }
        });
        Button button2= view.findViewById(R.id.naverlogin);
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
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        // If using in a fragment
        callbackManager = CallbackManager.Factory.create();
        LoginUtil.INSTANCE.FaceBookLogin(loginButton,callbackManager);
    }

}
