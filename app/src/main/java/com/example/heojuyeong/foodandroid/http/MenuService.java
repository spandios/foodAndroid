package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.model.MenuItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuService {

    interface MenuInterFace{
        @GET("api/menu/readMenu")
        Call<ArrayList<MenuItem>> getMenu(@Query("menu_category_id") int menu_category_id);

//        @GET("api/menu/readMenuOptionCategory")
//        Call<ArrayList<MenuItem.OptionCategory>> getOptionCategory(@Query("menu_id")int menu_id);
//
//
//        @GET("api/menu/readMenuOption")
//        Call<ArrayList<MenuItem.Option>> getOption(@Query("menu_option_category_id")int menu_option_category_id);

    }


    public static Call<ArrayList<MenuItem>> getCall(int menu_category_id){
        MenuService.MenuInterFace menuInterFace= RetrofitBase.getInstance().getRetrofit().create(MenuService.MenuInterFace.class);
        Call<ArrayList<MenuItem>> call=menuInterFace.getMenu(menu_category_id);
        return call;
    }

//    public Call<ArrayList<MenuItem.OptionCategory>> getMenuOptionCategory(int menu_id){
//        MenuService.MenuInterFace menuInterFace= MenuService.MenuInterFace.retrofit.create(MenuService.MenuInterFace.class);
//        Call<ArrayList<MenuItem.OptionCategory>> call=menuInterFace.getOptionCategory(menu_id);
//        return call;
//    }
//
//    public Call<ArrayList<MenuItem.Option>> getMenuOption(int menu_option_category_id){
//        MenuService.MenuInterFace menuInterFace= MenuService.MenuInterFace.retrofit.create(MenuService.MenuInterFace.class);
//        Call<ArrayList<MenuItem.Option>> call=menuInterFace.getOption(menu_option_category_id);
//        return call;
//    }



}
