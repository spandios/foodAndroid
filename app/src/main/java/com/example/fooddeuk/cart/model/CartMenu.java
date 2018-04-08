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
        if(menu.picture.size()!=0){
            this.picture = menu.picture.get(0);
        }else{
            this.picture=null;
        }

        this.avgtime = menu.avgtime;
    }


}
