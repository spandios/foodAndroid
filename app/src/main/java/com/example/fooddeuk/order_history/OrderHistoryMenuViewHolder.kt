package com.example.fooddeuk.order_history

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.option.OptionAdapter
import com.example.fooddeuk.util.setting
import com.example.fooddeuk.util.toJustWon
import kotlinx.android.synthetic.main.item_order_history_menu.view.*

/**
 * Created by heo on 2018. 3. 9..
 */
class OrderHistoryMenuViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    fun bind(orderMenuItem: OrderMenuItem){
        with(itemView){
            var optionList : ArrayList<CartOption> = ArrayList()
            order_history_menu_name.text=String.format(itemView.context.getString(R.string.order_history_menu_name_cnt),orderMenuItem.name,orderMenuItem.menu_count)
            order_history_menu_price.text=orderMenuItem.price.toJustWon()
            orderMenuItem.selNecessaryOptionListPost.forEach {
                optionList.addAll(it.selectedOptionList)
            }
            orderMenuItem.selUnNecessaryOptionListPost.forEach {
                optionList.addAll(it.selectedOptionList)
            }

            recycle_order_history_menu_option.setting(OptionAdapter(optionList,isOrderHistory = true),hasFixed = true)


        }
    }

}