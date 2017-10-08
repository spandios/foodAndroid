package com.example.fooddeuk.http;

import com.example.fooddeuk.model.menu.OptionItem;

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
        Call<ArrayList<OptionItem>> getOption(@Query("menu_id") int menu_id);
    }



    public static Call<ArrayList<OptionItem>> getOption(int menu_id) {
        OptionService.OptionInterface menuReviewInterface = RetrofitBase.getInstance().getRetrofit().create(OptionInterface.class);
        Call<ArrayList<OptionItem>> call = menuReviewInterface.getOption(menu_id);
        return call;
    }


}
