package com.example.fooddeuk.home


import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.LocationSettingByMapActivity
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.location.Location
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.jakewharton.rxbinding2.view.RxView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_home.*


//, AppBarLayout.OnOffsetChangedListener
class HomeFragment : Fragment(), NestedScrollView.OnScrollChangeListener{
    private lateinit var locationSettingDialog: MaterialDialog
    private var mParallaxImageHeight: Int =0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?)  {
        super.onActivityCreated(savedInstanceState)
        mParallaxImageHeight=resources.getDimensionPixelSize(R.dimen.parallax_image_height)
        setToolbar()
        home_scroll.setBackgroundColor(ContextCompat.getColor(context!!,R.color.white))
        home_scroll.setOnScrollChangeListener(this)
        locationSetting()
        locationNameFromMapBus()
//        initEventImagePage()
    }

    private fun initEventImagePage() {
        val list: ArrayList<String> = ArrayList<String>()
        list.add("https://fooddeuk.s3.ap-northeast-2.amazonaws.com/restpicture1515821529579_restpicture1514973793525_f34ead4bf3213a370d7ca6a685d1bb07.jpg")
//        home_event_viewpager_indicator.setViewPager(home_event_viewpager.apply { adapter = ImageViewPager(context, list) })
    }

    private fun locationNameFromMapBus(){
        RxBus.subscribe { o ->
            if((o as HashMap<*, *>).containsKey(R.string.rx_location_setting)){
                if(RxBus.getRxObjectBool(getString(R.string.rx_location_setting),0)){
                    header_address_text.text=Location.locationName
                }
            }
        }
    }

    private fun setToolbar(){
        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        toolbar.background.alpha=0
        //검색 이미지 색깔 흰색
        header_search.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_search)?.apply {
            setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        })

    }


    private fun locationSetting() {
        header_address_text.text = Location.locationName
        header_address_text.setOnClickListener({ locationSettingDialog.show() })
        header_address_text.let{
            it.text=Location.locationName
            RxView.clicks(it)
        }
        val onClickListener = { v: View ->
            when (v.id) {
                //현재위치에서 재 검색
                R.id.btn_set_location_by_gps ->  {
                    //TODO Custom Alert Dialog
                    (activity as MainActivity).let {
                        it.startLoading()
                        Location.buzy=true
                        Location.getLocation { lat, lng ->
                            Location.buzy=false
                            it.stopLoading()
                            header_address_text.text=Location.getLocationName(lat,lng)
                        }
                    }
                }
                //지도에서 직접 위치 지정
                R.id.btn_set_location_by_map -> { StartActivity(LocationSettingByMapActivity::class.java) }

                R.id.btn_location_cancel -> {}
            }

            locationSettingDialog.dismiss()
        }
        //Dialog
        locationSettingDialog = MaterialDialog.Builder(context!!).customView(R.layout.dialog_current_location, true).build().apply {
            view.findViewById<Button>(R.id.btn_set_location_by_gps).setOnClickListener(onClickListener)
            view.findViewById<Button>(R.id.btn_set_location_by_map).setOnClickListener(onClickListener)
            view.findViewById<TextView>(R.id.btn_location_cancel).setOnClickListener(onClickListener)
        }
    }

    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        val alpha = (Math.min(1f, scrollY.toFloat() / (mParallaxImageHeight-toolbar.height)))*255
        image.translationY=(scrollY/2).toFloat()
        when (alpha) {
            in 0..19 -> {
                header_address_text.setTextColor(Color.WHITE)
                header_search.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            }
            in 20..40 -> header_search.setColorFilter(R.color.white_1, PorterDuff.Mode.SRC_ATOP)

            in 41..70 -> {
                header_address_text.setTextColor(ContextCompat.getColor(context!!, R.color.white_2))
                header_search.setColorFilter(R.color.white_2, PorterDuff.Mode.SRC_ATOP)
            }
            in 71..100 -> {
                header_address_text.setTextColor(Color.BLACK)
                header_search.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP)
            }
        }
        toolbar.background.alpha = alpha.toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("destroy")
        //PagerViewPager이므로 Destroy되지 않고 저장됨
    }


}
