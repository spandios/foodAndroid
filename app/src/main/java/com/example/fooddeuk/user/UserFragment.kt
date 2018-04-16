package com.example.fooddeuk.user

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalVariable
import com.example.fooddeuk.activity.LoginActivity
import com.example.fooddeuk.order_history.OrderHistoryActivity
import com.example.fooddeuk.util.StartActivity
import com.facebook.login.LoginManager
import com.iwedding.app.helper.UserPrefUtil
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.nhn.android.naverlogin.OAuthLogin
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_user, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkIsLogin()
        btn_login.setOnClickListener{StartActivity(LoginActivity::class.java)}
        btn_current_order.setOnClickListener {
            StartActivity(OrderHistoryActivity::class.java)
        }

        btn_user_logout.setOnClickListener({
            MaterialDialog.Builder(activity!!)
                    .title("로그아웃")
                    .content("정말 로그아웃 하시겠습니까?")
                    .positiveText("예")
                    .onPositive { dialog, which ->
                        GlobalVariable.provider=""
                        GlobalVariable.isLogin=false
                        UserPrefUtil.setValue(UserPrefUtil.EXIST, false)
                        when(GlobalVariable.provider){
                            GlobalVariable.NAVER->OAuthLogin.getInstance().logout(activity!!)
                            GlobalVariable.KAKAO->UserManagement.requestLogout(object : LogoutResponseCallback() {
                                override fun onCompleteLogout() {
                                    Logger.d("logout")
                                }
                            })
                            GlobalVariable.FACEBOOK->LoginManager.getInstance().logOut()
                        }
                        UserPrefUtil.deleteData()
                        checkIsLogin()
                    }.show()
        })
    }


    override fun onResume() {
        super.onResume()
        checkIsLogin()
    }


    private fun checkIsLogin() {
        if (!GlobalVariable.isLogin) {
            layout_user_not_login.visibility = View.VISIBLE
            layout_user_login.visibility = View.GONE

        } else {
            layout_user_not_login.visibility = View.GONE
            layout_user_login.visibility = View.VISIBLE
        }
    }


}
