package com.example.fooddeuk.model.cart;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartItem extends RealmObject {
    @PrimaryKey
    int id;

    CartMenu menu;
    RealmList<CartOption> option;
    String menu_count;
    String totalPrice;

    public CartItem(){

    }

    public CartItem(int id, CartMenu menu,RealmList<CartOption> option) {
        this.id = id;
        this.menu = menu;

        this.option=option;
    }

    public CartItem(int id, CartMenu menu) {
        this.id = id;
        this.menu = menu;

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

    public RealmList<CartOption> getOption() {
        return option;
    }

    public void setOption(RealmList<CartOption> option) {
        this.option = option;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public CartMenu getMenu() {
        return menu;
    }

    public void setMenu(CartMenu menu){
        this.menu=menu;
    }
}
