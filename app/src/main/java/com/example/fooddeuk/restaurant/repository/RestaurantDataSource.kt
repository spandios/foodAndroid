package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.model.RestaurantResponse
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 16..
 */
interface RestaurantDataSource {


    fun getRestaurantList(queryMap: HashMap<String,String>) : Single<RestaurantResponse>

    fun getDangolRestaurant(): Single<ArrayList<Restaurant>>?

    fun getRestaurantImage(_id : String) : Single<ArrayList<String>>?

}

