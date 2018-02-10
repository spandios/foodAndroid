package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

import io.realm.annotations.Ignore;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */
@Parcel
public class Menu {
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
    @Ignore
    public ArrayList<OptionCategory> option;

    public Menu(){

    }

    public Menu(String menu_id, String name, String price, String description, String avgtime, String picture, String rating, int viewCnt, int likeCnt, int reviewCnt, ArrayList<OptionCategory> option) {
        this.menu_id = menu_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.avgtime = avgtime;
        this.picture = picture;
        this.rating = rating;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.reviewCnt = reviewCnt;
        this.option = option;
    }

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

    public ArrayList<OptionCategory> getOption() {
        return option;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvgtime(String avgtime) {
        this.avgtime = avgtime;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void setReviewCnt(int reviewCnt) {
        this.reviewCnt = reviewCnt;
    }

    public void setOption(ArrayList<OptionCategory> option) {
        this.option = option;
    }
}




