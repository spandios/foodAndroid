package com.example.fooddeuk.cart.model

import com.example.fooddeuk.order.model.MenuSide
import com.example.fooddeuk.util.RealmUtil

/**
 * Created by heo on 2018. 3. 3..
 */

interface CartItemDataSource {
    fun getCartItem(): ArrayList<CartItem>
    fun getMenuSide() : ArrayList<MenuSide>

}

object CartItemLocalRepository : CartItemDataSource {

    override fun getCartItem(): ArrayList<CartItem>{
        return ArrayList<CartItem>().apply { addAll(RealmUtil.findDataAll(CartItem::class.java)) }
    }

    override fun getMenuSide(): ArrayList<MenuSide> {
        var menuList = ArrayList<MenuSide>()
        var cartList = RealmUtil.findDataAll(CartItem::class.java)

        for(i in 0 until cartList.size){
            menuList.add(MenuSide(cartList[i]))
        }

        return menuList
    }

}
