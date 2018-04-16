package com.example.fooddeuk.restaurant.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalVariable.recentRestaurant
import com.example.fooddeuk.`object`.GlobalVariable.tempRecentRestaurant
import com.example.fooddeuk.restaurant.model.Restaurant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_restaurant.view.*
import java.util.*

/**
 * Created by heojuyeong on 2017. 10. 9..
 */

class RestaurantAdapter(private val context: Context, var restaurantItem: ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {
    lateinit var restaurantItemClickListener: (restaurant: Restaurant) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false))

        //Restaurant Detail Activity
        viewHolder.itemView.setOnClickListener({
            val restaurant = restaurantItem[viewHolder.adapterPosition]
            addRecentRestaurant(restaurant)
            restaurantItemClickListener(restaurant)

        })

        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = restaurantItem[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = restaurantItem.size


    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(restaurant: Restaurant) {
            with(itemView) {
                Picasso.with(context).load(restaurant.picture[0]).fit().into(img_restaurant_list)
                txt_restaurant_name_list.text = restaurant.name
                star_restaurant_list.rating = restaurant.rating
                txt_restaurant_reviewCnt_list.text = "리뷰(${restaurant.reviewCnt})"

                if (restaurant.discount.isNotEmpty()) {
                    txt_restaurant_sale_list.text = "포장 시 최대 -${restaurant.discount}"
                } else txt_restaurant_sale_list.visibility = View.GONE
            }

        }

    }


    fun updateRestaurant(restaurant: ArrayList<Restaurant>?) {
        restaurantItem.clear()
        restaurant?.let { restaurantItem.addAll(it) }
        notifyDataSetChanged()
    }

    private fun addRecentRestaurant(restaurant: Restaurant) {
        if (recentRestaurant.size > 4) {
            tempRecentRestaurant[4] = recentRestaurant[3]
            tempRecentRestaurant[3] = recentRestaurant[2]
            tempRecentRestaurant[2] = recentRestaurant[1]
            tempRecentRestaurant[1] = recentRestaurant[0]
            tempRecentRestaurant[0] = restaurant
            recentRestaurant = tempRecentRestaurant
            return
        }

        recentRestaurant.add(restaurant)

    }
}
