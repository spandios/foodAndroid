package com.example.fooddeuk.order_history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.order.model.OrderResponse
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.orhanobut.logger.Logger
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
            OrderHistoryViewHolder(context, parent).apply {
                itemView.order_list_static_map.setOnClickListener({
                    context.StartActivity(RxBus.OneRestaurantMapData,responses[adapterPosition].restaurant._id,OrderHistoryMapActivity::class.java)
                })
            }
        } else{
            CompleteOrderViewHolder(LayoutInflater.from(parent?.context ?:context).inflate(R.layout.item_order_history_complete, parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val orderItem = responses[position]
        if(holder is OrderHistoryViewHolder){
            holder.bind(orderItem)
        }else if(holder is CompleteOrderViewHolder){
            holder.bind(orderItem)
        }
    }


    override fun getItemCount(): Int = responses.size

    inner class CompleteOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener(View.OnClickListener {  })

        }

        fun bind(orderResponse: OrderResponse){
            itemView.txt_rest_name.text= orderResponse.restaurant.rest_name
            itemView.order_list_date.text= orderResponse.created_at
            itemView.order_list_state.text= orderResponse.status

        }



    }
}