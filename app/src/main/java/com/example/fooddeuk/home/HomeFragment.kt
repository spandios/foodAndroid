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
import com.example.fooddeuk.GlobalApplication.httpService
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.activity.LocationSettingByMapActivity
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.network.HTTP.Single
import com.example.fooddeuk.util.StartActivity
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_home.*


//, AppBarLayout.OnOffsetChangedListener
class HomeFragment : Fragment(), NestedScrollView.OnScrollChangeListener {
    private lateinit var locationSettingDialog: MaterialDialog
    private var mParallaxImageHeight: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mParallaxImageHeight = resources.getDimensionPixelSize(R.dimen.parallax_image_height)
        setToolbar()
        home_scroll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
        home_scroll.setOnScrollChangeListener(this)
        locationSetting()


//        initEventImagePage()
    }

    private fun initEventImagePage() {
        val list: ArrayList<String> = ArrayList<String>()
        list.add("https://fooddeuk.s3.ap-northeast-2.amazonaws.com/restpicture1515821529579_restpicture1514973793525_f34ead4bf3213a370d7ca6a685d1bb07.jpg")
//        home_event_viewpager_indicator.setViewPager(home_event_viewpager.apply { adapter = ImageViewPager(context, list) })
    }


    private fun setToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        toolbar.background.alpha = 0
        //검색 이미지 -> 색깔 흰색
        header_search.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_search)?.apply {
            setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        })

    }


    private fun locationSetting() {
        with(txt_home_location_name) {
            text = Location.locationName
            setOnClickListener({ locationSettingDialog.show() })
        }

        val onClickListener = { v: View ->
            when (v.id) {
            //현재위치에서 재 검색
                R.id.btn_set_location_by_gps -> {
                    //TODO Custom Alert Dialog
                    (activity as MainActivity).let {
                        it.startLoading()
                        Location.buzy = true
                        Location.getLocation { lat, lng ->
                            Location.buzy = false
                            it.stopLoading()
                            Single(httpService.getLocationNameByNaver(lng.toString() + "," + lat.toString())).bindToLifecycle(this).subscribe({
                                txt_home_location_name.text = it.gudong
                                Location.locationName = it.gudong
                            }, { it.printStackTrace() })
                        }
                    }
                }
            //지도에서 직접 위치 지정
                R.id.btn_set_location_by_map -> {
                    StartActivity(LocationSettingByMapActivity::class.java)
                }

                R.id.btn_location_cancel -> {
                }
            }

            locationSettingDialog.dismiss()
        }
        //Dialog
        locationSettingDialog = MaterialDialog.Builder(activity!!).customView(R.layout.dialog_current_location, true).build().apply {
            view.findViewById<Button>(R.id.btn_set_location_by_gps).setOnClickListener(onClickListener)
            view.findViewById<Button>(R.id.btn_set_location_by_map).setOnClickListener(onClickListener)
            view.findViewById<TextView>(R.id.btn_location_cancel).setOnClickListener(onClickListener)
        }
    }

    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        val alpha = (Math.min(1f, scrollY.toFloat() / (mParallaxImageHeight - toolbar.height))) * 255
        image.translationY = (scrollY / 2).toFloat()
        when (alpha) {
            in 0..19 -> {
                txt_home_location_name.setTextColor(Color.WHITE)
                header_search.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            }
            in 20..40 -> header_search.setColorFilter(R.color.white_1, PorterDuff.Mode.SRC_ATOP)

            in 41..70 -> {
                txt_home_location_name.setTextColor(ContextCompat.getColor(context!!, R.color.white_2))
                header_search.setColorFilter(R.color.white_2, PorterDuff.Mode.SRC_ATOP)
            }
            in 71..255 -> {
                txt_home_location_name.setTextColor(Color.BLACK)
                header_search.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP)
            }
        }
        toolbar.background.alpha = alpha.toInt()
    }

    override fun onResume() {
        txt_home_location_name.text=Location.locationName
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
    }


}
