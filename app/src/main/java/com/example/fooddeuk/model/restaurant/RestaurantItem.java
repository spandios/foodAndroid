package com.example.fooddeuk.model.restaurant;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;


public class RestaurantItem {
    ArrayList<Restaurant> restaurants;
    private String status;
    private String errorMessage;
    public String getStatus(){return status;}
    public String getErrorMessage(){return errorMessage;}
    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    @Parcel
    public static class Restaurant  {

        Restaurant(){

        }
        public void setRest_id(int rest_id) {
            this.rest_id = rest_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setRest_picture(String rest_picture) {
            this.rest_picture = rest_picture;
        }

        public void setOpen_time(String open_time) {
            this.open_time = open_time;
        }

        public void setClose_time(String close_time) {
            this.close_time = close_time;
        }

        public void setHoliday(String holiday) {
            this.holiday = holiday;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public void setReviewcount(int reviewcount) {
            this.reviewcount = reviewcount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public void setRest_admin_id(String rest_admin_id) {
            this.rest_admin_id = rest_admin_id;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public void setLatlng(double[] latlng) {
            this.latlng = latlng;
        }

        public Restaurant(int rest_id, String name, String tel, String address, String rest_picture, String open_time, String close_time, String holiday, float rating, int likes, int reviewcount, String avg_cooking_time, String discount, String rest_admin_id, String distance, double[] latlng) {
            this.rest_id = rest_id;
            this.name = name;
            this.tel = tel;
            this.address = address;
            this.rest_picture = rest_picture;
            this.open_time = open_time;
            this.close_time = close_time;
            this.holiday = holiday;
            this.rating = rating;
            this.likes = likes;
            this.reviewcount = reviewcount;
            this.avg_cooking_time = avg_cooking_time;
            this.discount = discount;
            this.rest_admin_id = rest_admin_id;
            this.distance = distance;
            this.latlng = latlng;
        }

        public int rest_id;
        public String name;
        public String tel;
        public String address;
        @SerializedName("restpicture")
        public String rest_picture;
        public String open_time;
        public String close_time;
        public String holiday;
        public float rating;
        public int likes;
        public int reviewcount;
        public String avg_cooking_time;
        public String discount;
        public String rest_admin_id;
        public String distance;
        public double[] latlng=new double[1];

        public double[] getLatlng() {
            return latlng;
        }

        public int getReviewcount(){
            return reviewcount;
        }
        public String getDistance(){
            return distance;
        }
        public int getRest_id() {
            return rest_id;
        }

        public String getName() {
            return name;
        }

        public String getTel() {
            return tel;
        }

        public String getAddress() {
            return address;
        }

        public String getRest_picture() {
            return rest_picture;
        }


        public String getOpen_time() {
            return open_time;
        }

        public String getClose_time() {
            return close_time;
        }

        public String getHoliday() {
            return holiday;
        }

        public float getRating() {
            return rating;
        }

        public int getLikes() {
            return likes;
        }

        public String getRest_admin_id() {
            return rest_admin_id;
        }

        public String getAvg_cooking_time() {
            return avg_cooking_time;
        }

        public String getDiscount(){return discount;}
        public void setAvg_cooking_time(String avg_cooking_time) {
            this.avg_cooking_time = avg_cooking_time;
        }


    }


}
