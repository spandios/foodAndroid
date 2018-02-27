package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.GlobalApplication.httpService
import com.example.fooddeuk.model.restaurant.RestaurantResponse
import com.example.fooddeuk.network.HTTP.Single
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRemoteRepository : RestaurantDataSource {

    override fun getNearRestaurantList(queryMap: HashMap<String, String>) : Single<RestaurantResponse> {
        return Single(httpService.getCurrentLocationRestaurant(queryMap))
    }
}