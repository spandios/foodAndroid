package com.example.fooddeuk.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.adapter.ImageViewPager
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val list: ArrayList<String> = ArrayList<String>()
        list.add("https://fooddeuk.s3.ap-northeast-2.amazonaws.com/restpicture1515821529579_restpicture1514973793525_f34ead4bf3213a370d7ca6a685d1bb07.jpg")
        home_event_viewpager_indicator.setViewPager(home_event_viewpager.apply { adapter = ImageViewPager(context, list) })
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            //measuring for alpha
            val toolBarHeight = toolbar.measuredHeight
            val appBarHeight = appBarLayout.measuredHeight

            val totalScrollRange: Int = appBarLayout.totalScrollRange

            val percentage = (((Math.abs(verticalOffset).toFloat()  / appBarLayout.totalScrollRange))*255).toInt()
            Logger.d("vertical offset " + Math.abs(verticalOffset))
            Logger.d("total  " + appBarLayout.totalScrollRange)
            Logger.d("percentage : " +Math.abs(verticalOffset) +"/" + totalScrollRange)

//            var f = (totalScrollRange - verticalOffset / totalScrollRange - verticalOffset) * 255
            toolbar.getBackground().setAlpha(percentage)

            //alpha = 0 투명함

        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title=null


    }

    override fun onResume() {
        super.onResume()

    }


    override fun onDestroy() {
        super.onDestroy()
    }


}
