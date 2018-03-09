package com.example.fooddeuk.cart

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.order_history.OrderMenuItem
import com.example.fooddeuk.order_history.OrderHistoryMenuViewHolder
import java.util.*

class OrderHistoryMenuAdapter(private val mContext: Context, private var orderMenuItem: ArrayList<OrderMenuItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_order_history_menu, parent, false)
        val vh = OrderHistoryMenuViewHolder(itemView)
        return vh

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val orderMenuItem = orderMenuItem[position]
        if (holder is OrderHistoryMenuViewHolder) {
            holder.bind(orderMenuItem)
        }
    }


    override fun getItemCount(): Int = orderMenuItem.size

}

