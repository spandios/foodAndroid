package com.example.fooddeuk.order.model

import com.example.fooddeuk.cart.model.CartItem

/**
 * Created by heo on 2018. 3. 3..
 */
class MenuSide(cartItem : CartItem) {
    var menu_id: String = cartItem.id
    var name: String = cartItem.menu.name
    var price: String =cartItem.menu.price
    var avgtime: String = cartItem.menu.price
    var menu_count: String = cartItem.menu_count
    var totalPrice: String = cartItem.totalPrice
    var selNecessaryOptionListPost: ArrayList<PostSelectedOption> = ArrayList()
    var selUnNecessaryOptionListPost: ArrayList<PostSelectedOption> = ArrayList()

    init {
        if(cartItem.selNecessaryOptionList.isNotEmpty()){
            cartItem.selNecessaryOptionList.forEach {
                selNecessaryOptionListPost.add(PostSelectedOption(it))
            }
        }
        if(cartItem.selUnNecessaryOptionList.isNotEmpty()){
            cartItem.selNecessaryOptionList.forEach {
                selUnNecessaryOptionListPost.add(PostSelectedOption(it))
            }
        }
    }

}

