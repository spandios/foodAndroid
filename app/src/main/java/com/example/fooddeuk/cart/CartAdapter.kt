package com.example.fooddeuk.cart

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.model.cart.CartItem
import com.example.fooddeuk.util.RealmUtil
import java.util.*

class CartAdapter(private val mContext: Context, private var cartItemList: ArrayList<CartItem>, private val cart_menu_order : Button) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mItemClickListener: OnItemClickListener? = null
//    fun updateList(cartItemList: ArrayList<CartItem>) {
//        this.cartItemList = cartItemList
//        notifyDataSetChanged()
//    }

    //장바구니 아이템 삭제

    private fun removeList(position:Int){
        RealmUtil.removeDataById(CartItem::class.java, cartItemList[position].id)
        cartItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cartItemList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_cart,parent,false)

        val vh= CartViewHolder(mContext,itemView)
        vh.itemView.findViewById<TextView>(R.id.cart_delete_button).setOnClickListener { removeList(vh.adapterPosition) }
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //자동 형 변환
        val cartItem = cartItemList[position]
        (holder as CartViewHolder).bind(cartItem)

    }


    override fun getItemCount(): Int {
        return cartItemList.size
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    private fun getItem(position: Int): CartItem {
        return cartItemList[position]
    }


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, model: CartItem)
    }



}

