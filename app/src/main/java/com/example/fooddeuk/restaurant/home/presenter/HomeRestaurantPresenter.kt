package com.example.fooddeuk.restaurant.home.presenter

import com.example.fooddeuk.BasePresenter
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 3. 17..
 */


interface HomeRestaurantContract {
    interface View {

    }

    interface Presenter : BasePresenter {

    }
}


class HomeRestaurantPresenter : HomeRestaurantContract.Presenter {

    override var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var view: HomeRestaurantContract.View


}