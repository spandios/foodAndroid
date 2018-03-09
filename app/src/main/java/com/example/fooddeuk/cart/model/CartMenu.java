package com.example.fooddeuk.cart.model;

import com.example.fooddeuk.menu.model.Menu;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartMenu extends RealmObject {
    @PrimaryKey
    public String menu_id;
    public String name;
    public String price;
    public String picture;
    public String avgtime;

    public CartMenu(){

    }
    public CartMenu(Menu menu){
        this.menu_id = menu.menu_id;
        this.name = menu.name;
        this.price = menu.price;
        this.picture=menu.picture[0];
        this.avgtime = menu.avgtime;
    }


}
