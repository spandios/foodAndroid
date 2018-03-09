package com.example.fooddeuk.order_history

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.option.OptionAdapter
import com.example.fooddeuk.util.setting
import kotlinx.android.synthetic.main.item_order_history_menu.view.*

/**
 * Created by heo on 2018. 3. 9..
 */
class OrderHistoryMenuViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    fun bind(orderMenuItem: OrderMenuItem){
        with(itemView){
            var optionList : ArrayList<CartOption> = ArrayList()
            order_history_menu_name.text=orderMenuItem.name
            order_history_menu_count.text=orderMenuItem.menu_count
            order_history_menu_price.text=orderMenuItem.price
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