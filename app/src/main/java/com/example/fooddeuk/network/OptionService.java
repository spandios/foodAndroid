package com.example.fooddeuk.network;

import com.example.fooddeuk.model.menu.OptionCategory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 9. 28..
 */

public class OptionService {

    interface OptionInterface {
        @GET("api/option/readOption")
        Call<ArrayList<OptionCategory>> getOption(@Query("menu_id") int menu_id);
    }

    public static Call<ArrayList<OptionCategory>> getOption(int menu_id) {
        OptionService.OptionInterface menuReviewInterface = RetrofitBase.getInstance().getRetrofit().create(OptionInterface.class);
        Call<ArrayList<OptionCategory>> call = menuReviewInterface.getOption(menu_id);
        return call;
    }


}
