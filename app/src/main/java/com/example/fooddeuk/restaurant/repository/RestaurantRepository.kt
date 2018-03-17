package com.example.fooddeuk.restaurant.repository

import com.example.fooddeuk.pick.DangolRepository
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.model.RestaurantResponse
import com.orhanobut.logger.Logger
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 17..
 */
object RestaurantRepository : RestaurantDataSource {
    private var isMenuDirty = Array(7, { i -> true })
    private var isDangolDirty = true
    private var isImageDirty = true
    private var cachedAllMenu: Single<RestaurantResponse>? = null
    private var cachedJapaneseMenu: Single<RestaurantResponse>? = null
    private var cachedChickenMenu: Single<RestaurantResponse>? = null
    private var cachedChineseMenu: Single<RestaurantResponse>? = null
    private var cachedKoreanMenu: Single<RestaurantResponse>? = null
    private var cachedDessertMenu: Single<RestaurantResponse>? = null
    private var cachedCoffeeMenu: Single<RestaurantResponse>? = null
    private var restaurantImage = HashMap<String, Single<ArrayList<String>>>()
    private var cachedHotRestaurant: Single<ArrayList<Restaurant>>? = null
    var cachedDangolList: Single<ArrayList<Restaurant>>? = null

    override fun getDangolRestaurant(): Single<ArrayList<Restaurant>>? {

        if (cachedDangolList != null && !DangolRepository.isDirty) {
            return cachedDangolList!!
        } else {
            val dangolRestaurant = RestaurantRemoteRepository.getDangolRestaurant()
            DangolRepository.isDirty = false
            cachedDangolList = dangolRestaurant
            return dangolRestaurant

        }

    }

    override fun getRestaurantImage(_id: String): Single<ArrayList<String>>? {
        if (!isImageDirty && restaurantImage[_id] != null) {
            return restaurantImage[_id]
        }
        return RestaurantRemoteRepository.getRestaurantImage(_id).apply {
            isImageDirty = false
            restaurantImage[_id] = this
        }
    }

    override fun getNearRestaurantList(queryMap: HashMap<String, String>): Single<RestaurantResponse> {
        when (queryMap["foodType"]) {
            "" -> {
                if (!isMenuDirty[0] && cachedAllMenu != null) {
                    Logger.d("is cached allmenu")
                    return cachedAllMenu as Single<RestaurantResponse>
                }
            }
            "일식" -> {
                if (!isMenuDirty[1] && cachedJapaneseMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedJapaneseMenu as Single<RestaurantResponse>
                }
            }
            "치킨" -> {
                if (!isMenuDirty[2] && cachedChickenMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedChickenMenu as Single<RestaurantResponse>
                }

            }
            "중식" -> {
                if (!isMenuDirty[3] && cachedChineseMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedChineseMenu as Single<RestaurantResponse>
                }

            }
            "한식" -> {
                if (!isMenuDirty[4] && cachedKoreanMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedKoreanMenu as Single<RestaurantResponse>
                }

            }
            "디저트" -> {
                if (!isMenuDirty[5] && cachedDessertMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedCoffeeMenu as Single<RestaurantResponse>
                }

            }
            "커피" -> {
                if (!isMenuDirty[6] && cachedCoffeeMenu != null) {
                    Logger.d("is cached Japaness")
                    return cachedCoffeeMenu as Single<RestaurantResponse>
                }

            }

        }

        return getRestaurantAndCached(queryMap)!!
    }

    fun refresh() {
        for (i in isMenuDirty.indices) {
            isMenuDirty[i] = true
        }
    }

    fun imageRefresh() {
        isImageDirty = true
    }

    fun dangolRefresh() {
        isDangolDirty = true
    }

    fun getRestaurantAndCached(queryMap: HashMap<String, String>): Single<RestaurantResponse>? {
        when (queryMap["foodType"]) {

            "" -> {
                return RestaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedAllMenu = this
                    isMenuDirty[0] = false
                }
            }
            "일식" -> {
                return RestaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedJapaneseMenu = this
                    isMenuDirty[1] = false
                }
            }
            "치킨" -> {
                return RestaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedChickenMenu = this
                    isMenuDirty[2] = false
                }
            }
            "중식" -> {
                return RestaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedChineseMenu = this
                    isMenuDirty[3] = false
                }
            }
            "한식" -> {
                return RestaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedKoreanMenu = this
                    isMenuDirty[4] = false
                }
            }
            "디저트" -> {
                return RestaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedDessertMenu = this
                    isMenuDirty[5] = false
                }
            }
            "커피" -> {
                return RestaurantRemoteRepository.getNearRestaurantList(queryMap).apply {
                    cachedCoffeeMenu = this
                    isMenuDirty[6] = false
                }
            }
        }
        return null
    }


}