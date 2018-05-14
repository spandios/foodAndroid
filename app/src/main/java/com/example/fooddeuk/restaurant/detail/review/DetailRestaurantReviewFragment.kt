package com.example.fooddeuk.restaurant.detail.review

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import com.example.fooddeuk.util.setting
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.detail_restaurant_review.*

class DetailRestaurantReviewFragment : Fragment() {

    private var mRestaurantId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mRestaurantId= arguments!!.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detail_restaurant_review, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        httpService.getReviewByRestaurant(mRestaurantId).compose(singleAsync()).bindToLifecycle(this).subscribe({
            if(it.success){
                review_rv.setting(DetailRestaurantReviewAdapter(context!!, it.result))
            }
        },{
            it.printStackTrace()
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){

        }
    }


    companion object {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailRestaurantReviewFragment.
         */

        fun newInstance(param1: String): DetailRestaurantReviewFragment {
            val fragment = DetailRestaurantReviewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
