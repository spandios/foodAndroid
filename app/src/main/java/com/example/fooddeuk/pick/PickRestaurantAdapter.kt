package com.example.fooddeuk.pick

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_dangol.view.*


class PickAdapter(val context: Context, var dangols : List<Restaurant>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_dangol,parent,false)
        return DangolViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val restaurant = dangols[position]
        if(holder is DangolViewHolder){
            holder.bind(restaurant)
        }

    }

    override fun getItemCount() : Int{
        if(dangols.size>4){
            return 4
        }
        return dangols.size
    }

}

class DangolViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    fun bind(restaurant: Restaurant){
        with(itemView){
            setOnClickListener {
                RxBus.intentPublish(RxBus.DetailRestaurantActivityData,restaurant)
                (context as MainActivity).StartActivity(DetailRestaurantActivity::class.java)
            }
            Picasso.with(context).load(restaurant.picture[0]).fit().into(dangol_item_image)
            dangol_item_name.text=restaurant.name
            dangol_item_rating_start.rating=restaurant.rating
            dangol_item_review_cnt.text= Util.StringFormat(context,R.string.reviewcnt,restaurant.reviewCnt.toString())
            dangol_item_sale.text = "포장 시 최대 -${restaurant.discount}"
        }
    }
}