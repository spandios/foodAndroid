package com.example.fooddeuk.home

import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.repository.RestaurantRepository
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 2. 27..
 */


interface HomeContract {
    interface View {
        fun setAddressText(locationName: String)
        fun setHomeEventAdapter(eventPictureList : HomeEventPictureResponse)
        fun setDangolRestaurant(dangolRestaurants: ArrayList<Restaurant>)
        fun showAddressError()
        fun showEventError()
        fun dangolError()
    }

    interface Presenter {
        var view: View
        fun setAddress()
        fun getLocation(lat: Double, lng: Double)
        fun setHomeEvent()
        fun getDangolRestaurant()
        fun clear()
    }
}


class HomePresenter : HomeContract.Presenter {
    override lateinit var view: HomeContract.View
    var restaurantRepository = RestaurantRepository
    var compositeDisposable = CompositeDisposable()

    override fun getDangolRestaurant() {
        restaurantRepository.getDangolRestaurant()?.subscribe({
            view.setDangolRestaurant(it)
        }, {
            view.dangolError()
            it.printStackTrace()
        })
    }

    override fun setAddress() {
        view.setAddressText(Location.locationName)
    }

    override fun getLocation(lat: Double, lng: Double) {
        compositeDisposable.add(HTTP.single(httpService.getLocationNameByNaver("$lng,$lat")).subscribe({
            view.setAddressText(it.gudong)
            Location.locationName = it.gudong
        }, {
            view.showAddressError()
            it.printStackTrace()
        }))
    }

    override fun setHomeEvent() {
        compositeDisposable.add(EventPictureRepository.getEventPicture().subscribe({
            view.setHomeEventAdapter(it)
        },{
            view.showEventError()
            it.printStackTrace()
        }))
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}