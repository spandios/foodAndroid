package com.example.fooddeuk.util

import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.`object`.Login
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.iwedding.app.helper.PrefUtil
import com.kakao.util.helper.log.Logger

/**
 * Created by heojuyeong on 2017. 10. 5..
 */

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Logger.d(refreshedToken)
        sendRegistrationToServer(refreshedToken)
        GlobalApplication.fcmToken = refreshedToken
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        token?.let {
            val providerId: String? = PrefUtil.getValue(PrefUtil.PROVIDER_ID, "")
            if(!providerId.isNullOrEmpty()){
                with(Login) {
                    getUser(providerId!!,{err, userResponse ->
                        userResponse?.let{
                            updateFcmToken(userResponse.user,token)
                        }?:err?.printStackTrace()
                    })
                }
            }else{
                Logger.d("not Login")
            }

        }
    }

    companion object {

        private val TAG = "MyFirebaseIIDService"
    }
}
