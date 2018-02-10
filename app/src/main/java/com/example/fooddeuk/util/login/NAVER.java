package com.example.fooddeuk.util.login;

import android.content.Context;
import android.widget.Toast;

import com.example.fooddeuk.R;
import com.example.fooddeuk.model.user.User;
import com.example.fooddeuk.util.LoginUtil;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by heojuyeong on 2017. 10. 8..
 */

public class NAVER {
    public static OAuthLogin getNaverLoginModule(Context context){

        OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                context,
                context.getString(R.string.naver_client_key),
                context.getString(R.string.naver_secret_key),
                "fooddeuk"
        );
        return mOAuthLoginModule;
    }

    public static OAuthLoginHandler getNaverLoginHandler(Context mContext, OAuthLogin mOAuthLoginModule){
        OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                    String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                    long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                    String tokenType = mOAuthLoginModule.getTokenType(mContext);
                    new Thread(() -> {
                        String response = mOAuthLoginModule.requestApi(mContext, accessToken, "https://openapi.naver.com/v1/nid/me");
                        try {
                            JSONObject jsonResult = new JSONObject(response).getJSONObject("response");
                            Logger.d(jsonResult.toString());
                            // response 객체에서 원하는 값 얻어오기
                            String email = jsonResult.getString("email");
                            String name = jsonResult.getString("name");
                            String provider_id = jsonResult.getString("enc_id");
                            LoginUtil.INSTANCE.CheckGetRegisterUser(new User(email,provider_id,name,"naver"));
                            // 액티비티 이동 등 원하는 함수 호출
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                    String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                    Toast.makeText(mContext, "Naver Login errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }
        };
        return mOAuthLoginHandler;
    }

}
