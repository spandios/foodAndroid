package com.example.fooddeuk.model.cart;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartItem extends RealmObject {
    @PrimaryKey
    public String id;
    public CartMenu menu;
    public RealmList<CartOptionCategory> optionList;
    public String menu_count;
    public String totalPrice;

    public CartItem(){

    }
    public CartItem(CartMenu menu,RealmList<CartOptionCategory> optionList) {
        this.id = menu.menu_id;
        this.menu = menu;
        this.optionList=optionList;
    }


}
