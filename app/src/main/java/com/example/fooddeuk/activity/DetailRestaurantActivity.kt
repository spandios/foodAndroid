package com.example.fooddeuk.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.R
import com.example.fooddeuk.adapter.restaurantImageVPAdapter
import com.example.fooddeuk.fragment.RestMenuFragment
import com.example.fooddeuk.home.HomeFragment
import com.example.fooddeuk.listview.menu.MenuListAdapter
import com.example.fooddeuk.model.menu.MenuCategory
import com.example.fooddeuk.model.restaurant.Restaurant
import com.example.fooddeuk.network.HTTP.Single
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.example.fooddeuk.util.addFragmentToActivity
import com.example.fooddeuk.util.replaceFragmentToActivity
import com.example.fooddeuk.util.toPx
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_restaurant.*


/**from NearRestaurantFragment */

class DetailRestaurantActivity : AppCompatActivity(), MenuListAdapter.OnItemClickListener, MenuListAdapter.OnCartCountClickListener, View.OnClickListener {
    var scrollFirstToolbar = 0
    var scrollSecondToolbar = 0 //->menu scroll base
    var menuItemHeight = 0
    var nameHeight = 0
    lateinit var restaurant: Restaurant
    lateinit var restMenuFragment: RestMenuFragment
    lateinit var homeFragment: HomeFragment
    lateinit var restaurantImageVPAdapter: restaurantImageVPAdapter
    lateinit var menuCategoryList: ArrayList<MenuCategory>
    lateinit var fakeTab : SmartTabLayout

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
        fakeTab=findViewById(R.id.tab_rest_menu_fake)

        val mGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //TODO View가 다 그려진 뒤에 값 구하기
                //리스너삭제
                scroll_rest_detail.viewTreeObserver.removeOnGlobalLayoutListener(this)
                scrollFirstToolbar = vp_rest_detail_image.height - toolbar.height
                nameHeight=rest_detail_name2.measuredHeight + 24.toPx +scrollFirstToolbar
                scrollSecondToolbar = (frame_rest_main.y).toInt() - toolbar.height


            }
        }

        scroll_rest_detail.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

        menuItemHeight = resources.getDimensionPixelOffset(R.dimen.menu_item)
        RxBus.intentSubscribe(RxBus.DetailRestaurantActivityData, this.javaClass, Consumer { it ->
            if (it is Restaurant) {
                restaurant = it
                menuCategoryList = restaurant.menuCategory
                setFragment()
                viewSetting()
            }
        })


        with(tab_rest_main) {
            tabMode = TabLayout.MODE_FIXED
            tabGravity = TabLayout.GRAVITY_FILL
            addTab(this.newTab().setText("메뉴"), true)
            addTab(this.newTab().setText("스토리"))
            addTab(this.newTab().setText("후기"))
            setSelectedTabIndicatorHeight(0)
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
    }

    private fun setFragment() {
        restMenuFragment = RestMenuFragment.newInstance(restaurant)
        homeFragment = HomeFragment()
        addFragmentToActivity(R.id.frame_rest_main, restMenuFragment)
        addFragmentToActivity(R.id.frame_rest_main, homeFragment)
        replaceFragmentToActivity(R.id.frame_rest_main, restMenuFragment)

    }


    //Implement CartAddClick -> 메뉴 추가 한 뒤 장바구니 개수 새로고침
    override fun onCartCount(cartSize: Int) {
        //        rest_detail_cart_count.setText(String.valueOf(cartSize));
    }

    //TODO 더 안정화
    //Implement ItemClick -> 해당 position의 상세 레이아웃 펼쳐짐
    override fun onItemClick(view: RecyclerView.ViewHolder, y: Float, menuItemHeight: Int) {
        Logger.d(view.itemView.y)

    }

    override fun onClick(v: View) {
        when (v.id) {

        }//            case R.id.rest_detail_back_btn:
        //                finish();
        //                break;
        //
        //            case R.id.detailTopCartButton:
        //                IntentUtil.startActivity(this, CartActivity.class);
        //                break;
        //            case R.id.rest_detail_cart_btn:
        //                IntentUtil.startActivity(this, CartActivity.class);
        //                break;
    }

    override fun onResume() {
        super.onResume()
        //        rest_detail_cart_count.setText(String.valueOf(RealmUtil.getDataSize(CartItem.class)));

    }


    private fun viewSetting() {
        setSupportActionBar(toolbar)
        //식당 대표 사진
        Single(GlobalApplication.httpService.getPicture(restaurant._id)).bindToLifecycle(this).subscribe({
            restaurantImageVPAdapter = restaurantImageVPAdapter(this@DetailRestaurantActivity, it)
            vp_rest_detail_image.adapter = restaurantImageVPAdapter
            rest_detail_image_viewpager_indicator.setViewPager(vp_rest_detail_image)
        }, { it.printStackTrace() })
        rest_detail_name.text=restaurant.name
        rest_detail_name2.text = restaurant.name
        rest_detail_rating!!.text = java.lang.Double.toString(restaurant.getRating().toDouble()) + "(${restaurant.reviewCnt})"
        rest_detail_name.setTextColor(ContextCompat.getColor(this, R.color.white))
        rest_detail_back.setImageDrawable(backArrow)
        rest_detail_back.setOnClickListener { finish() }
        rest_detail_heart.setImageDrawable(heart)
        rest_detail_cart.setImageDrawable(cart)
        rest_detail_cart.setOnClickListener {
            RxBus.intentPublish(3, restaurant)
            StartActivity(SerachActivity::class.java)
        }

        scroll_rest_detail.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//            logger("Y", scrollY)

            val toolbarIconAlpha = ((Math.min(1f, scrollY.toFloat() / scrollFirstToolbar)) * 255).toInt()
            val nameAlpha = (Math.min(1f, scrollY.toFloat() / nameHeight)).toInt()
            val tabAlpha = (Math.min(1f, scrollY.toFloat() / scrollSecondToolbar)).toInt()


            //매장이름
            if(nameAlpha==1){
                rest_detail_name.visibility=View.VISIBLE
            }else{
                rest_detail_name.visibility=View.GONE
            }

            //페이크 탭 레이아웃
            if (tabAlpha==1) {
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
                    rest_detail_name.setTextColor(ContextCompat.getColor(applicationContext, R.color.white_1))
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

    override fun onDestroy() {
        RxBus.intentUnregister(this.javaClass)
        super.onDestroy()
    }


}
