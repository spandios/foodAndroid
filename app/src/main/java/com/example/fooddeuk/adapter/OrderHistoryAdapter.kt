package com.example.fooddeuk.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.OrderHistoryMapActivity
import com.example.fooddeuk.model.order.OrderResponse
import com.example.fooddeuk.order.list.viewholder.OrderingViewHolder
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_order_history_ing.view.*


/**
 * Created by heo on 2018. 1. 24..
 */

class OrderHistoryAdapter(private val context : Context, private val responses:ArrayList<OrderResponse>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val orderIng = 0

    override fun getItemViewType(position: Int): Int
            = if(responses[position].status== "접수 대기"|| responses[position].status== "접수 완료") 0 else 1


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType==orderIng){
            OrderingViewHolder(context,parent).apply {
                itemView.order_list_static_map.setOnClickListener({
                    context.startActivity(Intent(context,OrderHistoryMapActivity::class.java).apply {
                        putExtra("latlng", LatLng(responses[adapterPosition].restaurant.lat, responses[adapterPosition].restaurant.lng))
                        putExtra("rest_name", responses[adapterPosition].restaurant.rest_name)
                        putExtra("address", responses[adapterPosition].restaurant.address)
                    })

                })
            }
        } else{
            CompleteOrderViewHolder(LayoutInflater.from(parent?.context ?:context).inflate(R.layout.item_order_history_complete, parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val orderItem = responses[position]

        if(holder is OrderingViewHolder){
            holder.bind(orderItem)
        }else if(holder is OrderHistoryAdapter.CompleteOrderViewHolder){
            holder.bind(orderItem)
        }
    }


    override fun getItemCount(): Int = responses.size

    inner class CompleteOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener(View.OnClickListener {  })

        }

        fun bind(orderResponse: OrderResponse){
            Picasso.with(context).load(orderResponse.restaurant.picture).transform(CropCircleTransformation()).into(itemView.order_list_image)
            itemView.order_list_rest_name.text= orderResponse.restaurant.rest_name
//            if(orderResponse.cartItems.size>1){
//                itemView.order_list_menu_name.text=orderResponse.cartItems[0].menuCategory.menu_name+" 외 " +(orderResponse.cartItems.size-1)
//            }else if(orderResponse.cartItems.size==1){
//                itemView.order_list_menu_name.text=orderResponse.cartItems[0].menuCategory.menu_name
//            }

            itemView.order_list_date.text= orderResponse.created_at
            itemView.order_list_state.text= orderResponse.status

        }



    }
}