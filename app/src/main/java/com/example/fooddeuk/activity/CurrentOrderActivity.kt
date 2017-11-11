package com.example.fooddeuk.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.fooddeuk.R
import com.example.fooddeuk.http.OrderService
import com.example.fooddeuk.model.menu.OrderItem
import com.example.fooddeuk.model.user.UserItemRealm
import com.example.fooddeuk.util.RealmUtil
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CurrentOrderActivity : AppCompatActivity() {
    var user_id:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_order)

        val userResult = RealmUtil.findDataAll(UserItemRealm::class.java)
        if (userResult!!.size > 0) {
            //            user_id = userResult.get(0).user_id;
            user_id = 32
        } else {
            Logger.e("user정보 없음")
        }
        OrderService.getCurrentOrder(user_id!!).enqueue(object : Callback<ArrayList<OrderItem>> {
            override fun onResponse(call: Call<ArrayList<OrderItem>>, response: Response<ArrayList<OrderItem>>) {
                if (response.isSuccessful) {
                    Logger.d(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<OrderItem>>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}
