package com.example.fooddeuk.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.RestaurantAdapter;
import com.example.fooddeuk.model.menu.MenuItem;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.util.LayoutUtil;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by heo on 2017. 10. 14..
 */

public class rest_type1_fragment extends Fragment {
    private RecyclerView restaurantList;
    private MenuItem menuItem;
    private RestaurantAdapter restaurantAdapter;
    ArrayList<RestaurantItem.Restaurant> restaurantItems;

    public rest_type1_fragment(){

    }
    public static rest_type1_fragment newInstance(ArrayList<RestaurantItem.Restaurant> restaurantItems) {

        Bundle args = new Bundle();
        args.putParcelable("restaurantItems", Parcels.wrap(restaurantItems));
        rest_type1_fragment fragment = new rest_type1_fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_rest_menu_type1,container,false);

        restaurantList =(RecyclerView)view.findViewById(R.id.rest_menu_type1_list);
        restaurantAdapter=new RestaurantAdapter(getActivity(),getArguments().getParcelable("restaurantItems"));
        LayoutUtil.RecyclerViewSetting(getActivity(), restaurantList);
        restaurantList.setAdapter(restaurantAdapter);
        return view;
    }
}
