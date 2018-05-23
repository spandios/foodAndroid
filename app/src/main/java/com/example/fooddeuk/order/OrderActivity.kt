package com.example.fooddeuk.order

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.custom.CustomTimeButton
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.util.*
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.realm.Realm
import io.socket.client.IO
import kotlinx.android.synthetic.main.activity_order.*
import org.joda.time.DateTime





class OrderActivity : AppCompatActivity(), OrderContract.View {
    var realm: Realm = Realm.getDefaultInstance()
    var isSetArrivedTime = true
    lateinit var arrivedTimePair : Pair<Int,Int>
    lateinit var restaurant: Restaurant
    lateinit var orderMenuAdapter: OrderMenuAdapter
    lateinit var presenter: OrderPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        getRealmItem()
        setView()
        presenter = OrderPresenter().apply {
            view = this@OrderActivity
            socket=IO.socket("http://10.0.2.2:3000").apply { connect() }
        }
        presenter.initCartListView()
        presenter.initTimeButton()

    }

    override fun setRecyclerView(cartItemList: ArrayList<CartItem>) {
        orderMenuAdapter = OrderMenuAdapter(this, cartItemList)
        orderMenuList.setting(orderMenuAdapter,verticalPadding = true, hasFixed = true)

        var resultPrice = 0
        cartItemList.forEach {
            resultPrice += it.totalPrice.getOriginalPrice()
        }
        orderResultPrice.text = resultPrice.toCommaWon()
//
    }

    //TODO RESTAURANT INTERVAL TIME
    private fun availableTimeSetting(maxMenuMakingTime: Int, interval: Int = 15): Pair<Int, Int> {
        var nowHour = DateTime.now().hourOfDay
        var nowMinutes = DateTime.now().minuteOfHour

        nowMinutes += maxMenuMakingTime

        if (nowMinutes >= 60) {
            nowHour += 1
            nowMinutes -= 60
        }

        if (nowMinutes < 15) {
            nowMinutes = 15
        } else if (nowMinutes in 15..29) {
            nowMinutes = 30
        } else if (nowMinutes in 30..44) {
            nowMinutes = 45
        } else if (nowMinutes >= 45) {
            nowHour += 1
            nowMinutes = 0
        }


        return Pair(nowHour, nowMinutes)
    }

    override fun setTimeButton(cartItemList: ArrayList<CartItem>) {
        var maxMenuMakingTime = 0

        Observable.range(0, cartItemList.size).flatMap {
            Observable.just(cartItemList[it])
        }.map {
            Logger.d(it.menu.avgtime)
            it.menu.avgtime

        }.filter { it.isNotEmpty() }
                .map { it.toInt() }.subscribe({
                    if (maxMenuMakingTime < it) {
                        maxMenuMakingTime = it
                    }
                })

        var (startHour, startMinute) = availableTimeSetting(maxMenuMakingTime)
        logger(startHour)
        logger(startMinute)
        var (endHour, endMinutes) = restaurant.closeTime.getTime()

        // 영업종료 전 30분
        endMinutes -= 30

        //새벽까지 운영일 경우
        if (endHour < 10) {
            endHour += 24

        }

        var hour = endHour - startHour


        val min = endMinutes - startMinute
        val tmpMin = min / 15
        val count: Int = hour * 4 + tmpMin

        for (i in 0..count) {

            layout_arrived_time.addView(CustomTimeButton(this, Pair(startHour, startMinute),clickListener = {timeButton->
                for(j in 0 until layout_arrived_time.childCount){
                    layout_arrived_time.getChildAt(j).let {
                        if(it.isSelected){
                            it.isSelected=false
                        }
                    }
                }
                timeButton.isSelected=true
                arrivedTimePair=timeButton.tag as Pair<Int, Int>
                logger(arrivedTimePair)

            }).apply { setMargin() })

            startMinute += 15
            if (startMinute >= 60) {
                startHour += 1
                startMinute = 0
            }
            if(startHour>=24){
                startHour-=24
            }
        }


    }

    private fun getRealmItem() {
        isSetArrivedTime = true
        restaurant = RealmUtil.findData(Restaurant::class.java)!!
    }


    private fun setView() {

        order_back_button.setOnClickListener {
            finish()
        }


        orderComplete.setOnClickListener({
            if (isSetArrivedTime) {
//                "${arrivedTimePair.first}:${arrivedTimePair.second}"
                presenter.order(restaurant, "11:30", requestText.text(), orderResultPrice.text())
            } else {
                Toast.makeText(this, "시간을 체크하세요", Toast.LENGTH_SHORT).show()
            }
        })

    }


    override fun successOrder() {
        toast("주문 완료")
        val intent = Intent(this@OrderActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("isOrder", true)
        startActivity(intent)
    }

    override fun failOrder() {
        toast("주문이 실했습니다.")
    }

    override fun notUser() {
        toast("유저가 없습니다.")
    }


}
