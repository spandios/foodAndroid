package com.example.fooddeuk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeuk.R;
import com.example.fooddeuk.http.OrderService;
import com.example.fooddeuk.model.menu.OrderItem;
import com.example.fooddeuk.model.user.UserItemRealm;
import com.example.fooddeuk.util.RealmUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentOrderFragment extends Fragment{
    private int user_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RealmResults<UserItemRealm> userResult = RealmUtil.findDataAll(UserItemRealm.class);
        if (userResult.size() > 0) {
//            user_id = userResult.get(0).user_id;
            user_id=32;
        } else {
            Logger.e("user정보 없음");
        }
        return inflater.inflate(R.layout.fragment_current_order, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**GET CURRENT ORDER**
         *
         * */
        OrderService.getCurrentOrder(user_id).enqueue(new Callback<ArrayList<OrderItem>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderItem>> call, Response<ArrayList<OrderItem>> response) {
                if(response.isSuccessful()){
                    Logger.d(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrderItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
