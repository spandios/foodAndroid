package com.example.fooddeuk.fragment


import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.settingLocationMapActivity
import com.example.fooddeuk.adapter.ImageViewPager
import com.example.fooddeuk.util.IntentUtil
import com.example.fooddeuk.util.Location
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private lateinit var locationSettingDialog: MaterialDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar()
        locationSetting()
        initEventImagePage()
    }


    private fun initEventImagePage() {
        val list: ArrayList<String> = ArrayList<String>()
        list.add("https://fooddeuk.s3.ap-northeast-2.amazonaws.com/restpicture1515821529579_restpicture1514973793525_f34ead4bf3213a370d7ca6a685d1bb07.jpg")
        home_event_viewpager_indicator.setViewPager(home_event_viewpager.apply { adapter = ImageViewPager(context, list) })
    }
    private fun toolbar(){
        //init
        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        //searchIcon white color
        home_header_search.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_search)?.apply {
            setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        })

        //Transparent toolbar
        appbar.addOnOffsetChangedListener(this)

    }

    private fun locationSetting() {
        header_address_text.text = Location.getLocationName()
        header_address_text.setOnClickListener({ locationSettingDialog.show() })
        val onClickListener = { v: View ->
            when (v.id) {
                //현재위치에서 재 검색
                R.id.btn_get_location ->  header_address_text.text = Location.getLocationName()
                //지도에서 직접 위치 지정
                R.id.btn_map_get_location_ -> {
                    locationSettingDialog.dismiss()
                    IntentUtil.startActivity(activity, settingLocationMapActivity::class.java)
                }
                //닫기
                R.id.btn_location_cancel -> locationSettingDialog.dismiss()
            }
        }
        locationSettingDialog = MaterialDialog.Builder(context!!).customView(R.layout.dialog_current_location, true).build().apply {
            view.findViewById<Button>(R.id.btn_get_location).setOnClickListener(onClickListener)
            view.findViewById<Button>(R.id.btn_map_get_location_).setOnClickListener(onClickListener)
            view.findViewById<TextView>(R.id.btn_location_cancel).setOnClickListener(onClickListener)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val percentage = (((Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange)) * 255).toInt()
        when (percentage) {
            in 0..19 -> {
                header_address_text.setTextColor(Color.WHITE)
                home_header_search.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            }
            in 20..40 -> {
                header_address_text.setTextColor(ContextCompat.getColor(context!!, R.color.white_1))
                home_header_search.setColorFilter(R.color.white_1, PorterDuff.Mode.SRC_ATOP)
            }
            in 41..70 -> {
                header_address_text.setTextColor(ContextCompat.getColor(context!!, R.color.white_2))
                home_header_search.setColorFilter(R.color.white_2, PorterDuff.Mode.SRC_ATOP)
            }
            in 71..100 -> {
                header_address_text.setTextColor(Color.BLACK)
                home_header_search.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP)
            }
        }
        toolbar.background.alpha = percentage
    }

    override fun onDestroy() {
        super.onDestroy()
        //PagerViewPager이므로 Destroy되지 않고 저장됨
    }


}
