package com.example.fooddeuk.model.restaurant;

import com.example.fooddeuk.model.menu.MenuCategory;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heo on 2018. 2. 4..
 */
public class Restaurant  extends RealmObject implements Serializable{

    @PrimaryKey
    int fixedPrimarykey=0;
    public String _id;
    public String rest_id;
    public String name;
    public String tel;
    public String address;
    @SerializedName("picture")
    public String picture;
    public String open_time;
    public String close_time;
    public String holiday;
    public float rating;
    public String description;
    public int dangolCnt;
    public int likeCnt;
    public int reviewCnt;
    public String avg_cooking_time;
    public String discount;
    @SerializedName("rest_admin_id")
    public int rest_admin_id;
    public String distance;
    public int orderCnt;
    public double lat;
    public double lng;
    @Ignore
    @SerializedName("menu")
    public ArrayList<MenuCategory> menuCategory;



    public Restaurant(){

    }

    public Restaurant(String rest_id, String name, String tel, String address, String picture, String open_time, String close_time, String holiday, float rating, String description, int dangolCnt, int likeCnt, int reviewCnt, String avg_cooking_time, String discount, int rest_admin_id, String distance, int orderCnt, double lat, double lng, ArrayList<MenuCategory> menuCategory) {
        this.rest_id = rest_id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.picture = picture;
        this.open_time = open_time;
        this.close_time = close_time;
        this.holiday = holiday;
        this.rating = rating;
        this.description = description;
        this.dangolCnt = dangolCnt;
        this.likeCnt = likeCnt;
        this.reviewCnt = reviewCnt;
        this.avg_cooking_time = avg_cooking_time;
        this.discount = discount;
        this.rest_admin_id = rest_admin_id;
        this.distance = distance;
        this.orderCnt = orderCnt;
        this.lat = lat;
        this.lng = lng;
        this.menuCategory = menuCategory;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDangolCnt() {
        return dangolCnt;
    }

    public void setDangolCnt(int dangolCnt) {
        this.dangolCnt = dangolCnt;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getReviewCnt() {
        return reviewCnt;
    }

    public void setReviewCnt(int reviewCnt) {
        this.reviewCnt = reviewCnt;
    }

    public String getAvg_cooking_time() {
        return avg_cooking_time;
    }

    public void setAvg_cooking_time(String avg_cooking_time) {
        this.avg_cooking_time = avg_cooking_time;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getRest_admin_id() {
        return rest_admin_id;
    }

    public void setRest_admin_id(int rest_admin_id) {
        this.rest_admin_id = rest_admin_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getOrderCnt() {
        return orderCnt;
    }

    public void setOrderCnt(int orderCnt) {
        this.orderCnt = orderCnt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ArrayList<MenuCategory> getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(ArrayList<MenuCategory> menuCategory) {
        this.menuCategory = menuCategory;
    }
}