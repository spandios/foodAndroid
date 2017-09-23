package com.example.heojuyeong.foodandroid.model.cart;

import io.realm.RealmObject;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartOption extends RealmObject{

    int menu_option_id;
    String menu_option_name;
    int menu_option_price;
    String menu_option_cateogory_name;

    public CartOption(){

    }
    public CartOption(int menu_option_id, String menu_option_name, int menu_option_price,String menu_option_cateogory_name) {
        this.menu_option_id = menu_option_id;
        this.menu_option_name = menu_option_name;
        this.menu_option_price = menu_option_price;
        this.menu_option_cateogory_name=menu_option_cateogory_name;
    }

    public String getMenu_option_cateogory_name() {
        return menu_option_cateogory_name;
    }

    public void setMenu_option_cateogory_name(String menu_option_cateogory_name) {
        this.menu_option_cateogory_name = menu_option_cateogory_name;
    }

    public int getMenu_option_id() {
        return menu_option_id;
    }

    public void setMenu_option_id(int menu_option_id) {
        this.menu_option_id = menu_option_id;
    }

    public String getMenu_option_name() {
        return menu_option_name;
    }

    public void setMenu_option_name(String menu_option_name) {
        this.menu_option_name = menu_option_name;
    }

    public int getMenu_option_price() {
        return menu_option_price;
    }

    public void setMenu_option_price(int menu_option_price) {
        this.menu_option_price = menu_option_price;
    }
}
