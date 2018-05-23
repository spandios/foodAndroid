package com.example.fooddeuk.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalVariable
import com.example.fooddeuk.`object`.GlobalVariable.KAKAO
import com.example.fooddeuk.`object`.GlobalVariable.NAVER
import com.example.fooddeuk.`object`.Login
import com.example.fooddeuk.user.User
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.auth.ErrorCode
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class LoginActivity : BaseActivity() {
    private lateinit var kakaoCallback : SessionCallback
    private lateinit var callbackManager : CallbackManager

    private val loginCallback : (success : Boolean) -> Unit = {it->when(it){
        true->finish()
        false->showToast("로그인에 실패했습니다.")
    }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login_kakao.setOnClickListener{
            kakaoCallback=SessionCallback()
            Session.getCurrentSession().addCallback(kakaoCallback)
            Session.getCurrentSession().checkAndImplicitOpen()
            btn_logic_kakao.performClick() }
        btn_login_naver.setOnClickListener{NAVER()}
        btn_login_facebook.setOnClickListener{setFacebook()}

    }



    private fun setFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList<String>("public_profile", "email"))
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val request = GraphRequest.newMeRequest(
                                loginResult.accessToken
                        ) { `object`, response ->
                                Login.registerUser(User(`object`.getString("email"), loginResult.accessToken.userId, `object`.getString("name"), GlobalVariable.FACEBOOK, ""),loginCallback)
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

    fun NAVER() {
        val mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
                this,
                this.getString(R.string.naver_client_key),
                this.getString(R.string.naver_secret_key),
                "fooddeuk"
        )
            val naverHandler =  @SuppressLint("HandlerLeak")
            object : OAuthLoginHandler() {
                override fun run(success: Boolean) {
                    if (success) {
                        val accessToken = mOAuthLoginModule.getAccessToken(this@LoginActivity)
                        Thread {
                            val response = mOAuthLoginModule.requestApi(this@LoginActivity, accessToken, "https://openapi.naver.com/v1/nid/me")
                            try {
                                val jsonResult = JSONObject(response).getJSONObject("response")
                                Logger.d(jsonResult.toString())
                                // response 객체에서 원하는 값 얻어오기
                                val email = jsonResult.getString("email")
                                val name = jsonResult.getString("name")
                                val provider_id = jsonResult.getString("id")
                                val profile_image = jsonResult.getString("profile_image")
                                // 액티비티 이동 등 원하는 함수 호출
                                Login.registerUser(User(email, provider_id, name, NAVER, profile_image),loginCallback)
                            }
                            catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }.start()
                    } else {
                        val errorCode = mOAuthLoginModule.getLastErrorCode(this@LoginActivity).code
                        val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                        Toast.makeText(this@LoginActivity, "Naver Login errorCode:" + errorCode
                                + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        mOAuthLoginModule.startOauthLoginActivity(this,naverHandler)
}



    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            UserManagement.requestMe(object : MeResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    val message = "failed to get user info. msg=" + errorResult!!
                    Logger.d("kakao error message : $message")
                    val result = ErrorCode.valueOf(errorResult.errorCode)
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish()
                    }
                }
                override fun onSessionClosed(errorResult: ErrorResult) {}

                override fun onNotSignedUp() {}

                override fun onSuccess(userProfile: UserProfile) {
                    //로그인 성공
                    Login.registerUser(User(userProfile.email, userProfile.id.toString(), userProfile.nickname, KAKAO, userProfile.profileImagePath),loginCallback)
                }
            })

        }

        override fun onSessionOpenFailed(exception: KakaoException) {
            // 세션 연결이 실패했을때
            Logger.d("${exception.printStackTrace()}")
            showToast("카카오 세션 실패")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //카카오
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        //FACEBOOK
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }



}
