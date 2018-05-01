package com.example.fooddeuk.restaurant.detail

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.cart.CartActivity
import com.example.fooddeuk.custom.ImageVPAdapter
import com.example.fooddeuk.menu.RestMenuFragment
import com.example.fooddeuk.menu.listview.DetailRestaurantVP
import com.example.fooddeuk.network.HTTP.completable
import com.example.fooddeuk.network.HTTP.completeAsync
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.order_history.OrderHistoryMapActivity
import com.example.fooddeuk.restaurant.detail.detail.DetailRestaurantMoreDetailFragment
import com.example.fooddeuk.restaurant.detail.review.DetailRestaurantReviewFragment
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.user.User
import com.example.fooddeuk.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ogaclejapan.smarttablayout.SmartTabLayout
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_restaurant.*


/**from NearRestaurantFragment */

class DetailRestaurantActivity : AppCompatActivity(), DetailRestaurantContract.View, OnMapReadyCallback {

    private var dangolCnt = 0
    private var isDangol = false
    var topImageHeight = 0
    var vpMainHeight = 0 //->menu scroll base
    var mainTabHeight = 0
    var nameHeight = 0
    private var menuTabLayout = 0.0
    private var mainTabLayout = 0.0
    private var toolbarIconAlpha = 0
    var isTabFake = false
    var isMenuTab = true

    private lateinit var googleMap: GoogleMap
    lateinit var restaurant: Restaurant
    var user: User? = null
    private lateinit var imageVPAdapter: ImageVPAdapter
    lateinit var fakeTab: SmartTabLayout
    private lateinit var presenter: DetailRestaurantPresenter
    lateinit var scrollView: NestedScrollView
    private lateinit var mainTabListener: TabLayout.OnTabSelectedListener
    private lateinit var fakeTabListener: TabLayout.OnTabSelectedListener
    lateinit var reviewFramnet : DetailRestaurantReviewFragment

    private val cart by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_cart)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }
    private val heart by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_heart)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }
    private val backArrow by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_back_black)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_restaurant)

        header.background.alpha = 0
        val mapFragment = supportFragmentManager.findFragmentById(R.id.rest_detail_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        user = RealmUtil.findData(User::class.java)
        RxBus.intentSubscribe(RxBus.DetailRestaurantActivityData, this.javaClass, Consumer { it ->
            if (it is Restaurant) {
                restaurant = it
                reviewFramnet= DetailRestaurantReviewFragment.newInstance(restaurant._id)
                addFragmentToActivity(R.id.review_fragment,reviewFramnet)
                dangolCnt = restaurant.dangolCnt
                isDangol()
                presenter = DetailRestaurantPresenter().apply { view = this@DetailRestaurantActivity }
                presenter.pictureViewPager(restaurant._id)
                scrollView = scroll_rest_detail
                viewSetting()
                setViewPager()
            }
        })
    }

    override fun onStop() {
        super.onStop()

        if (isDangol) {
            user?.let {
                if (!user?.rest_id?.contains(restaurant.rest_id)!!) {
                    RealmUtil.beginTranscation {
                        user?.rest_id?.add(restaurant.rest_id)
                        true
                    }
                }
                httpService.createDangol(it.user_id, restaurant.rest_id).compose(completeAsync()).subscribe({

                }, { it.printStackTrace() })
            }
        } else {
            user?.let {
                if (user?.rest_id?.contains(restaurant.rest_id)!!) {
                    RealmUtil.beginTranscation {
                        user?.rest_id?.remove(restaurant.rest_id)
                        true
                    }
                }
                completable(httpService.deleteDangol(it.user_id, restaurant.rest_id)).subscribe({}, { it.printStackTrace() })
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this.javaClass)
        presenter.clear()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap.apply {
            setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(restaurant.lat, restaurant.lng), 17f)) //현재위치
            googleMap.addMarker(MarkerOptions().position(LatLng(restaurant.lat, restaurant.lng)).title(restaurant.name).snippet(restaurant.address)).showInfoWindow()
            setMinZoomPreference(17f)
            setMaxZoomPreference(18f)
        }
    }

    private fun setViewPager() {
        //Restaurant Menu Fragment
        scrollView = scroll_rest_detail
        fakeTab = tab_rest_menu_fake
        RxBus.intentPublish(RxBus.RestMenuFragmentData, restaurant)
        RxBus.intentPublish(RxBus.RestaurantMoreDetail, restaurant)
        DetailRestaurantVP(this,restaurant).let {
//            vp_main.initPageChangeListener()
            vp_main.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    if(position==2){
                        review_fragment.visible()
                    }else{
                        review_fragment.gone()
                    }
                    vp_main.onRefresh()

                }
            })

            vp_main.offscreenPageLimit=2
            vp_main.adapter = it
            tab_main.setupWithViewPager(vp_main)
            tab_rest_main_fake.setupWithViewPager(vp_main)
            setTabListener()
        }

    }


//식당 상단 사진 뷰페이저
override fun setPictureViewPager(pictureList: ArrayList<String>) {
    imageVPAdapter = ImageVPAdapter(this@DetailRestaurantActivity, pictureList)
    vp_rest_detail_image.adapter = imageVPAdapter
    rest_detail_image_viewpager_indicator.setViewPager(vp_rest_detail_image)
}

private fun isDangol() {
    user?.let {
        it.rest_id.forEach {
            if (it == restaurant.rest_id) {
                isDangol = true
                rest_detail_heart?.setColorFilter(ContextCompat.getColor(this@DetailRestaurantActivity, R.color.heart), PorterDuff.Mode.SRC_ATOP)
                return
            }
        }
    }
}

private fun viewSetting() {

    //뷰가 다 그려진 뒤 스크롤에 필요한 값 구하기
    val mGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            //리스너삭제
            scroll_rest_detail.viewTreeObserver.removeOnGlobalLayoutListener(this)
            topImageHeight = vp_rest_detail_image.height - header.height
            nameHeight = rest_detail_name_in_list.measuredHeight + 24.toPx + topImageHeight
            vpMainHeight = (vp_main.y).toInt() - header.height
            mainTabHeight = tab_main.realY() - header.height
        }
    }

    scroll_rest_detail.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

    setSupportActionBar(header)
    rest_detail_name.text = restaurant.name
    rest_detail_name_in_list.text = restaurant.name
    rest_detail_rating.text = Util.stringFormat(this, R.string.rest_rating_and_review_cnt, restaurant.rating.toString(), restaurant.reviewCnt.toString())

    //finish
    with(rest_detail_back) {
        setImageDrawable(backArrow)
        setOnClickListener { finish() }
    }

    //좋아요
    with(rest_detail_heart) {
        setImageDrawable(heart)
        setOnClickListener {
            user?.let {
                isDangol = !isDangol
                if (isDangol) {
                    dangolCnt++
                    Snackbar.make(layout_detail_restaurant, "단골등록", Snackbar.LENGTH_SHORT).show()
                    rest_detail_heart?.setColorFilter(ContextCompat.getColor(this@DetailRestaurantActivity, R.color.heart), PorterDuff.Mode.SRC_ATOP)
                } else {
                    dangolCnt--
                    Snackbar.make(layout_detail_restaurant, "단골해제", Snackbar.LENGTH_SHORT).show()
                    var color = 0
                    when (toolbarIconAlpha) {
                        in 0..130 -> {
                            color = R.color.white
                        }
                        in 130..160 -> {
                            color = (R.color.white_1)
                        }
                        in 161..190 -> {
                            color = (R.color.white_2)
                        }
                        in 190..255 -> {
                            color = (R.color.black)
                        }
                        else -> {
                        }
                    }

                    rest_detail_heart?.setColorFilter(ContextCompat.getColor(this@DetailRestaurantActivity, color), PorterDuff.Mode.SRC_ATOP)
                }
            }
                    ?: Snackbar.make(layout_detail_restaurant, "로그인해주세요", Snackbar.LENGTH_SHORT).show()
        }
    }

    //장바구니
    with(rest_detail_cart) {
        setImageDrawable(cart)
        setOnClickListener { StartActivity(CartActivity::class.java) }
    }

    //MapIcon
    img_rest_detail_map.setOnClickListener {
        RxBus.intentPublish(RxBus.OneRestaurantMapData, restaurant._id)
        StartActivity(OrderHistoryMapActivity::class.java)
    }

    scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
        vp_rest_detail_image.translationY = (scrollY / 2).toFloat()
        toolbarIconAlpha = ((Math.min(1f, scrollY.toFloat() / topImageHeight)) * 255).toInt()
        mainTabLayout = (scrollY.toDouble() / mainTabHeight)
        menuTabLayout = (scrollY.toDouble() / vpMainHeight)
        //매장이름
        if (nameHeight > scrollY) {
            rest_detail_name.gone()
        } else {
            rest_detail_name.visible()
        }

        if (mainTabLayout > 0.99) {
            if (!isMenuTab) {
                isTabFake = true
                layout_tab_rest_main_fake.visible()
            }
        } else {
            isTabFake = false
            layout_tab_rest_main_fake.gone()
        }

        //페이크 메뉴 탭 레이아웃
        if (menuTabLayout > 0.99) {
            if (isMenuTab) {
                tab_rest_menu_fake.visible()
            }
        } else {
            tab_rest_menu_fake.gone()
        }

        //툴바 아이콘 칼러
        when (toolbarIconAlpha) {
            in 0..130 -> {
                setIconChangeColor(R.color.white)
            }
            in 130..160 -> {
                setIconChangeColor(R.color.white_1)
            }
            in 161..190 -> {
                setIconChangeColor(R.color.white_2)
            }
            in 190..255 -> {
                setIconChangeColor(R.color.black)
            }
        }

        header.background.alpha = toolbarIconAlpha
    })
}

private fun setIconChangeColor(colorResource: Int) {
    backArrow?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
    cart?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
    if (!isDangol) {
        heart?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
    }
}

override fun showTopPictureError() {
    toast("image error")
}

private fun setTabListener() {
    mainTabListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            if (!isTabFake) {
                when (tab.position) {
                    0 -> {
                        isMenuTab = true
                        rest_map_parent.visible()
                        tab_rest_main_fake.getTabAt(0)?.select()

                    }
                    1 -> {
                        isMenuTab = false
                        rest_map_parent.visible()
                        tab_rest_main_fake.getTabAt(1)?.select()

                    }
                    2 -> {
                        isMenuTab = false
//                        rest_map_parent.gone()
                        tab_rest_main_fake.getTabAt(2)?.select()
                    }
                }
            }
        }

    }
    fakeTabListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            if (isTabFake) {
                when {
                    tab.position == 0 -> {
                        isMenuTab = true
                        rest_map_parent.visible()
                        tab_main.getTabAt(0)?.select()
                        scroll_rest_detail.scrollTo(0, mainTabHeight)
                    }
                    tab.position == 1 -> {
                        isMenuTab = false
                        rest_map_parent.visible()
                        tab_main.getTabAt(1)?.select()
                        scroll_rest_detail.scrollTo(0, mainTabHeight)
                    }

                    tab.position == 2 -> {
                        rest_map_parent.gone()
                        isMenuTab = false
                        tab_main.getTabAt(2)?.select()
                        scroll_rest_detail.scrollTo(0, mainTabHeight)

                    }
                }
            }

        }
    }
    tab_main.addOnTabSelectedListener(mainTabListener)
    tab_rest_main_fake.addOnTabSelectedListener(fakeTabListener)
}


inner class MainViewPager(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> RestMenuFragment.newInstance()
            1 -> DetailRestaurantMoreDetailFragment.newInstance()
            2 -> {
                vp_main.gone()
                DetailRestaurantMoreDetailFragment.newInstance()
            }
            else->RestMenuFragment.newInstance()
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "메뉴"
            1 -> "상세정보"
            else -> "리뷰"
        }
    }


}
}

