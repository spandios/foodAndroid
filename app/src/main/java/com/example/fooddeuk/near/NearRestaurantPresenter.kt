package com.example.fooddeuk.near

import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.network.HTTP
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

class NearRestaurantPresenter : NearRestaurantContract.Presenter{
    override lateinit var view: NearRestaurantContract.View
    private var compositeDisposable = CompositeDisposable()


    override fun getLocation(lat: Double, lng: Double) {
        compositeDisposable.add(HTTP.Single(GlobalApplication.httpService.getLocationNameByNaver("$lng,$lat")).subscribe({
            view.setAddressText(it.gudong)
            refreshRestaurant()
            Location.locationName = it.gudong
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