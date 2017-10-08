package com.example.fooddeuk.model.cart;

import io.realm.RealmObject;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartMenu extends RealmObject {
    int menu_id;
    String menu_name;
    int menu_price;
    String menu_avgtime;

    public CartMenu(){

    }

    public CartMenu(int menu_id, String menu_name, int menu_priceString ,String menu_avgtime) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_avgtime = menu_avgtime;

    }



    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public int getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(int menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_avgtime() {
        return menu_avgtime;
    }

    public void setMenu_avgtime(String menu_avgtime) {
        this.menu_avgtime = menu_avgtime;
    }
}
