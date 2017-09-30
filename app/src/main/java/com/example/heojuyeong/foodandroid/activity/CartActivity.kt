package com.example.heojuyeong.foodandroid.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.heojuyeong.foodandroid.R
import com.example.heojuyeong.foodandroid.adapter.CartAdapter
import com.example.heojuyeong.foodandroid.model.cart.CartItem
import com.example.heojuyeong.foodandroid.util.IntentUtil
import com.example.heojuyeong.foodandroid.util.LayoutUtil
import com.example.heojuyeong.foodandroid.util.RealmUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity()  {
    val realm: Realm = Realm.getDefaultInstance()
    val cartItemList = ArrayList<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setToolBar()
        setCartAdapter()
        setView()
    }


    private fun setToolBar() {
        setSupportActionBar(cartToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cartToolbar.setNavigationIcon(R.drawable.backbutton)
        cartToolbar.setNavigationOnClickListener { finish() }
    }

    private fun setCartAdapter() {

        val cartItem = realm.where(CartItem::class.java).findAll()
        cartItemList.addAll(realm.copyFromRealm(cartItem))
        val adapter = CartAdapter(this, cartItemList)
        adapter.SetOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, model: CartItem) {
            }
        })
        LayoutUtil.RecyclerViewSetting(this.applicationContext,cartListView)
        cartListView.adapter = adapter




    }

    private fun setView(){
        cart_menu_order.setOnClickListener({

            val childCount = cartListView.childCount
            var i = 0
            while (i < childCount) {
                val cartItemViewHolder = cartListView.getChildViewHolder(cartListView.getChildAt(i))
                //최종 수량 가격 정보 추가함
                val count:TextView= cartItemViewHolder.itemView.findViewById(R.id.cart_menu_quantity) as TextView
                val totalPrice=cartItemViewHolder.itemView.findViewById(R.id.cart_menu_result_price) as TextView
                cartItemList[i].menu_count=count.text.toString()
                cartItemList[i].totalPrice=totalPrice.text.toString()
                RealmUtil.insertData(cartItemList[i])
                i++
            }
            if(cartItemList.size!=0){
                IntentUtil.startActivity(this,OrderActivity::class.java)
            }else{
                Toast.makeText(this,"아이템이 없습니다", Toast.LENGTH_SHORT).show()
            }



        })

        cartClear.setOnClickListener({
            RealmUtil.removeDataAll(CartItem::class.java)
            cartListView.adapter = null
        })

    }


}
