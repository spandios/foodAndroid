package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by heo on 2018. 1. 1..
 */
@Parcel
public class MenuCategory {
    @SerializedName("_id")
    public String menu_category_id;
    public String menu_category_name;
    public int priority;
    public ArrayList<Menu> menu_content;


    public MenuCategory(){

    }
    public MenuCategory(String menu_category_id, String menu_category_name, int priority, ArrayList<Menu> menu_content) {
        this.menu_category_id = menu_category_id;
        this.menu_category_name = menu_category_name;
        this.priority = priority;
        this.menu_content = menu_content;
    }

    public String getMenu_category_id() {
        return menu_category_id;
    }

    public void setMenu_category_id(String menu_category_id) {
        this.menu_category_id = menu_category_id;
    }

    public String getMenu_category_name() {
        return menu_category_name;
    }

    public void setMenu_category_name(String menu_category_name) {
        this.menu_category_name = menu_category_name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ArrayList<Menu> getMenu_content() {
        return menu_content;
    }

    public void setMenu_content(ArrayList<Menu> menu_content) {
        this.menu_content = menu_content;
    }
}
