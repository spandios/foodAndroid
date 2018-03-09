package com.example.fooddeuk.order_history

/**
 * Created by heo on 2018. 1. 25..
 */


import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.OrderHistoryMenuAdapter
import com.example.fooddeuk.order.model.OrderResponse
import com.example.fooddeuk.util.setting
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_order_history_ing.view.*





/**
 * Created by heo on 2018. 1. 25..
 */
class OrderHistoryViewHolder(val context: Context, parent: ViewGroup?) : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history_ing, parent, false)){


    fun bind(orderResponse: OrderResponse) {
        itemView.bindView(orderResponse)

    }

    private fun View.bindView(orderResponse: OrderResponse) {
        Picasso.with(context).load("https://maps.googleapis.com/maps/api/staticmap?center=${orderResponse.restaurant.lat},${orderResponse.restaurant.lng}&zoom=17&scale=1&size=600x300&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:1%7C${orderResponse.restaurant.lat},${orderResponse.restaurant.lng}&key=AIzaSyBilL-hPo0Rsm5cRWw0Dj7hABnC6bzN9uk").fit().into(order_list_static_map)
        order_list_id.text="주문 번호 : ${orderResponse.id}"
        order_list_rest_name.text = orderResponse.restaurant.rest_name
        order_list_date.text= orderResponse.created_at
        order_list_state.text="주문 상태 : ${orderResponse.status}"
        order_list_arrivedTime.text="도착 예정 시간 : ${orderResponse.user.arrivedTime}"
        order_list_result_price.text="총 주문 금액 : ${orderResponse.user.completePrice}"
        order_list_menu_recyclerview.setting(OrderHistoryMenuAdapter(context,orderResponse.orderMenuItems),false,true,true)
        setBackgroundColor()


    }

    fun setBackgroundColor(){
        val white = ContextCompat.getColor(context,R.color.white)
        with(itemView){
            layout_order_history_restaurant.setBackgroundColor(white)
            layout_order_history_reserve.setBackgroundColor(white)
            layout_order_history_menu.setBackgroundColor(white)
            layout_order_history_map.setBackgroundColor(white)
        }

    }




}