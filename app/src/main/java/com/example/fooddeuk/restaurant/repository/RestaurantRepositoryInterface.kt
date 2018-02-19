package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.model.restaurant.RestaurantResponse

/**
 * Created by heo on 2018. 2. 16..
 */
interface RestaurantRepositoryInterface {

    fun getNearRestaurantList(queryMap: HashMap<String,String>, callback : (err: Throwable?,result : RestaurantResponse?)->Unit)

}