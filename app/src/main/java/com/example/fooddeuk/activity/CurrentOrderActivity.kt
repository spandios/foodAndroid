package com.example.fooddeuk.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.fooddeuk.R

class CurrentOrderActivity : AppCompatActivity() {
    var user_id: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_order)
//
//        val userResult = RealmUtil.findDataAll(UserItemRealm::class.java)
//        if (userResult!!.size > 0) {
//            //            user_id = userResult.get(0).user_id;
//            user_id = 32
//        } else {
//            Logger.e("user정보 없음")
//        }
//        OrderService.getCurrentOrder(user_id!!).enqueue(object : Callback<ArrayList<OrderResponse>> {
//            override fun onResponse(call: Call<ArrayList<OrderResponse>>, response: Response<ArrayList<OrderResponse>>) {
//                if (response.isSuccessful) {
//                    Logger.d(response.body())
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<OrderResponse>>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })
//
//    }
    }
}
