package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by heo on 2018. 1. 1..
 */
@Parcel
public class Menu {
    @SerializedName("_id")
    public String menu_category_id;
    public String menu_category_name;
    public int priority;
    public ArrayList<MenuContentItem> menu_content;
}
