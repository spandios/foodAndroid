package com.example.fooddeuk.order

import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.cart.model.CartItemLocalRepository
import com.example.fooddeuk.user.UserRepository
import com.example.fooddeuk.order.model.OrderPost
import com.example.fooddeuk.order.model.RestaurantSide
import com.example.fooddeuk.order.model.UserSide
import com.example.fooddeuk.restaurant.model.Restaurant
import com.google.gson.Gson
import io.socket.client.Socket

/**
 * Created by heo on 2018. 3. 3..
 */

interface OrderContract {
    interface View {
        fun setRecyclerView(cartItemList: ArrayList<CartItem>)
        fun successOrder()
        fun failOrder()
        fun notUser()
        fun setTimeButton(cartItemList: ArrayList<CartItem>)
    }

    interface Presenter {
        var view: View
        var socket : Socket
        fun getRestaurant()
        fun initCartListView()
        fun initTimeButton()
        fun order(restaurant: Restaurant, arrivedTime: String, requestText: String, orderResultPrice: String)
    }
}


class OrderPresenter : OrderContract.Presenter {
    override lateinit var socket : Socket
    override lateinit var view: OrderContract.View
    val cartItemRepository = CartItemLocalRepository
    private var userRepository = UserRepository

    override fun initTimeButton() {
        view.setTimeButton(cartItemRepository.getCartItem())
    }

    override fun getRestaurant() {

    }

    override fun initCartListView() {
        view.setRecyclerView(cartItemRepository.getCartItem())
    }

    override fun order(restaurant: Restaurant, arrivedTime: String, requestText: String, orderResultPrice: String) {
        if (userRepository.getUser() != null) {
            val userSide = UserSide(userRepository.getUser(), arrivedTime, requestText, orderResultPrice)
            val orderPost = OrderPost(RestaurantSide(restaurant), userSide, cartItemRepository.getMenuSide(), "접수 완료")
            val orderPostGson = Gson().toJson(orderPost)
            socket.emit("orderRequestPost", orderPostGson)
            view.successOrder()
        } else {
            //TODO show Login ALERT
            view.notUser()
        }

    }
}