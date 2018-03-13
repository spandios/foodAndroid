package com.example.fooddeuk.pick

import com.example.fooddeuk.BasePresenter
import com.example.fooddeuk.restaurant.model.Restaurant
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 3. 11..
 */


interface PickContract {
    interface View {
        //RecyclerView
        fun setDangolRV(dangolList: List<Restaurant>)
        fun setHotRV(hotList : List<Restaurant>)

        //Layoutsetting
        fun noDangolList()
        fun dangolMore(dangolList: List<Restaurant>)

        fun noHotList()
        fun hotMore()


        //error
        fun showErrorDangol()
        fun showErrorRecommand()
        fun showErrorHot()
    }

    interface Presenter : BasePresenter {
        var view: View
        fun getDangol()
        fun getHot()

    }

}


class PickPresenter: PickContract.Presenter {
    private val DANGOL = 1
    private val HOT = 2
    private val RECOMMAND = 3

    override var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override lateinit var view: PickContract.View



    override fun getDangol() {
        DangolRepository.getDangol {
            it.subscribe({
                rvLayoutSetting(DANGOL,it)
                view.setDangolRV(it)
            }, {
                view.showErrorDangol()
                it.printStackTrace()
            }).let {
                addDisposable(it)
            }
        }
    }

    override fun getHot() {
        HotRepository.getHot {
            it.subscribe({
                rvLayoutSetting(HOT,it)
                view.setHotRV(it)
            },{ view.showErrorHot()
                it.printStackTrace()}).let{addDisposable(it)}
        }
    }



    private fun rvLayoutSetting(version : Int,restaurantList : List<Restaurant>){
        when(version){
            DANGOL->{
                when {
                    restaurantList.isEmpty() -> {
                        view.noDangolList()
                        return
                    }
                    restaurantList.size > 4 -> //4개 이상일 시 추가 버튼
                        view.dangolMore(restaurantList)
                }
            }

            HOT->{
                when{
                    restaurantList.isEmpty()->{
                        view.noHotList()
                        return
                    }
                    restaurantList.size>4->{
                        view.hotMore()
                    }

                }

            }
            RECOMMAND->{

            }
        }
    }

}








