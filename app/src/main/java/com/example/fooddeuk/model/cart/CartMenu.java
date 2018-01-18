package com.example.fooddeuk.model.cart;

import io.realm.RealmObject;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartMenu extends RealmObject {
    String menu_id;
    String menu_name;
    String menu_price;
    String menu_avg_time;

    public CartMenu(){

    }

    public CartMenu(String menu_id, String menu_name, String menu_price ,String menu_avg_time) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_avg_time = menu_avg_time;

    }


    public String getMenu_id() {
        return menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }


    public String getMenu_avg_time() {
        return menu_avg_time;
    }

    public void setMenu_avg_time(String menu_avg_time) {
        this.menu_avg_time = menu_avg_time;
    }
}
