package com.example.fooddeuk.util.login;

import com.example.fooddeuk.model.user.User;
import com.example.fooddeuk.util.LoginUtil;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.orhanobut.logger.Logger;

/**
 * Created by heojuyeong on 2017. 10. 8..
 */

public class KAKAO {



    public static void kakaoLoginResult() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Logger.d(errorResult.getErrorMessage());

            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("로그인성공");

                LoginUtil.CheckGetRegisterUser(new User(userProfile.getEmail(),String.valueOf(userProfile.getId()), userProfile.getNickname(),"kakao"));
            }

            @Override
            public void onNotSignedUp() {
                com.orhanobut.logger.Logger.d("on not sing up");
                kakaoRequestSignUp();
            }
        });
    }

    public static void kakaoRequestSignUp() {
        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(Long result) {
                Logger.d(result);
                kakaoLoginResult();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                final String message = "UsermgmtResponseCallback : failure : " + errorResult;
                Logger.d(message);

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }
        }, null);
    }




    public static void kakaoLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Logger.d("로그아웃");
            }
        });
    }

    public static void kakaoAccessTokenInfo() {
        AuthService.requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
//                Logger.d(errorResult);
            }

            @Override
            public void onNotSignedUp() {
                // not happened
            }

            @Override
            public void onFailure(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                Logger.d("KAKAO"+ userId);
                LoginUtil.initUser(String.valueOf(userId));


            }
        });
    }

}
