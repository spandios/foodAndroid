package com.example.fooddeuk.model.menu;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 28..
 */

public class OptionItem {
    public int menu_option_category_id;
    public String menu_option_category_name;
    public int necessary;
    public int multiple;
    public ArrayList<Option> option;

    public int getMenu_option_category_id() {
        return menu_option_category_id;
    }

    public String getMenu_option_category_name() {
        return menu_option_category_name;
    }

    public int getNecessary() {
        return necessary;
    }

    public int getMultiple(){
        return multiple;
    }

    public ArrayList<Option> getOption() {
        return option;
    }


    public class Option {
        public int menu_option_id;
        public String menu_option_name;
        public String menu_option_price;
        public String menu_option_description;

        public int getMenu_option_id() {
            return menu_option_id;
        }

        public String getMenu_option_name() {
            return menu_option_name;
        }

        public String getMenu_price() {
            return menu_option_price;
        }

        public String getMenu_description() {
            return menu_option_description;
        }
    }



}
