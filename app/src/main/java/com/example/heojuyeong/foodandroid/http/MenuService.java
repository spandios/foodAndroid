package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.item.MenuCategoryItem;
import com.example.heojuyeong.foodandroid.item.MenuItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuService {

    interface MenuInterFace{
        @GET("api/menu/readMenu")
        Call<ArrayList<MenuItem>> getMenu(@Query("menu_category_id") int menu_category_id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<ArrayList<MenuItem>> getCall(int menu_category_id){
        MenuService.MenuInterFace menuInterFace= MenuService.MenuInterFace.retrofit.create(MenuService.MenuInterFace.class);
        Call<ArrayList<MenuItem>> call=menuInterFace.getMenu(menu_category_id);
        return call;
    }

}
