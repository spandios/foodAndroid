package com.example.fooddeuk.review

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.ReviewActivity
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.example.fooddeuk.util.TimeUtil
import kotlinx.android.synthetic.main.item_simple_review.view.*

/**
 * Created by heo on 2018. 2. 25..
 */


class ReviewSimpleVH(context: Context, parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_simple_review, parent, false)) {
    fun bind(menuReview: MenuReview) {
        with(itemView) {
            txt_more_review.visibility = View.GONE
            txt_reviewer_name.text = menuReview.name
            txt_review_rating.text = menuReview.rating
            txt_review_content.text = menuReview.content
            txt_review_created_at.text = TimeUtil.currentBetweenTime(menuReview.created_at)
        }
    }

    fun bindMore(reviewList: ArrayList<MenuReview>) {
        with(itemView){
            itemView.setBackgroundColor(resources.getColor(R.color.tt2))
            txt_more_review.visibility = View.VISIBLE
            layout_review_content.visibility = GONE
            txt_more_review.text = "총 ${reviewList.size}개의 리뷰 모두보기"
            //리뷰 전체를 볼 수 있는 액티비티 생성
            txt_more_review.setOnClickListener{
                RxBus.intentPublish(RxBus.ReviewActivityData,reviewList)
                (context as AppCompatActivity).StartActivity(ReviewActivity::class.java)
            }
        }
    }
}