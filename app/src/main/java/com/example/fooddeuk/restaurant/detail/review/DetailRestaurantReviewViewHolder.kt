package com.example.fooddeuk.restaurant.detail.review

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.custom.ImageVPAdapter
import com.example.fooddeuk.review.MenuReview
import com.example.fooddeuk.util.TimeUtil
import com.example.fooddeuk.util.gone
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_detail_restaurant_review.view.*

/**
 * Created by heo on 2018. 4. 20..
 */

class DetailRestaurantReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(reviewItem: MenuReview) {
        with(itemView) {
            if (reviewItem.user_image.isNotEmpty()) { Picasso.with(context).load(reviewItem.user_image).fit().into(review_profile_picture) }
            review_user_id.text = reviewItem.name
            review_write_date.text = TimeUtil.currentBetweenTime(reviewItem.created_at)
            review_rating.rating = reviewItem.rating.toFloat()
            if(reviewItem.image.isNotEmpty()){
                vp_review_image.adapter =ImageVPAdapter(context, reviewItem.image)
            }else{
                layout_review_image.gone()
            }
            review_image_indicator.setViewPager(vp_review_image)
            review_content.text = reviewItem.content
        }
    }
}