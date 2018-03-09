package com.example.fooddeuk.cart.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by heo on 2018. 3. 3..
 */

public class SelectedOption extends RealmObject {
    public String categoryName;
    public RealmList<CartOption> selectedOptionList=new RealmList<>();

    public SelectedOption(){
        categoryName="";
    }

    public SelectedOption(String categoryName,CartOption basicNecessaryOption){
        this.categoryName=categoryName;
        selectedOptionList.add(basicNecessaryOption);
    }


}
