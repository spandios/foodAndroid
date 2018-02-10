package com.example.fooddeuk.util

import android.os.Bundle
import android.util.Log
import com.example.fooddeuk.common.CommonValueApplication.fcmToken
import com.example.fooddeuk.model.user.User
import com.example.fooddeuk.model.user.UserResponse
import com.example.fooddeuk.network.HTTP
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.orhanobut.logger.Logger

/**
 * Created by heojuyeong on 2017. 10. 6..
 */

object LoginUtil {
    /**
     * 로그인 되있는지 안 되어있는지 , check할수있음
     */
//    { err, userResponse ->
//        if (err != null) {
//            err!!.printStackTrace()
//        } else if (userResponse != null) {
//            //기존에 등록되있는 회원이라면  //local DB
//            if (userResponse!!.exist) {
//                val user = userResponse!!.user
//                if (userResponse!!.user.fcm_token != fcm_token) {
//                    updateFcmTokenAndRealmInsert(userResponse!!.user, fcm_token)
//                }
//                insertUser(user)
//                Logger.d(user.provider + "기존회원 : " + user.email)
//            } else {
//                //Login Button 온
//                Logger.d("기존유저가 아님")
//            }
//        }
//
//    })
    fun initUser(provider_id: String) {
        getUser(provider_id, {err, userResponse ->
            userResponse?.let {
                if (userResponse.exist) {
                    val user = userResponse.user
                    if (userResponse.user.fcm_token != fcmToken) {
                        updateFcmTokenAndRealmInsert(userResponse.user, fcmToken)
                    }
                    insertUser(user)
                    Logger.d(user.provider + "기존회원 : " + user.email)
                } else {
                    //Login Button 온
                    Logger.d("기존유저가 아님")
                }
            } ?: err?.printStackTrace()
        })
    }

    fun CheckGetRegisterUser(user: User) {
        try {


            user.fcm_token = fcmToken
            /**등록된 회원인지 체크 후 없으면 등록
             * param provider_id
             * result boolean
             */
            getUser(user.provider_id, { err, userResponse ->
                if (userResponse == null) {
                    Logger.d(err)
                    err?.printStackTrace()
                } else {
                    //기존에 등록되있는 회원이라면  //local DB
                    if (userResponse.exist) {
                        if (userResponse.user.fcm_token != fcmToken) {
                            updateFcmTokenAndRealmInsert(userResponse.user, fcmToken)
                        }
                        insertUser(user)
                        Logger.d("기존 회원입니다.")
                    } else {

                 HTTP.createUser(user).subscribe({ insertUser(user)},{it.printStackTrace()})
                    }//신규유저라면

                }

            })


        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun FaceBookLogin(loginButton: LoginButton, callbackManager: CallbackManager) {
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
        loginButton.setReadPermissions("email")
        loginButton.setReadPermissions("public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val request = GraphRequest.newMeRequest(
                        loginResult.accessToken
                ) { `object`, response ->
                    try {
                        CheckGetRegisterUser(User(`object`.getString("email"), loginResult.accessToken.userId, `object`.getString("name"), "facebook"))
                    }
                    catch (e: Exception) {
                        Logger.d(e)
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                // App code
                Log.v("Home", "cancel")
            }


            override fun onError(exception: FacebookException) {
                // App code
                Log.v("HOME LOGIN", exception.cause.toString())
            }

        })
    }


    private fun getUser(provider_id: String, callback : (err : Throwable?, userResponse : UserResponse?)->Unit)  {
        HTTP.getUser(provider_id).subscribe({ userResponse -> callback(null, userResponse) },{ throwable -> callback(throwable, null)})

    }


    fun insertUser(item: User) {
        Logger.d(item.user_id)
        RealmUtil.insertData(item)
        Logger.d(RealmUtil.findData(User::class.java))
        Logger.d("유저 정보 db에 저장 성공 ")
    }


    fun updateFcmTokenAndRealmInsert(user: User, fcm_token: String) {
        HTTP.updateToken(user.provider_id,fcm_token).subscribe({
            Logger.d("fcm_token update!!")
            Logger.d("유저 정보 db에 저장 성공 ")
            user.fcm_token = fcm_token
            RealmUtil.insertData(user)
        },{it.printStackTrace()})
    }



    interface ExistUserCallback {
        fun existCallback(err: Throwable, isExist: Boolean?)
    }

}
