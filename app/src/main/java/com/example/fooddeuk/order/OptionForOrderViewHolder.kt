package com.example.fooddeuk.order

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.cart.model.CartOption
import kotlinx.android.synthetic.main.item_option_for_order.view.*

/**
 * Created by heo on 2018. 3. 4..
 */
class OptionForOrderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    fun bind(option : CartOption){
        with(itemView){
            txt_option_content.text="(${option.menu_option_name})"
//            txt_option_price.text=option.menu_option_price.addPlus()
        }
    }

}