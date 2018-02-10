package com.example.fooddeuk.model.cart;

import io.realm.RealmObject;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartMenu extends RealmObject {
    public String menu_id;
    public String name;
    public String price;
    public String avgtime;

    public CartMenu(){

    }

    public CartMenu(String menu_id, String name, String price, String avgtime) {
        this.menu_id = menu_id;
        this.name = name;
        this.price = price;
        this.avgtime = avgtime;
    }

}
