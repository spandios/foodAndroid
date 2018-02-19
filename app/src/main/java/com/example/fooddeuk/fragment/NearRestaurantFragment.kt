package com.example.fooddeuk.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.DetailRestaurantActivity
import com.example.fooddeuk.adapter.RestaurantAdapter
import com.example.fooddeuk.model.restaurant.Restaurant
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.StartActivity
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_rest_list.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import org.parceler.Parcels


/**
 * Created by heo on 2017. 10. 14..
 */


class NearRestaurantFragment : android.support.v4.app.Fragment() {
    private lateinit var queryMap: HashMap<String, String>


    companion object {
        fun newInstance(queryMap: HashMap<String, String>): NearRestaurantFragment {
            return NearRestaurantFragment().apply {
                arguments = Bundle().apply { putParcelable("queryMap", Parcels.wrap<HashMap<String, String>>(queryMap)) }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rest_list, container, false)
        arguments?.let {
            queryMap = Parcels.unwrap<HashMap<String, String>>(it.getParcelable("queryMap"))

            //해당 메뉴타입에 맞는 매장 리사이클러뷰 셋팅
            HTTP.Single(GlobalApplication.httpService.getCurrentLocationRestaurant(queryMap)).bindToLifecycle(this).subscribe(
                    { result ->
                        if (result.status == "SUCCESS") {
                            if (result.restaurants.size > 0) {
                                recycle_restaurant.let {
                                    it.setBackgroundColor(ContextCompat.getColor(context!!,R.color.white))
                                    LayoutUtil.RecyclerViewSetting(context, it)
                                    OverScrollDecoratorHelper.setUpOverScroll(it, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
                                    it.adapter = RestaurantAdapter(activity!!, result.restaurants).apply {
                                        setRestaurantItemClickListener(object : RestaurantAdapter.RestaurantItemClickListener{
                                            override fun onItemClickListener(restaurant: Restaurant) {
                                                StartActivity(DetailRestaurantActivity::class.java, Bundle().apply { putParcelable("restaurant", Parcels.wrap<Restaurant>(restaurant)) })
                                                Logger.d(restaurant.name + " 선택 ", restaurant.rest_id + "저장")
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    }
            ) { throwable -> throwable.printStackTrace() }
        }
        return view
    }

}
