package com.example.fooddeuk.util;

import android.os.Bundle;
import android.util.Log;

import com.example.fooddeuk.http.UserService;
import com.example.fooddeuk.model.user.UserItem;
import com.example.fooddeuk.model.user.UserItemRealm;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heojuyeong on 2017. 10. 6..
 */

public class LoginUtil {
    /**
     * 로그인 되있는지 안 되어있는지 , check할수있음
     **/


    public static void initUser(String provider_id){
        String fcm_token = FirebaseInstanceId.getInstance().getToken();
        getUser(provider_id, (err, userItem) -> {
            if (err != null) err.printStackTrace();

                //기존에 등록되있는 회원이라면  //local DB
                if (userItem.exist) {
                    UserItem.User user=userItem.user;

                    if (!userItem.user.fcm_token.equals(fcm_token)) {
                        updateUserFcmToken(userItem.user.provider_id, fcm_token);
                        insertUserRealm(user,fcm_token);
                    }

                    insertUserRealm(user);
                    Logger.d(user.provider+"기존회원 : "+user.email);
                }else{
                    //Login Button 온
                    Logger.d("기존유저가 아님");
                }


        });

    }

    public static void CheckGetRegisterUser(String email, String name, String provider_id,String provider){
        try {
            String fcm_token = FirebaseInstanceId.getInstance().getToken();
            /**등록된 회원인지 체크 후 없으면 등록
             *param provider_id
             * result boolean
             * **/

            getUser(provider_id, (err, userItem) -> {
                if (userItem == null) {
                    Logger.d(err);
                    err.printStackTrace();
                } else {
                    //기존에 등록되있는 회원이라면  //local DB
                    if (userItem.exist) {
                        UserItem.User user=userItem.user;
                        if (!userItem.user.fcm_token.equals(fcm_token)) {
                            updateUserFcmToken(userItem.user.provider_id, fcm_token);
                            insertUserRealm(user,fcm_token);
                        }
                        insertUserRealm(user);
                        Logger.d("기존 회원입니다.");
                    }
                    //신규유저라면
                    else {
                        UserService.createUser(provider_id, email, name, provider, fcm_token).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response1) {
                                Logger.d("새로운 유저 등록");
                                insertUserRealm(provider_id);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Logger.d(t);
                            }
                        });
                    }

                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }








    public static void FaceBookLogin(LoginButton loginButton, CallbackManager callbackManager) {
        //     on destroy에 추가 profileTracker.stopTracking();


//    profileTracker = new ProfileTracker() {
//        @Override
//        protected void onCurrentProfileChanged(
//                Profile oldProfile,
//                Profile currentProfile) {
//            //if logout
//            if (currentProfile == null) {
//                Logger.d("LOGOUT");
//                commonValueApplication.provider="";
//            }else{
//                Logger.d(currentProfile.getId());
//            }
//
//        }
//    };
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            try{
                                CheckGetRegisterUser(object.getString("email"),object.getString("name"),loginResult.getAccessToken().getUserId(),"facebook");
                            }catch (Exception e){
                                Logger.d(e);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("Home", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("HOME LOGIN", exception.getCause().toString());
            }

        });
    }


    public static void getUser(String provider_id, GetUserCallBack getUserCallBack) {
        UserService.getUser(provider_id).enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                if (response.isSuccessful()) {
                    getUserCallBack.userCallback(null, response.body());
                }
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                getUserCallBack.userCallback(t, null);
            }
        });

    }

    public static void existUser(String provider_id,ExistUserCallback existUserCallback){
        UserService.existUser(provider_id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    existUserCallback.existCallback(null,response.body());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
                existUserCallback.existCallback(t,null);
            }
        });
    }




    public static void insertUserRealm(UserItem.User item) {
        RealmUtil.insertData(new UserItemRealm(item.user_id, item.email, item.provider_id, item.user_name, item.fcm_token,item.provider));
        Logger.d("유저 정보 db에 저장 성공 ");
    }
    public static void insertUserRealm(UserItem.User item,String fcm_token) {
        Logger.d("유저 정보 db에 저장 성공 , token update ");
        RealmUtil.insertData(new UserItemRealm(item.user_id, item.email, item.provider_id, item.user_name, fcm_token,item.provider));

    }

    public static void insertUserRealm(String provider_id) {
        UserService.getUser(provider_id).enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                if (response.isSuccessful()) {
                    UserItem.User item = response.body().user;
                    insertUserRealm(item);
                }
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



    public static void updateUserFcmToken(String provider_id, String fcm_token) {
        Logger.d("fcm_token update!!");
        UserService.updateToken(provider_id, fcm_token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface GetUserCallBack {
        void userCallback(Throwable err, UserItem user);
    }
    public interface ExistUserCallback{
        void existCallback(Throwable err,Boolean isExist);
    }

}
