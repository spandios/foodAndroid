package com.example.fooddeuk.order.model

import java.util.*

/**
 * Created by heo on 2018. 3. 3..
 */
data class OrderPost(var restaurant: RestaurantSide, var user: UserSide, var cartItems: ArrayList<MenuSide>, var status: String)