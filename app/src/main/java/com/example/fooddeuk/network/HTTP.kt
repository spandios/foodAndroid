package com.example.fooddeuk.network

import com.example.fooddeuk.common.CommonValueApplication.httpService
import com.example.fooddeuk.model.menu.ReviewItem
import com.example.fooddeuk.model.order.OrderResponse
import com.example.fooddeuk.model.restaurant.RestaurantResponse
import com.example.fooddeuk.model.user.User
import com.example.fooddeuk.model.user.UserResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiPredicate
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.*

/**
 * Created by heo on 2018. 2. 10..
 */
object HTTP {

    fun basic(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 1 && throwable is SocketTimeoutException }
    }

    fun socket(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 2 && throwable is SocketTimeoutException }
    }

    fun <T> Single(single: Single<T>): Single<T> {
        return single.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    //not emits value  ->  onComplete or onError
    fun Completable(completable: Completable): Completable {
        return completable.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> Flowable(flowable: Flowable<T>): Flowable<T> {
        return flowable.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    //User
    @JvmStatic
    fun getUser(provider_id: String): Single<UserResponse> = Single(httpService.getUser(provider_id))

    @JvmStatic
    fun createUser(user: User): Completable = Completable(httpService.createUser(user))

    @JvmStatic
    fun updateToken(provider_id: String, fcm_token: String): Completable = Completable(httpService.updateToken(provider_id, fcm_token))


    //Restaurant
    @JvmStatic
    fun getCurrentLocationRestaurant(curLat: Double, curLng: Double, maxDistance: Int, foodtype: String, filter: String, rest_name: String): Single<RestaurantResponse> =
            httpService.getCurrentLocationListItem(curLat, curLat, maxDistance, foodtype, filter, rest_name)

    @JvmStatic
    fun getRestaurantById(rest_id: String): Single<RestaurantResponse> = httpService.getRestaurantByRestId(rest_id)

    @JvmStatic
    fun getPicture(rest_id: String): Single<ArrayList<String>> = httpService.getPicture(rest_id)

    //Review
    @JvmStatic
    fun getReview(menu_id: String): Single<ArrayList<ReviewItem>>  = httpService.getReview(menu_id)


    //ORDER
    fun order(orderResponse: OrderResponse): Completable = httpService.order(orderResponse)

    fun getCurrentOrder(user_id: String): Single<ArrayList<OrderResponse>> = httpService.getCurrentOrder(user_id)



}