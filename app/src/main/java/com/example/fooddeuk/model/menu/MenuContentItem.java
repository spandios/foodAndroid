package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */
@Parcel
public class MenuContentItem {
    @SerializedName("_id")
    public String menu_id;
    public String name;
    public String price;
    public String description;
    public String avgtime;
    public String picture;
    public String rating;
    public int viewCnt;
    public int likeCnt;
    public int reviewCnt;
    public ArrayList<OptionItem> option;


    public String getMenu_id() {
        return menu_id;
    }

    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }

    public String getAvgtime() {
        return avgtime;
    }

    public String getPicture() {
        return picture;
    }

    public String getRating() {
        return rating;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public int getReviewCnt() {
        return reviewCnt;
    }

    public ArrayList<OptionItem> getOption() {
        return option;
    }
}




