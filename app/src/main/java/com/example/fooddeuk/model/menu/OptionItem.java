package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 28..
 */
@Parcel
public class OptionItem {
    @SerializedName("_id")
    public String menu_option_category_id;
    public String menu_option_category_name;
    public boolean multiple;
    public ArrayList<Option> necessary;
    public ArrayList<Option> unnecessary;

    @Parcel
    public static class Option {
        @SerializedName("_id")
        public String menu_option_id;
        public String menu_option_name;
        public String menu_option_price;
        public String menu_option_description;


    }



}
