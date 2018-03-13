package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.single
import com.example.fooddeuk.restaurant.model.RestaurantResponse
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRemoteRepository : RestaurantDataSource {


    override fun getNearRestaurantList(queryMap: HashMap<String, String>) : Single<RestaurantResponse> {
        return single(httpService.getCurrentLocationRestaurant(queryMap))
    }

    override fun getRestaurantImage(_id: String): Single<ArrayList<String>> {
        return single(httpService.getPicture(_id))
    }
}