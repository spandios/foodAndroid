package com.example.fooddeuk.map

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_map_bottom_restaurant.view.*


/**
 * Created by heo on 2018. 4. 2..
 */

class MapBottomRestaurantAdapter(val context : Context, private val restaurantList : ArrayList<Restaurant>) : RecyclerView.Adapter<MapBottomRestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_map_bottom_restaurant,parent,false)
        val viewHodler=ViewHolder(view)
        viewHodler.itemView.setOnClickListener {
            context.StartActivity(RxBus.DetailRestaurantActivityData,restaurantList[viewHodler.adapterPosition],DetailRestaurantActivity::class.java)
        }
        return viewHodler
    }

    override fun getItemCount(): Int = restaurantList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.bind(restaurant)

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(restaurant : Restaurant){
            with(itemView){
                Picasso.with(context).load(restaurant.picture[0]).fit().into(img_restaurant_map)
                txt_restaurant_map_box_name.text=restaurant.name
                star_restaurant_map_box.rating=restaurant.rating
                txt_restaurant_map_box_reviewcnt.text = String.format(context.getString(R.string.reviewcnt), restaurant.reviewCnt.toString())

            }
        }
    }
}