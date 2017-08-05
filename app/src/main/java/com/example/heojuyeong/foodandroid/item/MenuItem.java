package com.example.heojuyeong.foodandroid.item;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

    @SerializedName("size")
    ArrayList<Size> size=new ArrayList<>();

    public ArrayList<Size> getSize() {
        return size;
    }

    public class Size{
        @SerializedName("size_name")
        String size_name;
        @SerializedName("size_price")
        String size_price;

        public String getSize_name() {
            return size_name;
        }

        public void setSize_name(String size_name) {
            this.size_name = size_name;
        }

        public String getSize_price() {
            return size_price;
        }

        public void setSize_price(String size_price) {
            this.size_price = size_price;
        }
    }


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
