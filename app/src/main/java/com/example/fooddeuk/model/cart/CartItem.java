package com.example.fooddeuk.model.cart;

import com.example.fooddeuk.model.menu.Option;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartItem extends RealmObject {
    @PrimaryKey
    public int id;
    public CartMenu menu;
    public RealmList<Option> option;
    public String menu_count;
    public String totalPrice;

    public CartItem(){

    }
    public CartItem(int id, CartMenu menu,RealmList<Option> option) {

        this.id = id;
        this.menu = menu;
        this.option=option;

    }

    public CartItem(int id, CartMenu menu) {
        this.id = id;
        this.menu = menu;

    }



    public CartMenu getMenu() {
        return menu;
    }

    public void setMenu(CartMenu menu) {
        this.menu = menu;
    }

    public RealmList<Option> getOption() {
        return option;
    }

    public void setOption(RealmList<Option> option) {
        this.option = option;
    }

    public String getMenu_count() {
        return menu_count;
    }

    public void setMenu_count(String menu_count) {
        this.menu_count = menu_count;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
