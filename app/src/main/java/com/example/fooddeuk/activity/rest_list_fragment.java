package com.example.fooddeuk.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.RestaurantAdapter;
import com.example.fooddeuk.http.RestaurantService;
import com.example.fooddeuk.model.menu.MenuItem;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.LayoutUtil;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heo on 2017. 10. 14..
 */

public class rest_list_fragment extends android.support.v4.app.Fragment {
    private RecyclerView restaurantList;
    private MenuItem menuItem;
    private RestaurantAdapter restaurantAdapter;
    private ArrayList<RestaurantItem.Restaurant> restaurantItems;
    private double lat;
    private double lng;
    private int maxDistnace;
    private String menuType;
    private String filter;
    private String rest_name;

    public rest_list_fragment() {

    }

    //    public static rest_list_fragment newInstance(ArrayList<RestaurantItem.Restaurant> restaurantItems) {
//
//        Bundle args = new Bundle();
//        args.putParcelable("restaurantItems", Parcels.wrap(restaurantItems));
//        rest_list_fragment fragment = new rest_list_fragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static rest_list_fragment newInstance(double lat, double lng, int maxDistance, String menuType, String filter, String rest_name) {

        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        args.putInt("maxDistance", maxDistance);
        args.putString("menuType", menuType);
        args.putString("filter",filter);
        args.putString("rest_name",rest_name);
        rest_list_fragment fragment = new rest_list_fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest_list, container, false);
        restaurantList = (RecyclerView) view.findViewById(R.id.rest_list);
        if(getArguments()!=null){
            lat=getArguments().getDouble("lat");
            lng=getArguments().getDouble("lng");
            maxDistnace =getArguments().getInt("maxDistance");
            menuType=getArguments().getString("menuType");
            filter=getArguments().getString("filter");
            rest_name=getArguments().getString("rest_name");
        }
        RestaurantService.getCurrentLocationRestaurant(lat, lng, maxDistnace, menuType, filter, "").enqueue(new Callback<RestaurantItem>() {
            @Override
            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
            RestaurantAdapter restaurantAdapter=new RestaurantAdapter(getActivity(),response.body().getRestaurants());
                restaurantAdapter.setRestaurantItemClickListener(restaurant -> {
                    Parcelable restaurantParcel = Parcels.wrap(restaurant);
                    Bundle extra = new Bundle();
                    extra.putParcelable("restaurant", restaurantParcel);
                    IntentUtil.startActivity(getActivity(), DetailRestaurantActivity.class, extra);
                });
                LayoutUtil.RecyclerViewSetting(getActivity(), restaurantList);
                restaurantList.setAdapter(restaurantAdapter);
            }

            @Override
            public void onFailure(Call<RestaurantItem> call, Throwable t) {
                Logger.d(t);
                Toast.makeText(getActivity(), "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
