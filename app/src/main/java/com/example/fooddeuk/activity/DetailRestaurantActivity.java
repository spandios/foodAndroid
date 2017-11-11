package com.example.fooddeuk.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.fragment.RestMenuCategoryFragment;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.util.LayoutUtil;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRestaurantActivity extends AppCompatActivity implements MenuListAdapter.OnItemClickListener,MenuListAdapter.OnCartCountClickListener, View.OnClickListener {
    FragmentPagerAdapter fragmentPagerAdapter;
    RestaurantItem.Restaurant restaurant;
    HashMap<String, Integer> starDpMap;

    //ScrollView
    @BindView(R.id.detailRestaurantScrollView)
    com.amar.library.ui.StickyScrollView detailRestaurantScrollView;

    //뒤로가기
    @BindView(R.id.rest_detail_back_btn)
    Button rest_detail_back_btn;

    //이름
    @BindView(R.id.rest_detail_name)
    TextView rest_detail_name;

    //사진
    @BindView(R.id.rest_detail_image)
    ImageView rest_detail_image;

    //평점
    @BindView(R.id.rest_detail_rating)
    TextView rest_detail_rating;

    //별
    @BindView(R.id.rest_detail_rating_star_layout)
    LinearLayout rest_detail_rating_star_layout;

    //TabLayout
    @BindView(R.id.rest_detail_main_tab)
    TabLayout rest_detail_main_tab;

    //Menu ViewPager
    @BindView(R.id.rest_detail_main_tab_viewpager)
    MainTabViewPager rest_detail_main_tab_viewpager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        ButterKnife.bind(this);

        //Parcel로 선택된 식당 정보 가져옴
        /**from CurLocationRestaurantFragment**/
        /**from CurLocationRestaurantFragment**/


        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        starDpMap=LayoutUtil.DpToLayoutParams(getApplicationContext(), 12,11);
        viewSetting();
        setStarView(restaurant.rating,rest_detail_rating_star_layout);
        fragmentPagerAdapter = new fragmentPagerAdapter(getSupportFragmentManager());
        rest_detail_main_tab_viewpager.setAdapter(fragmentPagerAdapter);
        rest_detail_main_tab.setupWithViewPager(rest_detail_main_tab_viewpager);

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


//            if(rest_detail_main_tab.getHeight()>detailRestaurantScrollView.getScrollY()){
//                detailRestaurantScrollView.smoothScrollTo(0,(int)view.itemView.getY());
////                recyclerView.scrollToPosition(view.getAdapterPosition());
//
//            }else{
////                recyclerView.scrollToPosition(view.getAdapterPosition());
//                detailRestaurantScrollView.smoothScrollTo(0,(int)view.itemView.getY());
//            }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rest_detail_back_btn:
                finish();
                break;

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

        rest_detail_name.setText(restaurant.getName());
        Picasso.with(this).load(restaurant.getRest_picture()).resize(150, 150).into(rest_detail_image);
        rest_detail_rating.setText(Double.toString(restaurant.getRating()));
        rest_detail_back_btn.setOnClickListener(this);

    }


    private void setStarView(float rating, LinearLayout rest_detail_rating_star_layout) {
        for (int j = 0; j < 5; j++) {
            ImageView star = new ImageView(getApplicationContext());
            star.setLayoutParams(new ViewGroup.LayoutParams(starDpMap.get("width"), starDpMap.get("height")));
            if (rating == 0) {
                star.setImageResource(R.drawable.ic_star);
                rest_detail_rating_star_layout.addView(star);

            } else if (rating >= 1.0) {
                rating -= 1.0;
                star.setImageResource(R.drawable.ic_star_ranked);
                rest_detail_rating_star_layout.addView(star);

            } else if (rating == 0.5) {
                rating -= 0.5;
                star.setImageResource(R.drawable.ic_star_ranked_half);
                rest_detail_rating_star_layout.addView(star);

            }
        }
    }


    public class fragmentPagerAdapter extends FragmentPagerAdapter {

        public fragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                   return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
                case 1:
                    return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
                case 2:
                    return RestMenuCategoryFragment.newInstance(restaurant.rest_id);

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "메뉴";
                case 1:
                    return "메뉴판";
                case 2:
                    return "정보";

                default:return null;
            }
        }
    }




}
