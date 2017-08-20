package com.example.heojuyeong.foodandroid.item;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 30..
 */

public class MenuCategoryItem {
    @SerializedName("results")
    ArrayList<MenuCategory> results;

    @SerializedName("status")
    String status;

    public ArrayList<MenuCategory> getResults() {
        return results;
    }


    public String getStatus() {
        return status;
    }

    public class MenuCategory {
        @SerializedName("menu_category_id")
        int menu_cate_gory_id;

        @SerializedName("rest_id")
        int rest_id;

        @SerializedName("cateName")
        String cateNAme;

        @SerializedName("cateDescription")
        String cateDescription;

        public int getMenu_cate_gory_id() {
            return menu_cate_gory_id;
        }

        public int getRest_id() {
            return rest_id;
        }

        public String getCateNAme() {
            return cateNAme;
        }

        public String getCateDescription() {
            return cateDescription;
        }
    }
}
