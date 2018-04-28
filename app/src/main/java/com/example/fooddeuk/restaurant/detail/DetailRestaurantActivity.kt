package com.example.fooddeuk.restaurant.detail

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS
import android.view.ViewTreeObserver
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.cart.CartActivity
import com.example.fooddeuk.custom.ImageVPAdapter
import com.example.fooddeuk.menu.RestMenuFragment
import com.example.fooddeuk.menu.model.MenuCategory
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
import com.orhanobut.logger.Logger
import com.southernbox.springscrollview.SpringScrollView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_restaurant.*


/**from NearRestaurantFragment */

class DetailRestaurantActivity : AppCompatActivity(), DetailRestaurantContract.View, OnMapReadyCallback {

    var dangolCnt = 0
    var isDangol = false
    var scrollFirstToolbar = 0
    var scrollMenuTabLayout = 0 //->menu scroll base
    var scrollTablayout = 0
    var nameHeight = 0
    var menuTablayout = 0.0
    var otherTabLayout = 0
    private var toolbarIconAlpha = 0
    var isTabFake = false
    var isMenuTab = true

    private lateinit var googleMap: GoogleMap
    lateinit var restaurant: Restaurant
    var user: User? = null
    private lateinit var restMenuFragment: RestMenuFragment
    private lateinit var detailRestaurantReviewFragment: DetailRestaurantReviewFragment
    private lateinit var moreDetailFragment: DetailRestaurantMoreDetailFragment
    private lateinit var ImageVPAdapter: ImageVPAdapter
    private lateinit var menuCategoryList: ArrayList<MenuCategory>
    lateinit var fakeTab: SmartTabLayout
    private lateinit var presenter: DetailRestaurantPresenter
    lateinit var scrollView: SpringScrollView

    private val cart by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_cart)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }
    private val heart by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_heart)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }
    private val backArrow by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_back_black)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
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
                dangolCnt = restaurant.dangolCnt
                isDangol()
                presenter = DetailRestaurantPresenter().apply { view = this@DetailRestaurantActivity }
                presenter.pictureViewPager(restaurant._id)
                menuCategoryList = it.menuCategory
                scrollView = scroll_rest_detail
                scrollView.isFocusable=false
                scrollView.isFocusableInTouchMode=false
                scrollView.descendantFocusability=FOCUS_BLOCK_DESCENDANTS
                frame_rest_main.isFocusable=false
                frame_rest_main.isFocusableInTouchMode=false
                frame_rest_main.descendantFocusability= FOCUS_BLOCK_DESCENDANTS

                viewSetting()
                setMainTabFragment()
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

    private fun setMainTabFragment() {
        //Restaurant Menu Fragment
        scrollView = scroll_rest_detail
        RxBus.intentPublish(RxBus.RestMenuFragmentData, restaurant)
        RxBus.intentPublish(RxBus.RestaurantMoreDetail, restaurant)
        restMenuFragment = RestMenuFragment.newInstance()
        moreDetailFragment = DetailRestaurantMoreDetailFragment.newInstance()
        detailRestaurantReviewFragment = DetailRestaurantReviewFragment.newInstance(restaurant._id)
        addFragmentToActivity(R.id.frame_rest_main, restMenuFragment)
        addFragmentToActivity(R.id.frame_rest_main, moreDetailFragment)
        addFragmentToActivity(R.id.frame_rest_main, detailRestaurantReviewFragment)

        showFragmentToActivity(restMenuFragment)
        hideFragmentToActivity(moreDetailFragment)
        hideFragmentToActivity(detailRestaurantReviewFragment)

    }

    //식당 상단 사진 뷰페이저
    override fun setPictureViewPager(pictureList: ArrayList<String>) {
        ImageVPAdapter = ImageVPAdapter(this@DetailRestaurantActivity, pictureList)
        vp_rest_detail_image.adapter = ImageVPAdapter
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

    private fun setTabLayout() {
        //탭설정
        fakeTab = tab_rest_menu_fake
        with(tab_rest_main) {
            tabMode = TabLayout.MODE_FIXED
            tabGravity = TabLayout.GRAVITY_FILL
            addTab(this.newTab().setText("메뉴"), true)
            addTab(this.newTab().setText("상세정보"))
            addTab(this.newTab().setText("후기"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    if (!isTabFake) {
                        when {
                            tab.position == 0 -> hideFragmentToActivity(restMenuFragment)
                            tab.position == 1 -> hideFragmentToActivity(moreDetailFragment)
                            tab.position == 2 -> hideFragmentToActivity(detailRestaurantReviewFragment)
                        }
                    }

                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (!isTabFake) {
                        when {
                            tab.position == 0 -> {
                                isMenuTab = true
                                rest_map_parent.visible()
                                showFragmentToActivity(restMenuFragment)
                                tab_rest_main_fake.getTabAt(0)?.select()
                            }
                            tab.position == 1 -> {
                                isMenuTab = false
                                rest_map_parent.visible()
                                showFragmentToActivity(moreDetailFragment)
                                tab_rest_main_fake.getTabAt(1)?.select()
                            }
                            tab.position == 2 -> {
                                isMenuTab = false
                                rest_map_parent.gone()
                                showFragmentToActivity(detailRestaurantReviewFragment)
                                tab_rest_main_fake.getTabAt(2)?.select()

                            }
                        }
                    }

                }
            })

            setSelectedTabIndicatorHeight(0)
        }

        with(tab_rest_main_fake) {
            tabMode = TabLayout.MODE_FIXED
            tabGravity = TabLayout.GRAVITY_FILL
            addTab(this.newTab().setText("메뉴"), true)
            addTab(this.newTab().setText("상세정보"))
            addTab(this.newTab().setText("후기"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    if (isTabFake) {
                        when {
                            tab.position == 0 -> hideFragmentToActivity(restMenuFragment)
                            tab.position == 1 -> hideFragmentToActivity(moreDetailFragment)
                            tab.position == 2 -> hideFragmentToActivity(detailRestaurantReviewFragment)
                        }
                    }
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (isTabFake) {
                        when {
                            tab.position == 0 -> {
                                isMenuTab = true
                                rest_map_parent.visible()
                                showFragmentToActivity(restMenuFragment)
                                tab_rest_main.getTabAt(0)?.select()
                                scroll_rest_detail.scrollTo(0, scrollTablayout)
                            }
                            tab.position == 1 -> {
                                isMenuTab = false
                                rest_map_parent.visible()
                                showFragmentToActivity(moreDetailFragment)
                                tab_rest_main.getTabAt(1)?.select()
                                scroll_rest_detail.scrollTo(0, scrollTablayout)
                            }
                            tab.position == 2 -> {
                                isMenuTab = false
                                tab_rest_main.getTabAt(2)?.select()
                                rest_map_parent.gone()
                                showFragmentToActivity(detailRestaurantReviewFragment)
                                scroll_rest_detail.scrollTo(0, scrollTablayout)
                            }

                        }
                    }

                }
            })
            setSelectedTabIndicatorHeight(0)
        }
    }


    private fun viewSetting() {
        setTabLayout()

        //뷰가 다 그려진 뒤 스크롤에 필요한 값 구하기
        val mGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //리스너삭제
                scroll_rest_detail.viewTreeObserver.removeOnGlobalLayoutListener(this)
                scrollFirstToolbar = vp_rest_detail_image.height - header.height
                nameHeight = rest_detail_name_in_list.measuredHeight + 24.toPx + scrollFirstToolbar
                scrollMenuTabLayout = (frame_rest_main.y).toInt() - header.height
                scrollTablayout = tab_rest_main.realY() - header.height
            }
        }



        setSupportActionBar(header)
        rest_detail_name.text = restaurant.name

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

        rest_detail_name_in_list.text = restaurant.name
        rest_detail_rating.text = Util.stringFormat(this, R.string.rest_rating_and_review_cnt, restaurant.rating.toString(), restaurant.reviewCnt.toString())
        scroll_rest_detail.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            vp_rest_detail_image.translationY = (scrollY / 2).toFloat()
            toolbarIconAlpha = ((Math.min(1f, scrollY.toFloat() / scrollFirstToolbar)) * 255).toInt()
            menuTablayout = (scrollY.toDouble() / scrollMenuTabLayout)
            otherTabLayout = ((Math.min(1f, scrollY.toFloat() / scrollTablayout)) * 255).toInt()
            Logger.d(scrollY)
            //매장이름
            if (nameHeight > scrollY) {
                rest_detail_name.gone()
            } else {
                rest_detail_name.visible()
            }

            //페이크 메뉴 탭 레이아웃
            if (menuTablayout > 0.99) {
                if (isMenuTab) {
                    tab_rest_menu_fake.visible()
                }
            } else {
                tab_rest_menu_fake.gone()
            }

            if (otherTabLayout > 254) {
                if (!isMenuTab) {
                    isTabFake = true
                    layout_tab_rest_main_fake.visible()
                }
            } else {
                isTabFake = false
                layout_tab_rest_main_fake.gone()
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
        }

        scroll_rest_detail.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

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
}

