package com.example.fooddeuk.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.adapter.CartAdapter
import com.example.fooddeuk.model.cart.CartItem
import com.example.fooddeuk.util.IntentUtil
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.RealmUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.item_cart.view.*


class CartActivity : BaseActivity()  {
    val realm: Realm = Realm.getDefaultInstance()
    private val cartItemList = ArrayList<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setToolBar()
        setView()
        setCartAdapter()








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
        LayoutUtil.RecyclerViewSetting(this.applicationContext, cartListView)
        cartListView!!.adapter = adapter

        }

    private fun setView(){
        //모든 장바구니 삭제
        cartClear.setOnClickListener({
            RealmUtil.removeDataAll(CartItem::class.java)
            cartListView!!.adapter = null
        })

        cart_menu_order.setOnClickListener({
            if (cartItemList.size != 0) {
                /** 장바구니 각각의 아이템  수량과 가격 데이터 삽입 후 주문 activity 호출*/
                var childCount=cartListView.childCount
                var i=0
                while (i < childCount) {
                    var cartItemViewHolder=cartListView.findViewHolderForAdapterPosition(i)
                    val count: TextView = cartItemViewHolder.itemView.cart_menu_quantity
                    val totalPrice = cartItemViewHolder.itemView.cart_menu_result_price
                    cartItemList[i].menu_count = count.text.toString()
                    cartItemList[i].totalPrice = totalPrice.text.toString()
                    RealmUtil.insertData(cartItemList[i])
                    i++
                }

                IntentUtil.startActivity(this,OrderActivity::class.java)
            } else {
                Toast.makeText(this, "아이템이 없습니다", Toast.LENGTH_SHORT).show()
            }


        })


}
}
