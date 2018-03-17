package com.example.fooddeuk.pick

import com.example.fooddeuk.BasePresenter
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.restaurant.model.Restaurant
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by heo on 2018. 3. 11..
 */


interface PickContract {
    interface View {
        //RecyclerView
        fun setDangolRV(dangolRestaurantList: List<Restaurant>)

        fun setHotMenuRV(hotList: List<Menu>)

        //dangol layout
        fun noDangolList()
        fun dangolMore(dangolList: List<Restaurant>)

        //hot_menu layout
        fun noHotMenuList()

        fun hotMenuMore()

        //error
        fun showErrorDangol()
        fun showErrorRecommand()
        fun showErrorHot()

    }

    interface Presenter : BasePresenter {
        var view: View
        fun getDangol()
        fun getHotMenu()

    }

}


class PickPresenter : PickContract.Presenter {
    private val DANGOL = 1
    private val RECOMMAND = 2

    override var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override lateinit var view: PickContract.View


    override fun getDangol() {
        DangolRepository.getDangol {
            it.subscribe({
                rvLayoutSetting(DANGOL, it)
                view.setDangolRV(it)
            }, {
                view.showErrorDangol()
                it.printStackTrace()
            }).let {
                addDisposable(it)
            }
        }
    }

    override fun getHotMenu() {
        HotMenuRepository.getHotMenu {
            it.subscribe({
                if (it.isEmpty()) {
                    view.noHotMenuList()
                } else if (it.size > 4) {
                    view.hotMenuMore()
                }
                view.setHotMenuRV(it)

            }, { it.printStackTrace() })
        }
    }


    private fun rvLayoutSetting(version: Int, restaurantList: List<Restaurant>) {
        when (version) {
            DANGOL -> {
                when {
                    restaurantList.isEmpty() -> {
                        view.noDangolList()
                        return
                    }
                    restaurantList.size > 4 -> //4개 이상일 시 추가 버튼
                        view.dangolMore(restaurantList)
                }
            }


            RECOMMAND -> {

            }
        }
    }

}








