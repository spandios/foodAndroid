package com.example.fooddeuk.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.R
import com.example.fooddeuk.adapter.MenuListAdapter
import com.example.fooddeuk.adapter.restaurantImageVPAdapter
import com.example.fooddeuk.fragment.RestMenuCategoryFragment
import com.example.fooddeuk.model.restaurant.Restaurant
import com.example.fooddeuk.network.HTTP.Single
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.addFragmentToActivity
import com.example.fooddeuk.util.replaceFragmentToActivity
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_restaurant.*


/**from NearRestaurantFragment */

class DetailRestaurantActivity : AppCompatActivity(), MenuListAdapter.OnItemClickListener, MenuListAdapter.OnCartCountClickListener, View.OnClickListener {


    lateinit var restaurant: Restaurant
    lateinit var restMenuCategoryFragment: RestMenuCategoryFragment
    lateinit var restaurantImageVPAdapter: restaurantImageVPAdapter
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
        RxBus.intentSubscribe(RxBus.DetailRestaurantActivityData,this.javaClass, Consumer {
            it->if(it is Restaurant){
            restaurant=it
        }
        })
        //메뉴 프레그멘트 생성
        restMenuCategoryFragment = RestMenuCategoryFragment.newInstance(restaurant)

        viewSetting()

//        testFragment = HomeFragment()
//        addFragmentToActivity(R.id.rest_main_tab, testFragment)
        addFragmentToActivity(R.id.rest_main_tab, restMenuCategoryFragment)
        replaceFragmentToActivity(R.id.rest_main_tab, restMenuCategoryFragment)
//        rest_detail_main_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                when (tab.position) {
//                    0 -> {
//                        showFragmentToActivity(restMenuCategoryFragment)
////                        hideFragmentToActivity(testFragment)
//                    }
//                    1 -> {
//                        hideFragmentToActivity(restMenuCategoryFragment)
////                        showFragmentToActivity(testFragment)
//                    }
//                    2 -> {
//                        hideFragmentToActivity(restMenuCategoryFragment)
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


    //Implement CartAddClick -> 메뉴 추가 한 뒤 장바구니 개수 새로고침
    override fun onCartCount(cartSize: Int) {
        //        rest_detail_cart_count.setText(String.valueOf(cartSize));
    }

    //TODO 더 안정화
    //Implement ItemClick -> 해당 position의 상세 레이아웃 펼쳐짐
    override fun onItemClick(view: RecyclerView.ViewHolder, y: Float, recyclerView: RecyclerView) {
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
            rest_detail_image_viewpager.adapter = restaurantImageVPAdapter
            rest_detail_image_viewpager_indicator.setViewPager(rest_detail_image_viewpager)
        }, { it.printStackTrace() })
        rest_detail_name.text=restaurant.name
        rest_detail_name2.text = restaurant.name
        rest_detail_name.setTextColor(ContextCompat.getColor(this,R.color.white))

        rest_detail_back.setImageDrawable(backArrow)
        rest_detail_back.setOnClickListener { finish() }

        rest_detail_heart.setImageDrawable(heart)
        rest_detail_cart.setImageDrawable(cart)

        app_bar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            //collapse
            if (Math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
                rest_detail_name.visibility=View.VISIBLE
                rest_detail_name.setTextColor(Color.BLACK)
            }else{
                rest_detail_name.visibility=View.GONE
            }

            var alpha = ((Math.min(1f, verticalOffset.toFloat() / ((appBarLayout.height - toolbar.height) * -1).toFloat())) * 255).toInt()
            toolbar.background.alpha = alpha
            when (alpha) {
                in 0..130 -> {
//                    rest_detail_name.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                    setIconChangeColor(R.color.white)
                }
                in 130..160 -> {
//                    rest_detail_name.visibility=View.GONE
                    rest_detail_name.setTextColor(ContextCompat.getColor(applicationContext, R.color.white_1))
                    setIconChangeColor(R.color.white_1)
                }

                in 161..190 -> {

//                    rest_detail_name.setTextColor(ContextCompat.getColor(applicationContext, R.color.white_2))
                    setIconChangeColor(R.color.white_2)
                }
                in 190..255 -> {
//                    rest_detail_name.setTextColor(Color.BLACK)
                    setIconChangeColor(R.color.black)
                }
            }
        }
        rest_detail_rating!!.text = java.lang.Double.toString(restaurant.getRating().toDouble())
        rest_detail_review_count!!.text = "평가(" + restaurant.reviewCnt + ")"
        rest_detail_dangol_count!!.text = "/  단골수 : " + restaurant.dangolCnt



        //        rest_detail_back_btn.setOnClickListener(this);


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

    //    private void setStarView(float rating, LinearLayout rest_detail_rating_star_layout) {
    //        for (int j = 0; j < 5; j++) {
    //            ImageView star = new ImageView(getApplicationContext());
    //            star.setLayoutParams(new ViewGroup.LayoutParams(starDpMap.get("width"), starDpMap.get("height")));
    //            if (rating == 0) {
    //                star.setImageResource(R.drawable.ic_star);
    //                rest_detail_rating_star_layout.addView(star);
    //
    //            } else if (rating >= 1.0) {
    //                rating -= 1.0;
    //                star.setImageResource(R.drawable.ic_star_ranked);
    //                rest_detail_rating_star_layout.addView(star);
    //
    //            } else if (rating == 0.5) {
    //                rating -= 0.5;
    //                star.setImageResource(R.drawable.ic_star_ranked_half);
    //                rest_detail_rating_star_layout.addView(star);
    //            }
    //        }
    //    }


    //    public class NearRestaurantStatePagerAdapter extends FragmentPagerAdapter {
    //
    //        public NearRestaurantStatePagerAdapter(FragmentManager fm) {
    //            super(fm);
    //        }
    //
    ////        @Override
    ////        public int getItemPosition(Object object) {
    ////            return POSITION_NONE;
    ////        }
    //
    //        @Override
    //        public int getCount() {
    //            return 3;
    //        }
    //
    //
    //        @Override
    //        public Fragment getOrderHistoryItem(int position) {
    //            switch (position) {
    //                case 0:
    //                   return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
    //                case 1:
    //                    return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
    //                case 2:
    //                    return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
    //
    //            }
    //            return null;
    //        }
    //
    //        @Override
    //        public CharSequence getPageTitle(int position) {
    //            switch (position) {
    //                case 0:
    //                    return "메뉴";
    //                case 1:
    //                    return "메뉴판";
    //                case 2:
    //                    return "정보";
    //
    //                default:return null;
    //            }
    //        }
    //    }


}
