package com.example.fooddeuk.cart

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.RealmUtil
import java.util.*

class CartAdapter(private val mContext: Context, private var cartItemList: ArrayList<CartItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    fun updateList(cartItemList: ArrayList<CartItem>) {
//        this.cartItemList = cartItemList
//        notifyDataSetChanged()
//    }


    private fun removeList(position: Int) {
        RealmUtil.removeDataById(CartItem::class.java, cartItemList[position].id)
        cartItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cartItemList.size)
        if(cartItemList.size==0){
            RxBus.publish(RxBus.CartFragmentSizeZero,true)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false)
        val vh = CartViewHolder(mContext, itemView)
        vh.itemView.findViewById<TextView>(R.id.cart_delete_button).setOnClickListener { removeList(vh.adapterPosition) }
        return vh

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        if (holder is CartViewHolder) {
            holder.bind(cartItem)
        }
    }


    override fun getItemCount(): Int = cartItemList.size

}

