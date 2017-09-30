package com.example.heojuyeong.foodandroid.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.heojuyeong.foodandroid.R
import com.example.heojuyeong.foodandroid.model.cart.CartItem
import kotlinx.android.synthetic.main.item_order_menu.view.*
import java.util.*

/**
 * Created by heojuyeong on 2017. 9. 30..
 */

class OrderMenuAdapter(private val mContext: Context, private var modelList: ArrayList<CartItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem:CartItem=modelList[position]
        if (holder is OrderMenuAdapter.ViewHolder) {
            holder.orderMenuName.text=cartItem.menu.menu_name
            holder.orderMenuCount.text=cartItem.menu_count
            holder.orderResultPrice.text=cartItem.totalPrice

            for(item in cartItem.option){
                var optionName=TextView(mContext)
                optionName.text=item.menu_option_name
                holder.orderMenuOptionLayout.addView(optionName)
            }
        }


    }

    override fun getItemCount(): Int {
        return modelList.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var orderMenuName=itemView.order_menu_name
        var orderMenuCount=itemView.order_menu_count
        var orderResultPrice=itemView.order_menu_result_price
        var orderMenuOptionLayout=itemView.order_menu_option_layout
    }

}
