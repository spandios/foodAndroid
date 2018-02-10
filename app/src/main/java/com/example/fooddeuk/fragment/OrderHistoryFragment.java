package com.example.fooddeuk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.OrderHistoryAdapter;
import com.example.fooddeuk.model.order.OrderResponse;
import com.example.fooddeuk.model.user.User;
import com.example.fooddeuk.network.OrderService;
import com.example.fooddeuk.util.LayoutUtil;
import com.example.fooddeuk.util.RealmUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends android.support.v4.app.Fragment {

    private String user_id;

    @BindView(R.id.currentOrderRecyclerView)
    RecyclerView currentOrderRecyclerView;
    @BindView(R.id.order_history_loading_bar)
    me.zhanghai.android.materialprogressbar.MaterialProgressBar order_history_loading_bar;


    interface OrderDataCallback {
        void getOrderHistoryItem(Throwable err, ArrayList<OrderResponse> item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        user_id=RealmUtil.findData(User.class).user_id;
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        order_history_loading_bar.setIndeterminateDrawable(new android.support.v4.widget.CircularProgressDrawable(getContext()));

        /**
         *
         *GET CURRENT ORDER
         *
         * */

        getOrderHistoryData((err, item) -> {
                if (err != null) {
                    try {
                        throw err;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }else{
                    order_history_loading_bar.setVisibility(View.GONE);
                    setRecyclerView(currentOrderRecyclerView, item);
                }
            });





    }

    @Override
    public void onResume() {
        super.onResume();
    }




    private void getOrderHistoryData(OrderDataCallback OrderDataCallback) {
        OrderService.getCurrentOrder(user_id).enqueue(new Callback<ArrayList<OrderResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderResponse>> call, Response<ArrayList<OrderResponse>> response) {
                if (response.isSuccessful()) {
                    OrderDataCallback.getOrderHistoryItem(null, response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<OrderResponse>> call, Throwable t) {
                OrderDataCallback.getOrderHistoryItem(t, null);
            }
        });
    }

    private void setRecyclerView(RecyclerView recyclerView, ArrayList<OrderResponse> items) {

        LayoutUtil.RecyclerViewSetting(getActivity(), recyclerView);
        recyclerView.setAdapter(new OrderHistoryAdapter(getActivity(), items));
    }


}
