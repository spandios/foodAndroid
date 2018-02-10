package com.example.fooddeuk.fcm;

import com.example.fooddeuk.network.UserService;
import com.facebook.AccessToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kakao.util.helper.log.Logger;

/**
 * Created by heojuyeong on 2017. 10. 5..
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Logger.d(refreshedToken);
        sendRegistrationToServer(refreshedToken);
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
    private void sendRegistrationToServer(String token) {
        //FACEBOOK UPDATE TOKEN
        if(AccessToken.getCurrentAccessToken()!=null){
            Logger.d(token);
            UserService.updateToken(AccessToken.getCurrentAccessToken().getUserId(),token);
        }

    }
}
