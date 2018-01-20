package com.example.fooddeuk.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.ImageViewPager;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.fragment.HomeFragment;
import com.example.fooddeuk.fragment.RestMenuCategoryFragment;
import com.example.fooddeuk.http.RestaurantService;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestaurantActivity extends AppCompatActivity implements MenuListAdapter.OnItemClickListener,MenuListAdapter.OnCartCountClickListener, View.OnClickListener {

    RestaurantItem.Restaurant restaurant;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    RestMenuCategoryFragment restMenuCategoryFragment;
    HomeFragment homeFragment;
    ImageViewPager imageViewPager;




//    @BindView(R.id.rest_detail_back_btn)
//    Button rest_detail_back_btn;


    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rest_detail_back)
    ImageView rest_detail_back;
    @BindView(R.id.rest_detail_name)
    TextView rest_detai_name;
    @BindView(R.id.rest_detail_heart)
    ImageView rest_detail_heart;
    @BindView(R.id.rest_detail_cart)
    ImageView rest_detail_cart;

    @BindView(R.id.rest_detail_image_viewpager)
    ViewPager rest_detail_image_viewpager;

    @BindView(R.id.rest_detail_image_viewpager_indicator)
    CircleIndicator rest_detail_image_viewpager_indicator;

    @BindView(R.id.rest_main_tab)
    FrameLayout rest_main_tab;
//    @BindView(R.id.rest_detail_image)
//    ImageView rest_detail_image;
    //이름
//    @BindView(R.id.rest_detail_name)
//    TextView rest_detail_name;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;


    //평점
    @BindView(R.id.rest_detail_rating)
    TextView rest_detail_rating;
    //리뷰숫자
    @BindView(R.id.rest_detail_review_count)
    TextView rest_detail_review_count;
    //단골숫자
    @BindView(R.id.rest_detail_dangol_count)
    TextView rest_detail_dangol_count;


    //별
//    @BindView(R.id.rest_detail_rating_star_layout)
//    LinearLayout rest_detail_rating_star_layout;

    @BindView(R.id.rest_detail_main_tab)
    TabLayout tabLayout;

    //TabLayout
//    @BindView(R.id.rest_detail_main_tab)
//    TabLayout rest_detail_main_tab;

    //Menu ViewPager
//    @BindView(R.id.rest_detail_main_tab_viewpager)
//    MainTabViewPager rest_detail_main_tab_viewpager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        ButterKnife.bind(this);

        //Parcel로 선택된 식당 정보 가져옴
        /**from CurLocationRestaurantFragment**/
        /**from CurLocationRestaurantFragment**/
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));

        //메뉴관련 ViewPager 생성
        restMenuCategoryFragment=RestMenuCategoryFragment.newInstance(restaurant);

        //HomeFragment 생성
        homeFragment=new HomeFragment();
        //ImageViewPager 등록
        RestaurantService.getPicture(restaurant.rest_id).enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                Logger.d(response.body());
                if(response.isSuccessful()){
                    imageViewPager=new ImageViewPager(DetailRestaurantActivity.this,response.body());
                    rest_detail_image_viewpager.setAdapter(imageViewPager);
                    rest_detail_image_viewpager_indicator.setViewPager(rest_detail_image_viewpager);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Logger.d("Ff");
            t.printStackTrace();
            }
        });





        viewSetting();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rest_main_tab, restMenuCategoryFragment);
        fragmentTransaction.add(R.id.rest_main_tab, homeFragment);
        fragmentTransaction.hide(homeFragment);
        fragmentTransaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
// get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.show(restMenuCategoryFragment);
                        fragmentTransaction.hide(homeFragment);
//                        fragmentTransaction.replace(R.id.rest_main_tab, restMenuCategoryFragment);
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
//                        ft.replace(R.id.rest_main_tab, homeFragment);
                        ft.hide(restMenuCategoryFragment);
                        ft.show(homeFragment);
                        ft.commit();

                        break;
                    case 2:
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.rest_main_tab, restMenuCategoryFragment);
                        fragmentTransaction.commit();
                        break;
                }

//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.rest_main_tab, fragment);
//                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    //Implement CartAddClick -> 메뉴 추가 한 뒤 장바구니 개수 새로고침
    @Override
    public void onCartCount(int cartSize) {
//        rest_detail_cart_count.setText(String.valueOf(cartSize));
    }

    //TODO 더 안정화
    //Implement ItemClick -> 해당 position의 상세 레이아웃 펼쳐짐
    @Override
    public void onItemClick(RecyclerView.ViewHolder view, float y,RecyclerView recyclerView) {
        Logger.d(view.itemView.getY());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rest_detail_back_btn:
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
    }

    @Override
    protected void onResume() {
        super.onResume();
//        rest_detail_cart_count.setText(String.valueOf(RealmUtil.getDataSize(CartItem.class)));

    }



    private void viewSetting() {
        //MenuList

        rest_detai_name.setText(restaurant.getName());
        rest_detai_name.setTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        final Drawable backArrow = getResources().getDrawable(R.drawable.ic_back_black);
        final Drawable heart = getResources().getDrawable(R.drawable.ic_heart);
        final Drawable cart=getResources().getDrawable(R.drawable.ic_cart);


        backArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        heart.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        cart.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        rest_detail_back.setImageDrawable(backArrow);
        rest_detail_back.setOnClickListener(view -> finish());

        rest_detail_heart.setImageDrawable(heart);
        rest_detail_cart.setImageDrawable(cart);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //collapse
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                backArrow.setColorFilter(getResources().getColor(R.color.charcoal_grey), PorterDuff.Mode.SRC_ATOP);
                heart.setColorFilter(getResources().getColor(R.color.charcoal_grey), PorterDuff.Mode.SRC_ATOP);
                cart.setColorFilter(getResources().getColor(R.color.charcoal_grey), PorterDuff.Mode.SRC_ATOP);
            } else if (verticalOffset == 0) {
                backArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                heart.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                cart.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            } else {
                // Somewhere in between
            }
//                int toolBarHeight = toolbar.getMeasuredHeight();
//                int appBarHeight = appBarLayout.getMeasuredHeight();
//                Float f = ((((float) appBarHeight - toolBarHeight) + verticalOffset) / ( (float) appBarHeight - toolBarHeight)) * 255;
////                toolbar.getBackground().setAlpha(255 - Math.round(f));
//                toolbar.setAlpha(255 - Math.round(f));
        });

//        Picasso.with(this).load(restaurant.rest_picture).fit().into(rest_detail_image);
        rest_detail_rating.setText(Double.toString(restaurant.getRating()));
        rest_detail_review_count.setText("평가("+restaurant.reviewCnt+")");
        rest_detail_dangol_count.setText("/  단골수 : "+restaurant.dangolCnt);
        collapsingToolbarLayout.setTitle(restaurant.name);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        int bottomPx = 180; // margin in dips
        float d = getResources().getDisplayMetrics().density;
        int margin = (int)(bottomPx * d);
        collapsingToolbarLayout.setExpandedTitleMarginBottom(margin);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

//        rest_detail_back_btn.setOnClickListener(this);




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


//    public class fragmentPagerAdapter extends FragmentPagerAdapter {
//
//        public fragmentPagerAdapter(FragmentManager fm) {
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
//        public Fragment getItem(int position) {
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
