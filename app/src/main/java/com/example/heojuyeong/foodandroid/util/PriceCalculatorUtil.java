package com.example.heojuyeong.foodandroid.util;

import android.widget.TextView;

/**
 * Created by heojuyeong on 2017. 9. 12..
 */

public class PriceCalculatorUtil {

    static public String plusPrice(TextView optionPrice, TextView totalPrice){
        String tempPrice1=optionPrice.getText().toString().substring(1);
        String optionPriceResult=tempPrice1.substring(0,tempPrice1.length()-1);

        //총 가격
        String totalPriceResult=totalPrice.getText().toString().substring(0,totalPrice.length()-1);
        int price=Integer.parseInt(totalPriceResult)+Integer.parseInt(optionPriceResult);
        return price+"원";
    }

    static public String minusPrice(TextView optionPrice,TextView totalPrice){
        String tempPrice1=optionPrice.getText().toString().substring(1);
        String optionPriceResult=tempPrice1.substring(0,tempPrice1.length()-1);

        //총 가격
        String totalPriceResult=totalPrice.getText().toString().substring(0,totalPrice.length()-1);
        int price=Integer.parseInt(totalPriceResult)-Integer.parseInt(optionPriceResult);
        return price+"원";
    }

    static public String unCheckRadio(TextView dialog_totalPrice, TextView optionPrice){
        int price=Integer.parseInt(dialog_totalPrice.getText().toString().substring(0,dialog_totalPrice.getText().toString().length()-1))-Integer.parseInt(optionPrice.getText().toString().substring(1,optionPrice.length()-1));
        return String.valueOf(price);
    }

    static public String checkRadio(TextView dialog_totalPrice,TextView optionPrice){
        int price=Integer.parseInt(dialog_totalPrice.getText().toString().substring(0,dialog_totalPrice.getText().toString().length()-1))+Integer.parseInt(optionPrice.getText().toString().substring(1,optionPrice.length()-1));
        return String.valueOf(price);
    }




}
