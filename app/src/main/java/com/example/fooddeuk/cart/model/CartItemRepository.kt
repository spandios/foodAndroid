package com.example.fooddeuk.cart.model

import com.example.fooddeuk.order.model.MenuSide
import com.example.fooddeuk.util.RealmUtil
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by heo on 2018. 3. 3..
 */

interface CartItemDataSource {
    fun getCartItem(): ArrayList<CartItem>
    fun getMenuSide() : ArrayList<MenuSide>

}

object CartItemLocalRepository : CartItemDataSource {

    override fun getCartItem(): ArrayList<CartItem>{
        val realm = Realm.getDefaultInstance()
        val cartRealmList : RealmResults<CartItem>? = RealmUtil.findDataAll(CartItem::class.java)
        cartRealmList?.let {
            val cartList = realm.copyFromRealm(cartRealmList)
            return cartList as ArrayList<CartItem>
        }?:return ArrayList<CartItem>()
    }

    override fun getMenuSide(): ArrayList<MenuSide> {
        var menuList = ArrayList<MenuSide>()
        var cartList = RealmUtil.findDataAll(CartItem::class.java)
        cartList?.let {
            for(i in 0 until it.size){
                menuList.add(MenuSide(it[i]!!))
            }
        }


        return menuList
    }

}
