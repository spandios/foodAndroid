package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 28..
 */

public class OptionCategory implements Serializable {
    @SerializedName("_id")
    public String menu_option_category_id;
    public String menu_option_category_name;
    public boolean multiple;
    public ArrayList<Option> necessary;
    public ArrayList<Option> unnecessary;

    public OptionCategory(){

    }

    public String getMenu_option_category_id() {
        return menu_option_category_id;
    }

    public void setMenu_option_category_id(String menu_option_category_id) {
        this.menu_option_category_id = menu_option_category_id;
    }

    public String getMenu_option_category_name() {
        return menu_option_category_name;
    }

    public void setMenu_option_category_name(String menu_option_category_name) {
        this.menu_option_category_name = menu_option_category_name;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public ArrayList<Option> getNecessary() {
        return necessary;
    }

    public void setNecessary(ArrayList<Option> necessary) {
        this.necessary = necessary;
    }

    public ArrayList<Option> getUnnecessary() {
        return unnecessary;
    }

    public void setUnnecessary(ArrayList<Option> unnecessary) {
        this.unnecessary = unnecessary;
    }
}
