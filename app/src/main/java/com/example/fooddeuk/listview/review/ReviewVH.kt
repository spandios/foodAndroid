package com.example.fooddeuk.listview.review

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.model.review.MenuReview
import com.example.fooddeuk.util.TimeUtil
import kotlinx.android.synthetic.main.item_simple_review.view.*

/**
 * Created by heo on 2018. 2. 25..
 */

class ReviewSimpleVH(context: Context, parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_simple_review,parent,false)){
    fun bind(menuReview : MenuReview, size : Int, isMore : Boolean=true){
        when(isMore){
            true->{
                itemView.txt_more_review.visibility=VISIBLE
                itemView.txt_more_review.text="총 ${size}개의 리뷰 모두보기"
            }
            false->{itemView.txt_more_review.visibility=GONE
                with(itemView){
                    txt_reviewer_name.text = menuReview.name
                    txt_review_rating.text= menuReview.rating
                    txt_review_content.text = menuReview.content
                    txt_review_created_at.text = TimeUtil.currentBetweenTime(menuReview.created_at)
                }
            }
        }



    }
}