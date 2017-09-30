package com.example.heojuyeong.foodandroid.activity

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.example.heojuyeong.foodandroid.R
import com.example.heojuyeong.foodandroid.adapter.OrderMenuAdapter
import com.example.heojuyeong.foodandroid.model.cart.CartItem
import com.example.heojuyeong.foodandroid.util.LayoutUtil
import com.example.heojuyeong.foodandroid.util.PriceUtil
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.item_order_menu.view.*
import java.util.*


class OrderActivity : AppCompatActivity() , com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {




    val realm: Realm = Realm.getDefaultInstance()
    var cartItemList=ArrayList<CartItem>()
    var isSetArrivedTime:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCartItem()
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


    private fun getCartItem(){
        val cartItem = realm.where(CartItem::class.java).findAll()
        cartItemList.addAll(realm.copyFromRealm(cartItem))
    }


    private fun setOrderAdapter(){
        val adapter=OrderMenuAdapter(this,cartItemList)
        LayoutUtil.RecyclerViewSetting(this.applicationContext,orderMenuList)
        orderMenuList.adapter=adapter

        //listview가 그려진 후 각 아이템의 가격을 더 해 최종가격을 구한다.
        orderMenuList.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val childCount=orderMenuList.childCount
                var i=0
                var sum=0
                while(i<childCount){
                    val cartItemViewHolder = orderMenuList.getChildViewHolder(orderMenuList.getChildAt(i))
                    sum+= PriceUtil.getOriginalPrice(cartItemViewHolder.itemView.order_menu_result_price.text.toString())
                    i++
                }
                orderResultPrice.text=PriceUtil.comma_won(sum)
                // remove this layout listener - as it will run every time the view updates
                if (orderMenuList.viewTreeObserver.isAlive) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        orderMenuList.viewTreeObserver
                                .removeOnGlobalLayoutListener(this)
                    } else {
                        orderMenuList.viewTreeObserver
                                .removeGlobalOnLayoutListener(this)
                    }
                }
            }
        })


    }

    private fun setView(){
            orderArriveTime.setOnClickListener({
                val now = Calendar.getInstance()
                val hour = now.get(Calendar.HOUR_OF_DAY)
                val minute = now.get(Calendar.MINUTE)

                val dpd = TimePickerDialog.newInstance(
                        this@OrderActivity,hour,minute,false
                )
                dpd.setMinTime(hour,minute,0)
//                dpd.setMaxTime()
                dpd.show(fragmentManager, "Timepickerdialog")

            })
    }


    override fun onTimeSet(view: com.wdullaer.materialdatetimepicker.time.TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val time = "예상 도착 시간: " + hourOfDay + "시 " + minute + "분"
        orderArriveTime.text = time
        isSetArrivedTime =true
    }

}
