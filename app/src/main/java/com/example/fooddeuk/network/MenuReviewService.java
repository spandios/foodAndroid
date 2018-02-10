package com.example.fooddeuk.network;

import com.example.fooddeuk.model.menu.ReviewItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 9. 27..
 */

public class MenuReviewService {

   interface MenuReviewInterface{
       @GET("api/review/readReview")
       Call<ArrayList<ReviewItem>> getReview(@Query("menu_id") String menu_id);
    }

    public static Call<ArrayList<ReviewItem>> getReview(String menu_id){
        MenuReviewService.MenuReviewInterface menuReviewInterface=RetrofitBase.getInstance().getRetrofit().create(MenuReviewInterface.class);
        Call<ArrayList<ReviewItem>> call=menuReviewInterface.getReview(menu_id);
        return call;
    }


}
