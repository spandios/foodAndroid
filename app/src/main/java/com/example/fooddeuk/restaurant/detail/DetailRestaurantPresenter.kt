package com.example.fooddeuk.restaurant.detail

import com.example.fooddeuk.restaurant.repository.RestaurantRepository
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 2. 28..
 */

interface DetailRestaurantContract{
    interface View{
        fun setPictureViewPager(pictureList : ArrayList<String>)
        fun showTopPictureError()
    }
    interface Presenter{
        var view : View
        fun pictureViewPager(_id : String)
        fun clear()
    }
}


class DetailRestaurantPresenter : DetailRestaurantContract.Presenter{
    override lateinit var view : DetailRestaurantContract.View
    private var compositeDisposable = CompositeDisposable()
    override fun pictureViewPager(_id : String){
        RestaurantRepository.getRestaurantImage(_id)?.subscribe({view.setPictureViewPager(it)},{
            view.showTopPictureError()
            it.printStackTrace()
        })?.let { compositeDisposable.add(it) }
    }
    override fun clear() {
        compositeDisposable.clear()
    }

}