package com.example.fooddeuk.pick

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recent_restaurant.view.*


class RecentAdapter(val context: Context, var recentViewRestaurants : ArrayList<Restaurant>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_recent_restaurant,parent,false)
        return RecentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val restaurant = recentViewRestaurants[position]
        if(holder is RecentViewHolder){
            holder.bind(restaurant)
        }
    }

    override fun getItemCount() : Int{

        if(recentViewRestaurants.size>4){
            return 5
        }
        return recentViewRestaurants.size
    }
}

class RecentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    fun bind(restaurant: Restaurant){
        with(itemView){
            setOnClickListener {
                RxBus.intentPublish(RxBus.DetailRestaurantActivityData,restaurant)
                (context as MainActivity).StartActivity(DetailRestaurantActivity::class.java)
            }
            recent_name.text=restaurant.name
            Picasso.with(context).load(restaurant.picture[0]).fit().into(recent_img)

        }
    }
}