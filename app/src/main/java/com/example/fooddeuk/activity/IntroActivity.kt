package com.example.fooddeuk.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.location.Location.getLocation
import com.example.fooddeuk.util.LoginUtil
import com.example.fooddeuk.util.NetworkUtil
import com.example.fooddeuk.util.StartActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        basicInit()
    }

    private fun basicInit() {
        Picasso.with(this).load(R.drawable.rv2).into(introImageView)
        NetworkUtil.CheckNetGps(this)
        permissionCheck()
    }


    private fun permissionCheck() {
        val permissionListener = object : PermissionListener {
            @SuppressLint("MissingPermission")
            override fun onPermissionGranted() {
                LoginUtil.checkUser({exist ->
                    when(exist){
                        true->getLocation({ lat, lng ->  nextActivity()})
                        false->{StartActivity(LoginActivity::class.java)
                        finish()}
                    }
                })
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                Toast.makeText(this@IntroActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check()
    }

    private fun nextActivity(){
        Handler().postDelayed({
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 10)
    }



    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


//        //facebook
//        if (AccessToken.getCurrentAccessToken() != null) {
//            LoginUtil.initUser(AccessToken.getCurrentAccessToken().userId)
//            return
//        }
//        //KAKAO
//        KAKAO.kakaoAccessTokenInfo()

//        //NAVER
//        val naverLoginModule = NAVER.getNaverLoginModule(this)
//        if (naverLoginModule.getState(this).toString() == "OK") {
//            Thread {
//                val response = naverLoginModule.requestApi(this, naverLoginModule.getAccessToken(this), "https://openapi.naver.com/v1/nid/me")
//                try {
//                    val jsonResult = JSONObject(response).getJSONObject("response")
//                    val provider_id = jsonResult.getString("enc_id")
//                    LoginUtil.initUser(provider_id)
//                    // 액티비티 이동 등 원하는 함수 호출
//                }
//                catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }.start()
//        }


}



