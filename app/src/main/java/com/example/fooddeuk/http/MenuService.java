package com.example.fooddeuk.http;

import com.example.fooddeuk.model.menu.MenuContentItem;

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
        Call<ArrayList<MenuContentItem>> getMenu(@Query("menu_category_id") int menu_category_id);
    }

    public static Call<ArrayList<MenuContentItem>> getMenu(int menu_category_id){
        MenuService.MenuInterFace menuInterFace= RetrofitBase.getInstance().getRetrofit().create(MenuService.MenuInterFace.class);
        Call<ArrayList<MenuContentItem>> call=menuInterFace.getMenu(menu_category_id);
        return call;
    }





}
