package com.example.fooddeuk.restaurant.detail.review

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.review.MenuReview

/**
 * Created by heo on 2018. 4. 20..
 */
class DetailRestaurantReviewAdapter(val context : Context, var reviews: ArrayList<MenuReview>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            DetailRestaurantReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.item_detail_restaurant_review, parent, false))

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is DetailRestaurantReviewViewHolder){
            holder.bind(reviews[position])
        }
    }
}