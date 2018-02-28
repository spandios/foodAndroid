package com.example.fooddeuk.restaurant.near

import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.repository.RestaurantRepository
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 2. 27..
 */

interface NearRestaurantListContract{
    interface View{
        fun setRecyclerView(restaurantList : ArrayList<Restaurant>)
        fun showErrorRestaurantList()

    }
    interface Presenter{
        var view : View
        fun setRecyclerView(queryMap : HashMap<String,String>)
        fun clear()
    }
}

class NearRestaurantListPresenter : NearRestaurantListContract.Presenter {
    override lateinit var view: NearRestaurantListContract.View
    private var compositeDisposable = CompositeDisposable()

    override fun setRecyclerView(queryMap: HashMap<String, String>) {
        RestaurantRepository.getRestaurantAndCached(queryMap)?.subscribe({ result->
            if(result.status == "SUCCESS"){
                if(result.restaurants.isNotEmpty()){
                    view.setRecyclerView(result.restaurants)
                }
            }
        },{
            view.showErrorRestaurantList()
            it.printStackTrace()
        })
    }

    override fun clear() {
     compositeDisposable.clear()
    }
}