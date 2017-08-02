package com.example.heojuyeong.foodandroid.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuItem {

    @SerializedName("menu_id")
    int menu_id;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    String price;

    @SerializedName("description")
    String description;

    @SerializedName("avgtime")
    String avgtime;

    @SerializedName("menupicture")
    String menupicture;

    @SerializedName("rating")
    String rating;

    @SerializedName("viewcount")
    int viewcount;

    @SerializedName("likecount")
    int likecount;

    public int getMenu_id() {
        return menu_id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getAvgtime() {
        return avgtime;
    }

    public String getMenupicture() {
        return menupicture;
    }

    public String getRating() {
        return rating;
    }

    public int getViewcount() {
        return viewcount;
    }

    public int getLikecount() {
        return likecount;
    }
}
