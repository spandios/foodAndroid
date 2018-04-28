package com.example.fooddeuk.restaurant.detail.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import io.reactivex.functions.Consumer

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailRestaurantReviewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailRestaurantReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailRestaurantMoreDetailFragment : Fragment() {

    private lateinit var restaurant: Restaurant


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_restaurant_more_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        RxBus.intentSubscribe(RxBus.RestaurantMoreDetail, DetailRestaurantMoreDetailFragment::class.java, Consumer {
            if (it is Restaurant) {
                restaurant = it

            }
        })

    }

    companion object {
        fun newInstance(): DetailRestaurantMoreDetailFragment = DetailRestaurantMoreDetailFragment()
    }


}
