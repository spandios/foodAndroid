package com.example.fooddeuk.`object`

import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.network.HTTP.completable
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import com.example.fooddeuk.user.User
import com.example.fooddeuk.user.UserResponse
import com.example.fooddeuk.util.RealmUtil
import com.iwedding.app.helper.UserPrefUtil
import com.orhanobut.logger.Logger

/**
 * Created by heojuyeong on 2017. 10. 6..
 */

object Login {
    fun checkUser(callback: () -> Unit) {
        val exist: Boolean? = UserPrefUtil.getValue(UserPrefUtil.EXIST, false)
        val providerId: String? = UserPrefUtil.getValue(UserPrefUtil.PROVIDER_ID, "")
        val fcm_token: String? = UserPrefUtil.getValue(UserPrefUtil.FCM_TOKEN, "")
        var phone: String? = UserPrefUtil.getValue(UserPrefUtil.PHONE, "")
        var name: String? = UserPrefUtil.getValue(UserPrefUtil.NAME, "")
        val provider: String? = UserPrefUtil.getValue(UserPrefUtil.PROVIDER, "")
        GlobalVariable.provider = provider.toString()

        if (exist!!) {
            //예전 로그인 정보가 남아있을 때
            if (!providerId.isNullOrEmpty() && !fcm_token.isNullOrEmpty() && !name.isNullOrEmpty()) {

                getUser(providerId!!, { err, userResponse ->
                    if (err != null) {
                        err.printStackTrace()
                        callback()
                    } else {
                        userResponse?.let {
                            if (it.exist) {
                                Logger.d("provider id : $providerId , fcm_token $fcm_token , name $name")
                                val user = it.user
                                if (user.fcm_token != GlobalApplication.fcmToken) {
                                    updateFcmToken(user, GlobalApplication.fcmToken)
                                    user.fcm_token = GlobalApplication.fcmToken
                                }
                                //Pref Fcm Token
                                if (fcm_token != GlobalApplication.fcmToken) {
                                    UserPrefUtil.setValue("fcm_token", user.fcm_token)
                                    Logger.d("fcm token update $fcm_token")
                                }

                                if (name != user.user_name) {
                                    name = user.user_name
                                    UserPrefUtil.setValue("name", user.user_name)
                                }
//                                if (user.phone != phone) {
//                                    Logger.d("phone fail")
//                                    UserPrefUtil.setValue("phone", user.phone)
//                                }//TODO UPDATE PHONE
                                GlobalVariable.provider = user.provider
                                GlobalVariable.isLogin = true
                                insertUser(user)
                                callback()
                                Logger.d("정상 자동로그인")
                            } else {
                                Logger.d("서버측에서 정보가 없음")
                                //TODO 유저 정보가 비었음 go to LoginActivity
                                callback()
                            }
                        }
                    }
                })
                //Pref 정보가 하나라도 오류있을 경우
            } else {
                Logger.d("안드 pref에서 정보가 없음")
                //TODO 유저 정보가 비었음 go to LoginActivity

                callback()
            }
        } else {
            Logger.d("user empty")
            //TODO 유저 정보가 없음 go to LoginActivity

            callback()
        }
    }


    //유저등록
    fun registerUser(user: User, callback: (success: Boolean) -> Unit) {

        try {
            getUser(user.provider_id, { err, userResponse ->
                if (err != null) err.printStackTrace()
                userResponse?.let {
                    GlobalVariable.provider = user.provider
                    GlobalVariable.isLogin = true

                    if (it.exist) {
                        Logger.d("기존 유저입니다.")
                        insertUserPref(it.user)
                        insertUser(it.user)
                        callback(true)
                    } else {
                        user.fcm_token = GlobalApplication.fcmToken
                        completable(httpService.createUser(user)).subscribe({
                            insertUser(user)
                            insertUserPref(user)
                            callback(true)
                            Logger.d("새로운 유저 등록 $user")
                        }, {
                            Logger.d("regisetr fail ${user.user_name}")
                            it.printStackTrace()
                            callback(false)
                        })


                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun insertUserPref(user: User) {
        UserPrefUtil.apply {
            setValue(PROVIDER_ID, user.provider_id)
            setValue(EMAIL, user.email)
            setValue(FCM_TOKEN, user.fcm_token)
            setValue(NAME, user.user_name)
            setValue(PROVIDER, user.provider)
            setValue(EXIST, true)
        }
    }

    fun getUser(provider_id: String, callback: (err: Throwable?, userResponse: UserResponse?) -> Unit) {

        httpService.readUser(provider_id).compose(singleAsync()).subscribe(
                { userResponse ->
                    callback(null, userResponse)
                }, { throwable -> callback(throwable, null) }
        )
    }


    fun insertUser(item: User) {
        RealmUtil.insertData(item)
        Logger.d("유저 정보 db에 저장 성공 ")
    }


    fun updateFcmToken(user: User, fcm_token: String) {
        HTTP.completable(httpService.updateToken(user.provider_id, fcm_token)).subscribe({
            Logger.d("fcm_token update!!")
            UserPrefUtil.setValue(UserPrefUtil.FCM_TOKEN, user.fcm_token)
        }, { it.printStackTrace() })
    }


}
