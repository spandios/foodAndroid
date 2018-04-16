package com.example.fooddeuk.`object`

import com.example.fooddeuk.restaurant.model.Restaurant

/**
 * Created by heojuyeong on 2017. 9. 23..
 */

object GlobalVariable {

    //LOGIN

    var isLogin = false
    var provider = ""
    const val KAKAO = "KAKAO"
    const val NAVER = "NAVER"
    const val FACEBOOK = "FACEBOOK"


    //FILTER
    enum class FILTER {
        DISTANCE, RATING, DISCOUNT, REVIEW
    }

    var recentRestaurant = ArrayList<Restaurant>()
    var tempRecentRestaurant = ArrayList<Restaurant>()

    const val distance = 0
    const val rating = 1
    const val discount = 2
    const val dangol = 3
    const val review = 4

    const val distance3km = 3000
    const val distance6km = 6000
    const val distance9km = 9000

    enum class MENU {
        ANYTHING, JAPAN, CHICKEN, CHINESE, KOREAN, CAFE, THAI, FRANCHISE, DESSERT,
    }

    //location


    //restaurant


}
