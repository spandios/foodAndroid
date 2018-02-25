package com.example.fooddeuk.model.review

/**
 * Created by heo on 2018. 2. 24..
 */
data class ReviewResponse(val result : ArrayList<MenuReview>, val success : Boolean)

data class MenuReview(var user_id : Int,var name : String, var user_image : String, var review_id : Int, var content : String, var rating : String, var image : String, var created_at : String)
