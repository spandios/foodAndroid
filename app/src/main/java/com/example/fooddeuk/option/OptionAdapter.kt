package com.example.fooddeuk.option

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.order.OptionForOrderViewHolder
import com.example.fooddeuk.order_history.OptionForOderHistoryViewHolder

/**
 * Created by heo on 2018. 3. 2..
 */

class OptionAdapter(private val optionList : ArrayList<CartOption>, private val necessary: Boolean =true, private val isOrderOption : Boolean = false,var isOrderHistory :Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var recyclerView : RecyclerView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(isOrderHistory){
            OptionForOderHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_option_for_order_history, parent, false))
        } else if(!isOrderOption){
            OptionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_option,parent,false))
        }else{
            OptionForOrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_option_for_order, parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val option = optionList[position]

            if(holder is OptionViewHolder){
                if(necessary){
                    holder.necessaryBindOption(option, position,{
                        selectPosition ->
                        for(i in 0 until recyclerView.adapter.itemCount){
                            recyclerView.getChildAt(i).isSelected=false
                        }
                        recyclerView.getChildAt(selectPosition).isSelected=true
                    })
                }else{
                    holder.unnecessaryBindOption(option)
                }
            }else if(holder is OptionForOrderViewHolder){
                holder.bind(option)

            }else if(holder is OptionForOderHistoryViewHolder){
                holder.bind(option)
            }

    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView=recyclerView
    }



}