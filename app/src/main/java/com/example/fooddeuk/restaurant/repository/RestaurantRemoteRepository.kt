package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.`object`.GlobalApplication
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.single
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.model.RestaurantResponse
import com.example.fooddeuk.user.User
import com.example.fooddeuk.util.RealmUtil
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRemoteRepository : RestaurantDataSource {
    override fun getDangolRestaurant(): Single<ArrayList<Restaurant>>? {
        val user = RealmUtil.findData(User::class.java)

        user?.let {
            val userObject = GlobalApplication.getInstance().realm.copyFromRealm(it)
            val dangolList = ArrayList<String>().apply {
                addAll(userObject.rest_id)
            }

            return if (dangolList.isNotEmpty()) {
                single(httpService.getDangolRestaurant(dangolList))
            } else {
                null
            }

        } ?: return null
    }

    override fun getNearRestaurantList(queryMap: HashMap<String, String>) : Single<RestaurantResponse> {
        return single(httpService.getCurrentLocationRestaurant(queryMap))
    }

    override fun getRestaurantImage(_id: String): Single<ArrayList<String>> {
        return single(httpService.getPicture(_id))
    }
}