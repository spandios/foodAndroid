package com.example.fooddeuk.home

import com.example.fooddeuk.BasePresenter
import com.example.fooddeuk.`object`.GlobalVariable.recentRestaurant
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.single
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.repository.RestaurantRepository
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 2. 27..
 */


interface HomeContract {
    interface View {
        fun setAddressText(locationName: String)
        fun setHomeEventAdapter(eventPictureList : HomeEventPictureResponse)
        fun setRecentRestaurantRV(recentRestaurants : ArrayList<Restaurant>)
        fun setDangolRestaurantRV(dangolRestaurants: ArrayList<Restaurant>)
        fun showAddressError()
        fun showEventError()
        fun dangolError()

    }

    interface PresenterInterface : BasePresenter{
        var view : View
        fun setAddress()
        fun setLocationName(locationName: String)
        fun setHomeEvent()
        fun getDangolRestaurants()

        fun clear()
        fun getRecentResgaurants()
    }
}


class HomePresenterInterface : HomeContract.PresenterInterface {
    override lateinit var view : HomeContract.View
    override var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var restaurantRepository = RestaurantRepository

    override fun getDangolRestaurants() {
        restaurantRepository.getDangolRestaurant()?.subscribe({
            view.setDangolRestaurantRV(it)
        }, {
            view.dangolError()
            it.printStackTrace()
        })
    }

    override fun getRecentResgaurants() {
        if(recentRestaurant.size>0){
            view.setRecentRestaurantRV(recentRestaurant)
        }
    }

    override fun setAddress() {
        view.setAddressText(Location.locationName)
    }


    override fun setLocationName(locationName: String) {
        view.setAddressText(locationName)

    }

    override fun setHomeEvent() {
        single(httpService.homeEvent).subscribe({
        },{it.printStackTrace()})
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