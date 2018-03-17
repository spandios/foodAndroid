package com.example.fooddeuk.restaurant.near

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.list.RestaurantAdapter
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.StartActivity
import com.example.fooddeuk.util.toast
import kotlinx.android.synthetic.main.fragment_rest_list.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


/**
 * Created by heo on 2017. 10. 14..
 */


class NearRestaurantListFragment : android.support.v4.app.Fragment(), NearRestaurantListContract.View {
    private lateinit var queryMap: HashMap<String, String>
    lateinit var presenter: NearRestaurantListPresenter

    companion object {
        fun newInstance(queryMap: HashMap<String, String>): NearRestaurantListFragment {
            return NearRestaurantListFragment().apply {
                arguments = Bundle().apply { putSerializable("queryMap", queryMap) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_rest_list, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        queryMap = arguments?.getSerializable("queryMap") as HashMap<String, String>
//        var user = RealmUtil.findData(User::class.java)

    }

    override fun onResume() {
        super.onResume()
        presenter = NearRestaurantListPresenter().apply { view = this@NearRestaurantListFragment }
        presenter.setRecyclerView(queryMap)

    }
    override fun onPause() {
        super.onPause()
        presenter.clear()
    }

    override fun setRecyclerView(restaurantList: ArrayList<Restaurant>) {
        with(recycle_restaurant) {
            LayoutUtil.RecyclerViewSetting(context, this)
            setHasFixedSize(true)
            OverScrollDecoratorHelper.setUpOverScroll(this, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
            adapter = RestaurantAdapter(context, restaurantList).apply {
                restaurantItemClickListener = {
                    RxBus.intentPublish(RxBus.DetailRestaurantActivityData, it)
                    StartActivity(DetailRestaurantActivity::class.java)
                }
            }
        }
    }

    override fun showErrorRestaurantList() {
        toast("매장 리스트를 가져올 수 없습니다.")
    }
}
