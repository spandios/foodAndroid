package com.example.heojuyeong.foodandroid.util;

import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by heojuyeong on 2017. 9. 12..
 */

public class PriceUtil {



    public static String TextViewToString(TextView textView) {
        return textView.getText().toString();
    }

    public static String TextViewToString(CharSequence charSequence) {
        return charSequence.toString();
    }

    public static String comma_won(int money) {
        int inValues = money;
        DecimalFormat Commas = new DecimalFormat("#,###");
        String result_int = Commas.format(inValues);
        return result_int + "원";
    }

    public static String comma_won(String money) {
        int inValues = Integer.parseInt(money);
        DecimalFormat Commas = new DecimalFormat("#,###");
        String result_int = Commas.format(inValues);
        return result_int + "원";
    }

    public static String optionComma_won(int money){
        int inValues = money;
        DecimalFormat Commas = new DecimalFormat("#,###");
        String result_int = Commas.format(inValues);
        return "+"+result_int + "원";
    }


    static public String plusPrice(String price1, String price2) {
        return comma_won(getOriginalPrice(price1) + getOriginalPrice(price2));
    }

    static public String plusPrice(String price1, int price2) {
        return comma_won(getOriginalPrice(price1) + price2);
    }

    static public String minusPrice(String price1, String price2) {
        if (getOriginalPrice(price1) > getOriginalPrice(price2)) {
            return comma_won(getOriginalPrice(price1) - getOriginalPrice(price2));
        } else {
            return comma_won(getOriginalPrice(price2) - getOriginalPrice(price1));
        }

    }

    static public String minusPrice(String price1, int price2) {
        if (getOriginalPrice(price1) > price2) {
            return comma_won(getOriginalPrice(price1) - price2);
        } else {
            return comma_won(price2 - getOriginalPrice(price1));
        }

    }

    static public int getOriginalPrice(String price) {

        StringBuilder resultPrice1 = new StringBuilder(price);
        if (resultPrice1.toString().contains("+")) {
            resultPrice1.deleteCharAt(resultPrice1.indexOf("+"));
        }

        if (resultPrice1.toString().contains("원")) {
            resultPrice1.deleteCharAt(resultPrice1.indexOf("원"));

        }

        if (resultPrice1.length() >= 5) {
            while (true) {
                if (resultPrice1.toString().contains(","))
                    resultPrice1.deleteCharAt(resultPrice1.indexOf(","));
                else {
                    break;
                }
            }
        }

        return Integer.parseInt(resultPrice1.toString());
    }


    static public String unCheckRadio(TextView dialog_totalPrice, TextView optionPrice) {
        int price = Integer.parseInt(dialog_totalPrice.getText().toString().substring(0, dialog_totalPrice.getText().toString().length() - 1)) - Integer.parseInt(optionPrice.getText().toString().substring(1, optionPrice.length() - 1));
        return comma_won(price);
    }

    static public String checkRadio(TextView dialog_totalPrice, TextView optionPrice) {
        int price = Integer.parseInt(dialog_totalPrice.getText().toString().substring(0, dialog_totalPrice.getText().toString().length() - 1)) + Integer.parseInt(optionPrice.getText().toString().substring(1, optionPrice.length() - 1));
        return comma_won(price);
    }


}
