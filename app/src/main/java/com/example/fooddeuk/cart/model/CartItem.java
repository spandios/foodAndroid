package com.example.fooddeuk.cart.model;

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
    public RealmList<CartOptionCategory> optionCategoryList;
    public String menu_count;
    public String totalPrice;
    public RealmList<SelectedOption> selNecessaryOptionList;
    public RealmList<SelectedOption> selUnNecessaryOptionList;

    public CartItem(){

    }
    public CartItem(CartMenu menu,RealmList<CartOptionCategory> optionCategoryList) {
        this.id = menu.menu_id;
        this.menu = menu;
        this.optionCategoryList = optionCategoryList;
        menu_count="";
        totalPrice="";
        selNecessaryOptionList=new RealmList<>();
        selUnNecessaryOptionList =new RealmList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CartMenu getMenu() {
        return menu;
    }

    public void setMenu(CartMenu menu) {
        this.menu = menu;
    }

    public RealmList<CartOptionCategory> getOptionCategoryList() {
        return optionCategoryList;
    }

    public void setOptionCategoryList(
        RealmList<CartOptionCategory> optionCategoryList) {
        this.optionCategoryList = optionCategoryList;
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

    public RealmList<SelectedOption> getSelNecessaryOptionList() {
        return selNecessaryOptionList;
    }

    public void setSelNecessaryOptionList(
        RealmList<SelectedOption> selNecessaryOptionList) {
        this.selNecessaryOptionList = selNecessaryOptionList;
    }

    public RealmList<SelectedOption> getSelUnNecessaryOptionList() {
        return selUnNecessaryOptionList;
    }

    public void setSelUnNecessaryOptionList(
        RealmList<SelectedOption> selUnNecessaryOptionList) {
        this.selUnNecessaryOptionList = selUnNecessaryOptionList;
    }
}
