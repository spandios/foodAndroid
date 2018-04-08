package com.example.fooddeuk.restaurant.home.presenter

import com.example.fooddeuk.BasePresenter
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.repository.RestaurantRepository
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 3. 17..
 */


interface HomeRestaurantContract {
    interface View {
        fun startLoading()
        fun stopLoading()
        fun setAddressText(gudong: String)
        fun setRestaurantRecyclerView(restaurantList: ArrayList<Restaurant>?)
        fun updateRestaurant(restaurantList: ArrayList<Restaurant>?)
        fun errorAddress()
        fun errorRestaurant()
        fun restaurantNull()
    }

    interface PresenterInterface : BasePresenter {
        var view: View
        fun setRecyclerView(queryMap: HashMap<String, String>)
        fun updateRestaurantByCurrentLocation(queryMap: HashMap<String, String>, locationName : String)
        fun updateRestaurantByMenuCategory(queryMap: HashMap<String, String>)
        fun refreshRestaurant()


    }
}


class HomeRestaurantPresenterInterface : HomeRestaurantContract.PresenterInterface {

    override var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override lateinit var view: HomeRestaurantContract.View

    override fun updateRestaurantByCurrentLocation(queryMap: HashMap<String, String>, locationName: String) {
        updateRestaurant(queryMap)
        view.setAddressText(locationName)
    }

    override fun refreshRestaurant() {
        RestaurantRepository.refresh()
    }

    override fun setRecyclerView(queryMap: HashMap<String, String>) {
        Logger.d(queryMap["foodType"])

        RestaurantRepository.getRestaurantAndCached(queryMap)?.subscribe({ result ->
            if (result.status == "SUCCESS") {
                if (result.restaurants.isNotEmpty()) {
                    view.setRestaurantRecyclerView(result.restaurants)
                }else{
                    view.setRestaurantRecyclerView(null)
                }
            }else{
                view.setRestaurantRecyclerView(null)
            }
        }, {
            view.setRestaurantRecyclerView(null)
            view.errorRestaurant()
            it.printStackTrace()
        })?.let {
            compositeDisposable.add(it)
        }
    }

    override fun updateRestaurantByMenuCategory(queryMap: HashMap<String, String>) {
        view.startLoading()
        updateRestaurant(queryMap)
    }


    fun updateRestaurant(queryMap: HashMap<String, String>){
        view.startLoading()
        refreshRestaurant()
        RestaurantRepository.getRestaurantAndCached(queryMap)?.subscribe({ result ->
            if(result.restaurants!=null){
                view.updateRestaurant(result.restaurants)
            }else{
                view.restaurantNull()
                view.updateRestaurant(null)
            }
        }, {
            view.errorRestaurant()
            it.printStackTrace()
        })?.let {
            compositeDisposable.add(it)
        }
    }
}


