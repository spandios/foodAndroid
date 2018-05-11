package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.`object`.GlobalApplication
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.model.RestaurantResponse
import com.example.fooddeuk.user.User
import com.example.fooddeuk.util.RealmUtil
import com.orhanobut.logger.Logger
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRemoteRepository : RestaurantDataSource {
    override fun getDangolRestaurant(): Single<ArrayList<Restaurant>>? {
        val user = RealmUtil.findData(User::class.java)
        user?.let {
            val userObject = GlobalApplication.getInstance().realm.copyFromRealm(it)
            val dangolRestId = ArrayList<String>().apply {
                addAll(userObject.rest_id)
            }

            if (dangolRestId.isNotEmpty()) {
                return httpService.getDangolRestaurant(dangolRestId).compose(singleAsync())
            }else null

        } ?: return null
    }

    override fun getRestaurantList(queryMap: HashMap<String, String>) : Single<RestaurantResponse> {
        return httpService.getCurrentLocationRestaurant(queryMap).compose(singleAsync())
    }

    override fun getRestaurantImage(_id: String): Single<ArrayList<String>> {
        return httpService.getPicture(_id).compose(singleAsync())
    }
}