package com.example.fooddeuk.order_history

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.R
import com.example.fooddeuk.login.User
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.util.RealmUtil
import com.example.fooddeuk.util.setting
import kotlinx.android.synthetic.main.activity_order_history.*

class OrderHistoryActivity : AppCompatActivity() {
    private var user_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        val userData = RealmUtil.findData(User::class.java)
        if (userData != null) {
            user_id = RealmUtil.findData(User::class.java).user_id
            if (user_id != null) {
                HTTP.Single(GlobalApplication.httpService.getCurrentOrder(user_id)).subscribe({
                    order_history_loading_bar.visibility = View.GONE
                    currentOrderRecyclerView.setting(OrderHistoryAdapter(this, it), true)
                }, {
                    it.printStackTrace()
                })
            }
        }

    }
}
