package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;

/**
 * Created by heo on 2018. 2. 4..
 */

@Parcel
public class Option extends RealmObject{
    @SerializedName("_id")
    public String menu_option_id;
    public String menu_option_name;
    public String menu_option_price;

    public Option(){

    }

    public Option(String menu_option_id, String menu_option_name, String menu_option_price) {
        this.menu_option_id = menu_option_id;
        this.menu_option_name = menu_option_name;
        this.menu_option_price = menu_option_price;
    }

    public String getMenu_option_id() {
        return menu_option_id;
    }

    public void setMenu_option_id(String menu_option_id) {
        this.menu_option_id = menu_option_id;
    }

    public String getMenu_option_name() {
        return menu_option_name;
    }

    public void setMenu_option_name(String menu_option_name) {
        this.menu_option_name = menu_option_name;
    }

    public String getMenu_option_price() {
        return menu_option_price;
    }

    public void setMenu_option_price(String menu_option_price) {
        this.menu_option_price = menu_option_price;
    }
}
