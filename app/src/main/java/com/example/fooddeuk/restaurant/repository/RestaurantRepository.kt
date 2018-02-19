package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.model.restaurant.RestaurantResponse

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRepository : RestaurantRepositoryInterface {

    val restaurantRemoteRepository = RestaurantRemoteRepository


    override fun getNearRestaurantList(queryMap: HashMap<String, String>, callback: (err: Throwable?, result: RestaurantResponse?) -> Unit) {
        restaurantRemoteRepository.getNearRestaurantList(queryMap,{err, result -> callback(err,result) })
    }
}