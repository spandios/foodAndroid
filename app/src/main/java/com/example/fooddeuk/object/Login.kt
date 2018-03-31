package com.example.fooddeuk.`object`

import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.network.HTTP.completable
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import com.example.fooddeuk.user.User
import com.example.fooddeuk.user.UserResponse
import com.example.fooddeuk.util.RealmUtil
import com.iwedding.app.helper.PrefUtil
import com.orhanobut.logger.Logger

/**
 * Created by heojuyeong on 2017. 10. 6..
 */

object Login {
    fun checkUser(callback: () -> Unit) {
        var exist: Boolean? = PrefUtil.getValue(PrefUtil.EXIST, false)
        val providerId: String? = PrefUtil.getValue(PrefUtil.PROVIDER_ID, "")
        var fcm_token: String? = PrefUtil.getValue(PrefUtil.FCM_TOKEN, "")
        var phone: String? = PrefUtil.getValue(PrefUtil.PHONE, "")
        var name: String? = PrefUtil.getValue(PrefUtil.NAME, "")
        var provider : String? = PrefUtil.getValue(PrefUtil.PROVIDER,"")
        GlobalVariable.provider= provider.toString()

        if (exist!!) {
            Logger.d("자동 로그인 start")
            if (!providerId.isNullOrEmpty() && !fcm_token.isNullOrEmpty() && !name.isNullOrEmpty()) {

                getUser(providerId!!, { err, userResponse ->
                    if (err != null) {
                        err.printStackTrace()
                        Logger.d("서버 불가능")
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
                                    PrefUtil.setValue("fcm_token", user.fcm_token)
                                    Logger.d("fcm token update $fcm_token")
                                }

                                if (name != user.user_name) {
                                    name = user.user_name
                                    PrefUtil.setValue("name", user.user_name)
                                }
//                                if (user.phone != phone) {
//                                    Logger.d("phone fail")
//                                    PrefUtil.setValue("phone", user.phone)
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


    fun registerUser(user: User, callback: (success:Boolean) -> Unit) {
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


        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun insertUserPref(user: User) {
        PrefUtil.apply {
            setValue(PROVIDER_ID, user.provider_id)
            setValue(EMAIL, user.email)
            setValue(FCM_TOKEN, user.fcm_token)
            setValue(NAME, user.user_name)
            setValue(PROVIDER, user.provider)
            setValue(EXIST,true)
        }
    }

    fun getUser(provider_id: String, callback: (err: Throwable?, userResponse: UserResponse?) -> Unit) {
        Logger.d(provider_id)
        httpService.getUser(provider_id).compose(singleAsync()).subscribe(
                { userResponse ->
                    Logger.d(userResponse)
                    callback(null, userResponse) }, { throwable -> callback(throwable, null) })

    }


    fun insertUser(item: User) {
        RealmUtil.insertData(item)
        Logger.d("유저 정보 db에 저장 성공 ")
    }


    fun updateFcmToken(user: User, fcm_token: String) {
        HTTP.completable(httpService.updateToken(user.provider_id, fcm_token)).subscribe({
            Logger.d("fcm_token update!!")
            PrefUtil.setValue(PrefUtil.FCM_TOKEN, user.fcm_token)
        }, { it.printStackTrace() })
    }


}
