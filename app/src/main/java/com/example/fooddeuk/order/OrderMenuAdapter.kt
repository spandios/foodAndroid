package com.example.fooddeuk.order

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.util.toPx
import io.realm.RealmList
import kotlinx.android.synthetic.main.item_order_menu.view.*
import java.util.*

/**
 * Created by heojuyeong on 2017. 9. 30..
 */

class OrderMenuAdapter(private val mContext: Context, private val modelList: ArrayList<CartItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cartOptions = RealmList<CartOption>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val cartItem: CartItem = modelList[position]

        if (holder is ViewHolder) {
            holder.orderMenuName.text = cartItem.menu.name
            holder.orderMenuCount.text = cartItem.menu_count
//            holder.orderResultPrice.text = cartItem.totalPrice
            cartItem.selNecessaryOptionList.forEach {
                it.selectedOptionList.forEach {
                    holder.layout_order_menu_option.addView(initTextView(it.menu_option_name))
                }
            }
            cartItem.selUnNecessaryOptionList.forEach {
                it.selectedOptionList.forEach {
                    holder.layout_order_menu_option.addView(initTextView(it.menu_option_name))
                }
            }





        }


    }

    fun initTextView(textString : String) : TextView{
        return TextView(mContext).apply {
            textSize=11f
            text="($textString)"
            layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT).apply { setMargins(0,0,4.toPx,0) }


        }
    }

    override fun getItemCount(): Int = modelList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderMenuName = itemView.order_history_menu_name
        var orderMenuCount = itemView.order_history_menu_count
        var layout_order_menu_option = itemView.layout_order_menu_option
//        var orderResultPrice = itemView.order_menu_result_price

    }

}
