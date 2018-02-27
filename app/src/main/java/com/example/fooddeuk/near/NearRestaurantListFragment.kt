package com.example.fooddeuk.near

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.DetailRestaurantActivity
import com.example.fooddeuk.adapter.RestaurantAdapter
import com.example.fooddeuk.restaurant.repository.RestaurantRepository.getNearRestaurantList
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.StartActivity
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_rest_list.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import org.parceler.Parcels


/**
 * Created by heo on 2017. 10. 14..
 */


class NearRestaurantListFragment : android.support.v4.app.Fragment() {
    private lateinit var queryMap: HashMap<String, String>


    companion object {
        fun newInstance(queryMap: HashMap<String, String>): NearRestaurantListFragment {
            return NearRestaurantListFragment().apply {
                arguments = Bundle().apply { putParcelable("queryMap", Parcels.wrap<HashMap<String, String>>(queryMap)) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_rest_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            queryMap = Parcels.unwrap<HashMap<String, String>>(it.getParcelable("queryMap"))

            //매장 리사이클러뷰 셋팅
            getNearRestaurantList(queryMap).bindToLifecycle(this).subscribe(
                    { result ->
                        if (result.status == "SUCCESS") {
                            if (result.restaurants.size > 0) {
                                with(recycle_restaurant){
                                    LayoutUtil.RecyclerViewSetting(context, this)
                                    setHasFixedSize(true)
                                    OverScrollDecoratorHelper.setUpOverScroll(this, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
                                    adapter = RestaurantAdapter(activity!!, result.restaurants).apply {
                                        restaurantItemClickListener={
                                            RxBus.intentPublish(RxBus.DetailRestaurantActivityData,it)
                                            StartActivity(DetailRestaurantActivity::class.java)
                                        }
                                    }
                                }
                            }
                        }
                    }
            ) { throwable -> throwable.printStackTrace() }
        }
    }

}
