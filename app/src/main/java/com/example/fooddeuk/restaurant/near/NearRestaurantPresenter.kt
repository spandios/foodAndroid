package com.example.fooddeuk.restaurant.near

import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.restaurant.repository.RestaurantRepository
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 2. 27..
 */

interface NearRestaurantContract{
    interface View{
        fun updateViewPager()
        fun setAddressText(gudong: String)
        fun showAddressError()

    }
    interface Presenter{
        var view : View
        fun getLocation(lat: Double, lng: Double)
        fun refreshRestaurant()
        fun clear()
    }
}

class NearRestaurantPresenter : NearRestaurantContract.Presenter {
    override lateinit var view: NearRestaurantContract.View
    private var compositeDisposable = CompositeDisposable()


    override fun getLocation(lat: Double, lng: Double) {
        compositeDisposable.add(HTTP.Single(httpService.getLocationNameByNaver("$lng,$lat")).subscribe({
            view.setAddressText(it.gudong)
            Location.locationName = it.gudong
            refreshRestaurant()
        }, {
            view.showAddressError()
            it.printStackTrace()
        }))
    }

    override fun refreshRestaurant() {
        RestaurantRepository.refresh()
        view.updateViewPager()
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}