package com.example.heojuyeong.foodandroid.model.menu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 30..
 */

public class MenuCategoryItem {
    @SerializedName("results")
    ArrayList<MenuCategory> results;
    String status;

    public ArrayList<MenuCategory> getResults() {
        return results;
    }


    public String getStatus() {
        return status;
    }

    public static class MenuCategory {
        int menu_category_id;
        int rest_id;
        String cateName;
        String cateDescription;

        public int getMenu_cate_gory_id() {
            return menu_category_id;
        }

        public int getRest_id() {
            return rest_id;
        }

        public String getCateName() {
            return cateName;
        }

        public String getCateDescription() {
            return cateDescription;
        }
    }
}

