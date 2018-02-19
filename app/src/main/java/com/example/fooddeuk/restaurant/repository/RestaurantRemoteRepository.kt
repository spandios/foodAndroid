package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.GlobalApplication.httpService
import com.example.fooddeuk.model.restaurant.RestaurantResponse
import com.example.fooddeuk.network.HTTP.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRemoteRepository : RestaurantRepositoryInterface {

    override fun getNearRestaurantList(queryMap: HashMap<String, String>, callback: (err: Throwable?, result: RestaurantResponse?) -> Unit) {
        Single(httpService.getCurrentLocationRestaurant(queryMap)).subscribe({
            callback(null,it)
        },{
            callback(it,null)
        })
    }
}