package com.example.fooddeuk.home


import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.custom.CustomFilterDialog
import com.example.fooddeuk.map.LocationSettingByMapActivity
import com.example.fooddeuk.restaurant.detail.restaurantImageVPAdapter
import com.example.fooddeuk.util.*
import kotlinx.android.synthetic.main.fragment_home.*


//, AppBarLayout.OnOffsetChangedListener
class HomeFragment : Fragment(), NestedScrollView.OnScrollChangeListener, HomeContract.View {
    private lateinit var locationSettingDialog: CustomFilterDialog
    private lateinit var homePresenter: HomePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        renderView()
    }

    override fun onResume() {
        super.onResume()
        setAddressText(Location.locationName)
        homePresenter = HomePresenter().apply {
            view = this@HomeFragment
        }
        homePresenter.setAddress()
        homePresenter.setHomeEvent()
    }

    override fun onPause() {
        super.onPause()
        homePresenter.clear()
    }

    private fun renderView() {
        setToolbar()
        with(home_scroll) {
            setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
            setOnScrollChangeListener(this@HomeFragment)
        }
        setLocationDialog()
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


    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        val alpha = (Math.min(1f, scrollY.toFloat() / (250.toPx - toolbar.height))) * 255
        when (alpha) {
            in 0..70 -> {
                txt_home_location_name.setTextColor(Color.WHITE)
                header_search.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            }
            in 70..100 -> header_search.setColorFilter(R.color.white_1, PorterDuff.Mode.SRC_ATOP)

            in 100..150 -> {
                txt_home_location_name.setTextColor(ContextCompat.getColor(context!!, R.color.white_2))
                header_search.setColorFilter(R.color.white_2, PorterDuff.Mode.SRC_ATOP)
            }
            in 150..255 -> {
                txt_home_location_name.setTextColor(Color.BLACK)
                header_search.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP)
            }
        }
        toolbar.background.alpha = alpha.toInt()
    }


    override fun setHomeEventAdapter(eventPictureList: HomeEventPictureResponse) {
        home_event_viewpager_indicator.setViewPager(home_event_viewpager.apply { adapter = restaurantImageVPAdapter(context, eventPictureList.eventPictureList) })
    }

    override fun setAddressText(locationName: String) {
        with(txt_home_location_name) {
            text = locationName
            setOnClickListener({ locationSettingDialog.show() })
        }
    }


    private fun setLocationDialog() {
        val dialogContent = arrayListOf("현재 위치 재설정", "vertical", "현재 위치 재검색", "지도에서 설정")
        locationSettingDialog = CustomFilterDialog.Builder(context!!).isClearText(false).contentTypeFace(Typeface.DEFAULT_BOLD).isFirstSelectColor(false).contentGravity(Gravity.CENTER).setFilter(dialogContent
                , { position, _ ->
            when (position) {
                0 -> {
                    //현재위치에서 재 검색
                    startLoading()
                    Location.buzy = true
                    Location.getLocation { lat, lng ->
                        Location.buzy = false
                        stopLoading()
                        homePresenter.getLocation(lat, lng)
                    }
                }
                1 -> {
                    StartActivity(LocationSettingByMapActivity::class.java)
                }
            }
            locationSettingDialog.dismiss()
        }).build()
    }

    override fun showAddressError() {
        toast("주소 정보를 얻을 수 없습니다.")
    }

    override fun showEventError() {
        toast("이벤트를 불러 올 수 없습니다")
    }


}



