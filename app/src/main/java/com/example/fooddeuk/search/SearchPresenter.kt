package com.example.fooddeuk.search

import com.example.fooddeuk.BasePresenter
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.restaurant.model.Restaurant
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 4. 6..
 */
interface SearchContract {
    interface View {
        fun noSearchResult()
        fun errorSearch()
        fun setSearchRV(restaurants: ArrayList<Restaurant>)
    }

    interface PresenterInterface : BasePresenter {
        val view: View
        fun clear()
        fun getSearchResult(searchText: String)
    }

}


class SearchPresenter : SearchContract.PresenterInterface {

    override var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override lateinit var view: SearchContract.View


    override fun getSearchResult(searchText: String) {
        compositeDisposable.add(HTTP.httpService.searchRestaurant(Location.lat, Location.lng, searchText).compose(HTTP.singleAsync())
                .subscribe({
                    if (it.status == "SUCCESS") {
                        if (it.restaurants.isNotEmpty()) {

                            view.setSearchRV(it.restaurants)
                        }
                    } else {
                        view.noSearchResult()
                    }
                }, {
                    view.errorSearch()
                    it.printStackTrace()
                }))
    }

    override fun clear() {
        compositeDisposable.clear()
    }


}