package com.example.fooddeuk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.DetailRestaurantActivity;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.model.menu.Menu;
import com.example.fooddeuk.model.restaurant.Restaurant;
import com.example.fooddeuk.util.LayoutUtil;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heo on 2017. 11. 5..
 */

//RecyclerView Implement in Fragment
public class RestMenuFragment extends Fragment  {
    @BindView(R.id.rest_detail_menu_list)
    RecyclerView rest_detail_menu_list;
    private Context context;
    ArrayList<Menu> menu;

    private Restaurant restaurant;


    public RestMenuFragment(){

    }



    public static RestMenuFragment newInstance(ArrayList<Menu> menu, Restaurant restaurant){

        Bundle args=new Bundle();

        args.putParcelable("menuContent", Parcels.wrap(menu));
        args.putParcelable("restaurant",Parcels.wrap(restaurant));
        RestMenuFragment restMenuFragment=new RestMenuFragment();
        restMenuFragment.setArguments(args);
        return restMenuFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){

//            menu_category_id=getArguments().getInt("menu_category_id");
            menu=Parcels.unwrap(getArguments().getParcelable("menuContent"));

            restaurant=Parcels.unwrap(getArguments().getParcelable("restaurant"));


        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest_menu, container, false);
        ButterKnife.bind(this, view);
        getMenuList();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DetailRestaurantActivity){
            this.context=context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void getMenuList(){
        MenuListAdapter menuListAdapter=new MenuListAdapter(getActivity(),menu,restaurant);
//        menuListAdapter = new MenuListAdapter(getActivity(), menuCategory,rest_id);
        menuListAdapter.setOnItemClickListener((DetailRestaurantActivity)context);
        LayoutUtil.RecyclerViewSetting(getActivity(),rest_detail_menu_list);
        rest_detail_menu_list.setFocusable(true);
        rest_detail_menu_list.setFocusableInTouchMode(true);
        rest_detail_menu_list.setNestedScrollingEnabled(true);
        rest_detail_menu_list.setAdapter(menuListAdapter);
        menuListAdapter.notifyDataSetChanged();


    }


}
