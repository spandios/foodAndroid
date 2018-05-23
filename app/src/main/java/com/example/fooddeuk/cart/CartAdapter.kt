package com.example.fooddeuk.cart

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.PriceUtil
import com.example.fooddeuk.util.RealmUtil
import com.orhanobut.logger.Logger
import java.util.*
import kotlin.math.min

class CartAdapter(private val mContext: Context, private var cartItemList: ArrayList<CartItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private val havePicture = 0
    private val noPicture = 1

    override fun getItemViewType(position: Int): Int {
        return if(cartItemList[position].menu.picture!=null){
            return havePicture
        }else{
            noPicture
        }
    }

    private fun removeList(position: Int,minPrice : String) {
        RealmUtil.removeDataById(CartItem::class.java, cartItemList[position].id)
        cartItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cartItemList.size)
        RxBus.publish(RxBus.CartMinusPrice, minPrice)
//        if(cartItemList.size==0){
//            Logger.d("장바구니 비었음")
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==havePicture){
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false)
            val vh = CartViewHolder(mContext, itemView)
            vh.itemView.findViewById<TextView>(R.id.cart_delete_button).setOnClickListener {
                removeList(vh.adapterPosition,vh.itemView.findViewById<TextView>(R.id.cart_menu_result_price).text.toString())
            }
            return vh
        }else{
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_cart_no_picture, parent, false)
            val vh = CartViewHolder(mContext, itemView)
            vh.itemView.findViewById<TextView>(R.id.cart_delete_button).setOnClickListener {
                removeList(vh.adapterPosition,vh.itemView.findViewById<TextView>(R.id.cart_menu_result_price).text.toString())
            }
            return vh
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        if (holder is CartViewHolder) {
            holder.bind(cartItem)
        }
    }


    override fun getItemCount(): Int = cartItemList.size

}

