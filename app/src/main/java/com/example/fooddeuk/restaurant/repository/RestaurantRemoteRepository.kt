package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.`object`.GlobalApplication.httpService
import com.example.fooddeuk.restaurant.model.RestaurantResponse
import com.example.fooddeuk.network.HTTP.Single
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRemoteRepository : RestaurantDataSource {

    override fun getNearRestaurantList(queryMap: HashMap<String, String>) : Single<RestaurantResponse> {
        return Single(httpService.getCurrentLocationRestaurant(queryMap))
    }

    override fun getRestaurantImage(_id: String): Single<ArrayList<String>> {
        return Single(httpService.getPicture(_id))
    }
}