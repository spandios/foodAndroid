package com.example.fooddeuk.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.RestaurantAdapter;
import com.example.fooddeuk.http.RestaurantService;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.LayoutUtil;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heo on 2017. 10. 14..
 */

public class rest_list_fragment extends android.support.v4.app.Fragment {

    private double lat;
    private double lng;
    private int maxDistance;
    private String menuType;
    private String filter;
    private String rest_name;
    private boolean scrollEnough;

    public rest_list_fragment() {

    }

    @BindView(R.id.rest_list_fab)
    FloatingActionButton restListFab;
    @BindView(R.id.rest_list)
    RecyclerView restaurantList;


    public static rest_list_fragment newInstance(double lat, double lng, int maxDistance, String menuType, String filter, String rest_name) {

        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        args.putInt("maxDistance", maxDistance);
        args.putString("menuType", menuType);
        args.putString("filter", filter);
        args.putString("rest_name", rest_name);
        rest_list_fragment fragment = new rest_list_fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest_list, container, false);
        ButterKnife.bind(this, view);
        restListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantList.smoothScrollToPosition(0);
            }
        });



        if (getArguments() != null) {
            lat = getArguments().getDouble("lat");
            lng = getArguments().getDouble("lng");
            maxDistance = getArguments().getInt("maxDistance");
            menuType = getArguments().getString("menuType");
            if (menuType == null) {
                menuType = "";
            }
            Logger.d(menuType);
            filter = getArguments().getString("filter");
            Logger.d(filter);
            rest_name = getArguments().getString("rest_name");
            if (rest_name == null) {
                rest_name = "";
            }

        } else {
            new Exception().printStackTrace();
        }

        RestaurantService.getCurrentLocationRestaurant(lat, lng, maxDistance, menuType, filter, rest_name).enqueue(new Callback<RestaurantItem>() {
            @Override
            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
                if (response.isSuccessful()) {
                    RestaurantAdapter restaurantAdapter = new RestaurantAdapter(getActivity(), response.body().getRestaurants());
                    restaurantAdapter.setRestaurantItemClickListener(restaurant -> {
                        Parcelable restaurantParcel = Parcels.wrap(restaurant);
                        Bundle extra = new Bundle();
                        extra.putParcelable("restaurant", restaurantParcel);
                        IntentUtil.startActivity(getActivity(), DetailRestaurantActivity.class, extra);
                    });
                    LayoutUtil.RecyclerViewSetting(getActivity(), restaurantList);
                    restaurantList.setAdapter(restaurantAdapter);

                    if(response.body().getRestaurants().size()>10){
                        restaurantList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                if (dy > 0 || dy < 0 && restListFab.isShown()) {
                                    restListFab.hide();
                                }
                            }

                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == RecyclerView.SCROLL_STATE_IDLE&&recyclerView.computeVerticalScrollOffset()>300) {
                                    restListFab.show();
                                }
                            }
                        });
                    }else{
                        restListFab.hide();
                    }

                }
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
