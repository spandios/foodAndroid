package com.example.fooddeuk.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.fooddeuk.R
import com.example.fooddeuk.model.review.MenuReview
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.rx.RxBus.intentSubscribe
import com.example.fooddeuk.util.logger
import io.reactivex.functions.Consumer

class ReviewActivity : AppCompatActivity() {
    private var reviewList = ArrayList<MenuReview>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        intentSubscribe(RxBus.ReviewActivityData,this@ReviewActivity.javaClass, Consumer {
            reviewList=it as ArrayList<MenuReview>
            logger(reviewList)
        })



    }


    override fun onDestroy() {

        super.onDestroy()
        RxBus.intentUnregister(this@ReviewActivity.javaClass)

    }
}
