package com.example.fooddeuk.restaurant.near

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
        fun updateRestaurantByLocation(locationName : String)
        fun refreshRestaurant()
        fun clear()
    }
}

class NearRestaurantPresenter : NearRestaurantContract.Presenter {
    override lateinit var view: NearRestaurantContract.View
    private var compositeDisposable = CompositeDisposable()


    override fun updateRestaurantByLocation(locationName: String) {
            view.setAddressText(locationName)
            refreshRestaurant()

    }

    override fun refreshRestaurant() {
        RestaurantRepository.refresh()
        view.updateViewPager()
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}