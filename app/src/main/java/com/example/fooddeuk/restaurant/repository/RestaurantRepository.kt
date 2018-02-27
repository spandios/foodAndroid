package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.model.restaurant.RestaurantResponse
import com.orhanobut.logger.Logger
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRepository : RestaurantDataSource {
    private var isDirty = Array(7, { i -> true })
    private var cachedAllMenu: Single<RestaurantResponse>? = null
    private var cachedJapaneseMenu: Single<RestaurantResponse>? = null
    private var cachedChickenMenu: Single<RestaurantResponse>? = null
    private var cachedChineseMenu: Single<RestaurantResponse>? = null
    private var cachedKoreanMenu: Single<RestaurantResponse>? = null
    private var cachedDessertMenu: Single<RestaurantResponse>? = null
    private var cachedCoffeeMenu: Single<RestaurantResponse>? = null
    private val restaurantRemoteRepository = RestaurantRemoteRepository


    override fun getNearRestaurantList(queryMap: HashMap<String, String>): Single<RestaurantResponse> {
        when (queryMap["foodType"]) {
            "" -> {
                if (!isDirty[0] && cachedAllMenu != null) {
                    Logger.d("is cached allmenu")
                    return cachedAllMenu as Single<RestaurantResponse>
                }
            }
            "일식" -> {
                if (!isDirty[1] && cachedJapaneseMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedJapaneseMenu as Single<RestaurantResponse>
                }
            }
            "치킨" -> {
                if (!isDirty[2] && cachedChickenMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedChickenMenu as Single<RestaurantResponse>
                }

            }
            "중식" -> {
                if (!isDirty[3] && cachedChineseMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedChineseMenu as Single<RestaurantResponse>
                }

            }
            "한식" -> {
                if (!isDirty[4] && cachedKoreanMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedKoreanMenu as Single<RestaurantResponse>
                }

            }
            "디저트" -> {
                if (!isDirty[5] && cachedDessertMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedCoffeeMenu as Single<RestaurantResponse>
                }

            }
            "커피" -> {
                if (!isDirty[6] && cachedCoffeeMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedCoffeeMenu as Single<RestaurantResponse>
                }

            }

        }

        return getRestaurantAndCached(queryMap)!!
    }

    fun refresh() {
        for(i in isDirty.indices){
            isDirty[i]=true
        }
    }

    fun getRestaurantAndCached(queryMap: HashMap<String, String>): Single<RestaurantResponse>? {
        Logger.d("it is refresh")
        when (queryMap["foodType"]) {

            "" -> {
                return restaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedAllMenu = this
                    isDirty[0] = false
                }
            }
            "일식" -> {
                return restaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedJapaneseMenu = this
                    isDirty[1] = false
                }
            }
            "치킨" -> {
                return restaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedChickenMenu = this
                    isDirty[2] = false
                }
            }
            "중식" -> {
                return restaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedChineseMenu = this
                    isDirty[3] = false
                }
            }
            "한식" -> {
                return restaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedKoreanMenu = this
                    isDirty[4] = false
                }
            }
            "디저트" -> {
                return restaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedDessertMenu = this
                    isDirty[5] = false
                }
            }
            "커피" -> {
                return restaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedCoffeeMenu = this
                    isDirty[6] = false
                }
            }
        }
        return null
    }

}