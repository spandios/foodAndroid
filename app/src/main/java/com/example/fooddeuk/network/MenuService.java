package com.example.fooddeuk.network;

import com.example.fooddeuk.model.menu.Menu;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuService {

    interface MenuInterFace{
        @GET("api/menuCategory/readMenu")
        Call<ArrayList<Menu>> getMenu(@Query("menu_category_id") int menu_category_id);
    }

    public static Call<ArrayList<Menu>> getMenu(int menu_category_id){
        MenuService.MenuInterFace menuInterFace= RetrofitBase.getInstance().getRetrofit().create(MenuService.MenuInterFace.class);
        Call<ArrayList<Menu>> call=menuInterFace.getMenu(menu_category_id);
        return call;
    }





}
