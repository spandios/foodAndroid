package com.example.fooddeuk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.MenuPager;
import com.example.fooddeuk.http.MenuCategoryService;
import com.example.fooddeuk.model.menu.MenuCategoryItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heo on 2017. 11. 5..
 */

//메뉴카테고리 ->  tab layout  , 메뉴본문 -> viewpager
public class RestMenuCategoryFragment extends Fragment {
    private int rest_id;
    ArrayList<MenuCategoryItem.MenuCategory> menuCategories;

    @BindView(R.id.rest_menu_tab_layout)
    SmartTabLayout tabLayout;
    @BindView(R.id.rest_detail_menu_category_view_pager)
    MenuPager viewPager;




    public RestMenuCategoryFragment() {
    }

    public static RestMenuCategoryFragment newInstance(int rest_id) {
        Bundle args = new Bundle();
        args.putInt("rest_id", rest_id);

        RestMenuCategoryFragment restMenuCategoryFragment = new RestMenuCategoryFragment();
        restMenuCategoryFragment.setArguments(args);

        return restMenuCategoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest_menu_category, container, false);
        ButterKnife.bind(this,view);
        if (getArguments() != null) {
            rest_id = getArguments().getInt("rest_id");
            getMenuCategory();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void getMenuCategory(){
        MenuCategoryService.getMenuCategory(rest_id).enqueue(new Callback<MenuCategoryItem>() {
            @Override
            public void onResponse(Call<MenuCategoryItem> call, Response<MenuCategoryItem> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("SUCCESS")) {
                        ArrayList<MenuCategoryItem.MenuCategory> items = response.body().getResults();
                        //default first  menu show
                        if(items.size()>0){
                            menuCategories=items;
                            FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getChildFragmentManager());
                            viewPager.setAdapter(fragmentPagerAdapter);

                            tabLayout.setViewPager(viewPager);

                        }else{
                            Logger.d(" Null menuCategory");
                        }

                    } else {
                        Logger.d(" Null menuCategory");
                    }
                }
            }

            @Override
            public void onFailure(Call<MenuCategoryItem> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
        private int mCurrentPosition=-1;
        private FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return menuCategories.size();
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (position != mCurrentPosition) {
                Fragment fragment = (Fragment) object;
                MenuPager pager = (MenuPager) container;
                if (fragment != null && fragment.getView() != null) {
                    mCurrentPosition = position;
                    pager.measureCurrentView(fragment.getView());

                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            return RestMenuFragment.newInstance(menuCategories.get(position).getMenu_cate_gory_id());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menuCategories.get(position).getCateName();
        }
    }


}
