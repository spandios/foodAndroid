package com.example.fooddeuk.model.cart;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartOption extends RealmObject{
    @PrimaryKey
    public String menu_option_id;
    public String menu_option_name;
    public String menu_option_price;

    public CartOption(){

    }

    public CartOption(String menu_option_id, String menu_option_name, String menu_option_price) {
        this.menu_option_id = menu_option_id;
        this.menu_option_name = menu_option_name;
        this.menu_option_price = menu_option_price;
    }

    public String getMenu_option_name() {
        return menu_option_name;
    }

}
