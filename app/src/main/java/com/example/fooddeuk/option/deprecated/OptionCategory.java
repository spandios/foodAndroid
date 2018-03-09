package com.example.fooddeuk.option.deprecated;

import com.example.fooddeuk.option.Option;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 28..
 */

public class OptionCategory implements Serializable {
    @SerializedName("_id")
    public String menu_option_category_id;
    public String menu_option_category_name;
    public boolean multiple;
    public ArrayList<Option> option_content;

    public OptionCategory(){

    }

}
