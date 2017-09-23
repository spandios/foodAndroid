package com.example.heojuyeong.foodandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.heojuyeong.foodandroid.adapter.CartAdapter
import com.example.heojuyeong.foodandroid.model.cart.CartItem
import com.example.heojuyeong.foodandroid.util.LayoutUtil
import com.example.heojuyeong.foodandroid.util.RealmUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setToolBar()

        setCartAdapter()


    }


    private fun setToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.backbutton)
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun setCartAdapter() {
        val cartItemList = ArrayList<CartItem>()
        val cartItem = realm.where(CartItem::class.java).findAll()
        cartItemList.addAll(realm.copyFromRealm(cartItem))
        val adapter = CartAdapter(this, cartItemList)
        adapter.SetOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, model: CartItem) {
            }
        })
        LayoutUtil.RecyclerViewSetting(this.applicationContext,cartListView)
        cartListView.setHasFixedSize(true)
        cartListView.adapter = adapter

        // use a linear layout manager

        cartClear.setOnClickListener({
            RealmUtil.removeDataAll(CartItem::class.java)
            cartListView.adapter = null
        })

    }


}
