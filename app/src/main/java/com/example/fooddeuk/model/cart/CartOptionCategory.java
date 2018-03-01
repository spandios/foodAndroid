package com.example.fooddeuk.model.cart;


import com.example.fooddeuk.model.menu.OptionCategory;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartOptionCategory extends RealmObject{
    @PrimaryKey
    public String menu_option_category_id;
    public String menu_option_category_name;
    public boolean multiple;
    public RealmList<CartOption> necessary;
    public RealmList<CartOption> unnecessary;

    public CartOptionCategory(){

    }
    public CartOptionCategory(OptionCategory optionCategory,RealmList<CartOption> necessary,RealmList<CartOption> unnecessary){
        this.menu_option_category_id = optionCategory.menu_option_category_id;
        this.menu_option_category_name = optionCategory.menu_option_category_name;
        this.multiple = optionCategory.multiple;
        this.necessary = necessary;
        this.unnecessary = unnecessary;
    }

}
