package com.example.fooddeuk.menu.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by heo on 2018. 1. 1..
 */

public class MenuCategory implements Serializable{
    @SerializedName("_id")
    public String menu_category_id;
    public String menu_category_name;
    public int priority;
    public ArrayList<Menu> menu_content;


    public MenuCategory(String menu_category_id, String menu_category_name, int priority, ArrayList<Menu> menu_content) {
        this.menu_category_id = menu_category_id;
        this.menu_category_name = menu_category_name;
        this.priority = priority;
        this.menu_content = menu_content;
    }

}
