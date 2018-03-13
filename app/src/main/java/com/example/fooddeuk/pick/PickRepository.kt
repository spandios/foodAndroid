package com.example.fooddeuk.pick

import com.example.fooddeuk.BaseRepository
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.user.User
import com.example.fooddeuk.util.RealmUtil
import io.reactivex.Single
import java.util.*


/**
 * Created by heo on 2018. 3. 11..
 */

abstract class BasePickRepository : BaseRepository() {
    var user: User? = null
}


object DangolRepository : BasePickRepository() {
    var cachedDangolList: Single<List<Restaurant>>? = null


    fun getDangol(callback: (dangolList: Single<List<Restaurant>>) -> Unit) {
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


object HotRepository : BaseRepository(){

    var cachedHotList : Single<List<Restaurant>>?=null

    fun getHot(callback: (hotList: Single<List<Restaurant>>) -> Unit){
        if(cachedHotList!=null&&!isDirty){
            callback(cachedHotList!!)
        }

        HTTP.single(httpService.getHotRestaurant(Location.lat,Location.lng)).let{
            isDirty=false
            cachedHotList=it
            callback(it)
        }
    }

}

