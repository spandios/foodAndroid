package com.example.fooddeuk.activity

import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.adapter.OrderMenuAdapter
import com.example.fooddeuk.http.OrderService
import com.example.fooddeuk.model.cart.CartItem
import com.example.fooddeuk.model.menu.OrderItem
import com.example.fooddeuk.model.restaurant.RestaurantItemRealm
import com.example.fooddeuk.model.user.UserItemRealm
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.PriceUtil
import com.example.fooddeuk.util.RealmUtil
import com.orhanobut.logger.Logger
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.item_order_menu.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class OrderActivity : BaseActivity(), com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    val realm: Realm = Realm.getDefaultInstance()
    var cartItemList = ArrayList<CartItem>()
    var rest_id: Int? = null
    var rest_admin_id: String? = null
    var rest_name:String?=null
    var isSetArrivedTime: Boolean = false
    var user_id: Int? = null
    var lat : Double = 0.0
    var lng : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getRealmItem()
        setContentView(R.layout.activity_order)
        setToolBar()
        setOrderAdapter()
        setView()
    }

    private fun setToolBar() {
        setSupportActionBar(orderToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        orderToolbar.setNavigationIcon(R.drawable.backbutton)
        orderToolbar.setNavigationOnClickListener { finish() }
    }

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    private fun getRealmItem() {
        val cartItem = realm.where(CartItem::class.java).findAll()
        cartItemList.addAll(realm.copyFromRealm(cartItem))
        val restaurantResult = RealmUtil.findDataAll(RestaurantItemRealm::class.java)
        if (restaurantResult.size > 0) {
            rest_id = restaurantResult[0].rest_id
            rest_admin_id = restaurantResult[0].rest_admin_id
            rest_name=restaurantResult[0].name
            lat=restaurantResult[0].lat
            lng=restaurantResult[0].lng

        } else {
            Logger.e("restaurant정보 없음")

        }
        val userResult = RealmUtil.findDataAll(UserItemRealm::class.java)
        if (userResult.size > 0) {
            user_id = userResult[0].user_id


        } else {
            Logger.e("user정보 없음")
        }


    }

    private fun setOrderAdapter() {
        val adapter = OrderMenuAdapter(this, cartItemList)
        LayoutUtil.RecyclerViewSetting(this.applicationContext, orderMenuList)
        orderMenuList.adapter = adapter

        //listview가 그려진 후 각 아이템의 가격을 더 해 최종가격을 구한다.
        orderMenuList.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val childCount = orderMenuList.adapter.itemCount
//                Logger.d(childCount)
                var sum = 0
                var i = 0
                while (i < childCount) {
                    val cartItemViewHolder = orderMenuList.findViewHolderForLayoutPosition(i)
//                    val cartItemViewHolder = orderMenuList.getChildViewHolder(orderMenuList.getChildAt(i))
//                    Logger.d(cartItemViewHolder.itemView.order_menu_result_price.text)
                    sum += PriceUtil.getOriginalPrice(cartItemViewHolder.itemView.order_menu_result_price.text.toString())
                    i++
                }

                orderResultPrice.text = PriceUtil.comma_won(sum)
//                 remove this layout listener - as it will run every time the view updates
                if (orderMenuList.viewTreeObserver.isAlive) {
                    orderMenuList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }

        })


    }

    private fun setView() {
        //도착할 시간 설정
        orderArriveTime.setOnClickListener({

            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val minute = now.get(Calendar.MINUTE)
            val dpd = TimePickerDialog.newInstance(
                    this@OrderActivity, hour, minute, false
            )
            dpd.setMinTime(Timepoint(hour, minute))
            val realmRestaurant = RealmUtil.getRestaurantRealm()
            //TODO 영업시간 지난 후 클릭 x
//                dpd.setMaxTime(Timepoint(realmRestaurant[0].close_time.substring(0,2).toInt(),realmRestaurant[0].close_time.substring(3,5).toInt()))

            dpd.show(fragmentManager, "Timepickerdialog")

        })

        /**
         * 주문하기
         * 주문하기
         * 주문하기
         * 주문하기
         * **/
        orderComplete.setOnClickListener({
            if (isSetArrivedTime) {
                //예상도착시간
                val arrivedTime = orderArriveTime.text.subSequence(9, orderArriveTime.text.length)
                //최종주문정보
                val orderItem = OrderItem(/*userid*/ user_id!!, rest_id!!, rest_admin_id, rest_name,cartItemList, arrivedTime.toString(), requestText.text.toString(), "주문수락대기", orderResultPrice.text.toString(),lat,lng)
                /**주문하기**/
                OrderService.order(orderItem).enqueue(object : Callback<OrderItem> {
                    override fun onResponse(call: Call<OrderItem>?, response: Response<OrderItem>?) {
//                        Logger.d("response post order: " + response?.body().toString())
                    }

                    override fun onFailure(call: Call<OrderItem>?, t: Throwable?) {
                        Logger.d(t)
                    }
                })
            } else {
                Toast.makeText(this, "시간을 체크하세요", Toast.LENGTH_SHORT).show()
            }
        })

    }

    //선택한 시간 표시
    override fun onTimeSet(view: com.wdullaer.materialdatetimepicker.time.TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val time = "예상 도착 시간: " + hourOfDay + ":" + minute
        orderArriveTime.text = time
        isSetArrivedTime = true
    }

}
