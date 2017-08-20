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


    @SerializedName("options")
    ArrayList<Options> options=new ArrayList<>();


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



    public ArrayList<Options> getOptions() {
        return options;
    }



    public int getViewcount() {
        return viewcount;
    }

    public int getLikecount() {
        return likecount;
    }


    public class Options{
        @SerializedName("menu_option_category_id")
        int menu_option_category_id;
        @SerializedName("menu_category_name")
        String menu_category_name;
        @SerializedName("necessary")
        int necessary;
        @SerializedName("option")
        ArrayList<Option> option;

        public int getMenu_option_category_id() {
            return menu_option_category_id;
        }

        public String getMenu_category_name() {
            return menu_category_name;
        }

        public int getNecessary() {
            return necessary;
        }

        public ArrayList<Option> getOption() {
            return option;
        }

        public class Option{
            @SerializedName("menu_option_id")
            int menu_option_id;
            @SerializedName("menu_option_name")
            String menu_option_name;
            @SerializedName("menu_option_price")
            int menu_option_price;
            @SerializedName("menu_option_description")
            String menu_option_description;

            public int getMenu_option_id() {
                return menu_option_id;
            }

            public String getMenu_option_name() {
                return menu_option_name;
            }

            public int getMenu_price() {
                return menu_option_price;
            }

            public String getMenu_description() {
                return menu_option_description;
            }
        }

    }



}
