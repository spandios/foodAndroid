package com.example.fooddeuk.restaurant.detail

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.CartActivity
import com.example.fooddeuk.home.HomeFragment
import com.example.fooddeuk.menu.RestMenuFragment
import com.example.fooddeuk.menu.model.MenuCategory
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.*
import com.ogaclejapan.smarttablayout.SmartTabLayout
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_restaurant.*


/**from NearRestaurantFragment */

class DetailRestaurantActivity : AppCompatActivity(),  DetailRestaurantContract.View {
    var scrollFirstToolbar = 0
    var scrollSecondToolbar = 0 //->menu scroll base
    var menuItemHeight = 0
    var nameHeight = 0
    lateinit var restaurant: Restaurant
    private lateinit var restMenuFragment: RestMenuFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var restaurantImageVPAdapter: restaurantImageVPAdapter
    private lateinit var menuCategoryList: ArrayList<MenuCategory>
    lateinit var fakeTab: SmartTabLayout
    private lateinit var presenter: DetailRestaurantPresenter

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

        RxBus.intentSubscribe(RxBus.DetailRestaurantActivityData, this.javaClass, Consumer { it ->
            if (it is Restaurant) {
                restaurant = it
                presenter=DetailRestaurantPresenter().apply {view= this@DetailRestaurantActivity }
                presenter.pictureViewPager(restaurant._id)
                menuCategoryList = it.menuCategory
                setMainTabFragment()
                viewSetting()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this.javaClass)
        presenter.clear()
    }

    private fun setMainTabFragment() {
        restMenuFragment = RestMenuFragment.newInstance(restaurant)
        homeFragment = HomeFragment()
        addFragmentToActivity(R.id.frame_rest_main, restMenuFragment)
        addFragmentToActivity(R.id.frame_rest_main, homeFragment)
        replaceFragmentToActivity(R.id.frame_rest_main, restMenuFragment)

    }

    //식당 상단 사진 뷰페이저
    override fun setPictureViewPager(pictureList: ArrayList<String>) {
        restaurantImageVPAdapter = restaurantImageVPAdapter(this@DetailRestaurantActivity, pictureList)
        vp_rest_detail_image.adapter = restaurantImageVPAdapter
        rest_detail_image_viewpager_indicator.setViewPager(vp_rest_detail_image)
    }



    private fun viewSetting() {
        //뷰가 다 그려진 뒤 스크롤에 필요한 값 구하기
        val mGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //리스너삭제
                scroll_rest_detail.viewTreeObserver.removeOnGlobalLayoutListener(this)

                scrollFirstToolbar = vp_rest_detail_image.height - toolbar.height
                nameHeight = rest_detail_name_in_list.measuredHeight + 24.toPx + scrollFirstToolbar
                scrollSecondToolbar = (frame_rest_main.y).toInt() - toolbar.height
            }
        }

        scroll_rest_detail.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

        setSupportActionBar(toolbar)
        fakeTab = tab_rest_menu_fake

        //메인 탭설정
        with(tab_rest_main) {
            tabMode = TabLayout.MODE_FIXED
            tabGravity = TabLayout.GRAVITY_FILL
            addTab(this.newTab().setText("메뉴"), true)
            addTab(this.newTab().setText("스토리"))
            addTab(this.newTab().setText("후기"))
            setSelectedTabIndicatorHeight(0)
        }

        rest_detail_name.text = restaurant.name

        with(rest_detail_back) {
            setImageDrawable(backArrow)
            setOnClickListener { finish() }
        }
        with(rest_detail_heart){
            setImageDrawable(heart)
//            setOnClickListener{//TODO 좋아요}
        }
        with(rest_detail_cart){
            setImageDrawable(cart)
            setOnClickListener { StartActivity(CartActivity::class.java) }
        }

        rest_detail_name_in_list.text = restaurant.name
        rest_detail_rating.text = java.lang.Double.toString(restaurant.getRating().toDouble()) + "(${restaurant.reviewCnt})"
        scroll_rest_detail.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val toolbarIconAlpha = ((Math.min(1f, scrollY.toFloat() / scrollFirstToolbar)) * 255).toInt()
            val tabAlpha = (Math.min(1f, scrollY.toFloat() / scrollSecondToolbar)).toInt()
            //매장이름
            if (nameHeight > scrollY) {
                rest_detail_name.visibility = View.GONE
            } else {
                rest_detail_name.visibility = View.VISIBLE
            }

            //페이크 탭 레이아웃
            if (tabAlpha > 0.95) {
                tab_rest_menu_fake.visibility = View.VISIBLE
            } else {
                tab_rest_menu_fake.visibility = View.INVISIBLE
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

            toolbar.background.alpha = toolbarIconAlpha
        }

    }

    private fun setIconChangeColor(colorResource: Int) {
        backArrow?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
        cart?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
        heart?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
    }



    override fun showTopPictureError() {
        toast("image error")
    }
}


//
//        tab_rest_main.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                when (tab.position) {
//                    0 -> {
//                        showFragmentToActivity(restMenuFragment)
////                        hideFragmentToActivity(testFragment)
//                    }
//                    1 -> {
//                        hideFragmentToActivity(restMenuFragment)
////                        showFragmentToActivity(testFragment)
//                    }
//                    2 -> {
//                        hideFragmentToActivity(restMenuFragment)
////                        showFragmentToActivity(testFragment)
//                    }
//                }
//
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })