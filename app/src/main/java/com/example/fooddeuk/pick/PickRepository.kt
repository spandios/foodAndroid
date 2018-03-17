package com.example.fooddeuk.pick

import com.example.fooddeuk.BaseRepository
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.user.User
import com.example.fooddeuk.util.RealmUtil
import io.reactivex.Single


/**
 * Created by heo on 2018. 3. 11..
 */

abstract class BasePickRepository : BaseRepository() {
    var user: User? = null
}


object DangolRepository : BasePickRepository() {
    var cachedDangolList: Single<ArrayList<Restaurant>>? = null

    fun getDangol(callback: (dangolList: Single<ArrayList<Restaurant>>) -> Unit) {
        user = RealmUtil.findData(User::class.java)
        if (cachedDangolList != null && !isDirty) {
            callback(cachedDangolList!!)
        }

        user?.let {
            val restList = ArrayList<String>().apply { addAll(it.rest_id) }
            val dangolListRx = HTTP.single(httpService.getDangolRestaurant(restList))
            isDirty = false
            cachedDangolList = dangolListRx
            callback(dangolListRx)
        }
    }
}

object HotMenuRepository : BaseRepository() {
    var cachedHotMenuList: Single<List<Menu>>? = null

    fun getHotMenu(callback: (hotList: Single<List<Menu>>) -> Unit) {
        if (cachedHotMenuList != null && !isDirty) {
            callback(cachedHotMenuList!!)
        }

        HTTP.single(httpService.getHotMenu(Location.lat, Location.lng)).let {
            isDirty = false
            cachedHotMenuList = it
            callback(it)
        }
    }
}

object HotRestaurantRepository : BaseRepository() {

    var cachedHotRestaurantList: Single<List<Restaurant>>? = null

    fun getHotRestaurant(callback: (hotList: Single<List<Restaurant>>) -> Unit) {
        if (cachedHotRestaurantList != null && !isDirty) {
            callback(cachedHotRestaurantList!!)
        }

        HTTP.single(httpService.getHotRestaurant(Location.lat,Location.lng)).let{
            isDirty=false
            cachedHotRestaurantList = it
            callback(it)
        }
    }

}

