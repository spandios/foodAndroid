package com.example.fooddeuk.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.MenuPager;
import com.example.fooddeuk.model.menu.MenuCategory;
import com.example.fooddeuk.model.restaurant.Restaurant;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heo on 2017. 11. 5..
 */

//메뉴카테고리 ->  tab layout  , 메뉴본문 -> viewpager
public class RestMenuCategoryFragment extends Fragment {

    ArrayList<MenuCategory> menuCategoryCategory;
    private Restaurant restaurant;
    @BindView(R.id.rest_menu_tab_layout)
    SmartTabLayout tabLayout;
    @BindView(R.id.rest_detail_menu_category_view_pager)
    MenuPager viewPager;





    public RestMenuCategoryFragment() {
    }

    public static RestMenuCategoryFragment newInstance(Restaurant restaurantWithMenuCategory) {
        Parcelable restaurantParcel = Parcels.wrap(restaurantWithMenuCategory);
        Parcelable menuParcel = Parcels.wrap(restaurantWithMenuCategory.menuCategory);
        Bundle extra = new Bundle();
        extra.putParcelable("restaurant", restaurantParcel);
        extra.putParcelable("menuCategoryCategory", menuParcel);
        RestMenuCategoryFragment restMenuCategoryFragment = new RestMenuCategoryFragment();
        restMenuCategoryFragment.setArguments(extra);
        return restMenuCategoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            menuCategoryCategory =Parcels.unwrap(getArguments().getParcelable("menuCategoryCategory"));
            restaurant=Parcels.unwrap(getArguments().getParcelable("restaurant"));

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest_menu_category, container, false);
        ButterKnife.bind(this,view);
        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
//        getMenuCategory();
    }

//    public void getMenuCategory(){
//        MenuCategoryService.getMenuCategory(rest_id).enqueue(new Callback<RestaurantResponse.Restaurant>() {
//            @Override
//            public void onResponse(Call<RestaurantResponse.Restaurant> call, Response<RestaurantResponse.Restaurant> response) {
//                if(response.isSuccessful()){
//                    menuCategory=response.body();

//                }
//            }
//
//            @Override
//            public void onFailure(Call<RestaurantResponse.Restaurant> call, Throwable t) {
//                t.printStackTrace();
//
//            }
//        });
//
//    }


    public class FragmentPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter{
        private int mCurrentPosition=-1;
        private FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return menuCategoryCategory.size();
        }


//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
//            if (position != mCurrentPosition) {
//                Fragment fragment = (Fragment) object;
//                MenuPager pager = (MenuPager) container;
//                if (fragments != null && fragments.getView() != null) {
//                    mCurrentPosition = position;
//                    pager.measureCurrentView(fragment.getView());
//
//                }
//            }
//        }

        @Override
        public Fragment getItem(int position) {

            return RestMenuFragment.newInstance(menuCategoryCategory.get(position).menu_content,restaurant);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menuCategoryCategory.get(position).menu_category_name;
        }
    }


}
