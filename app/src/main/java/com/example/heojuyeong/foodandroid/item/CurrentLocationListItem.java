package com.example.heojuyeong.foodandroid.item;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;


public class CurrentLocationListItem  {
    @SerializedName("restaurants")
    ArrayList<Restaurant> restaurants;
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String errorMessage;
    public int getStatus(){return status;}
    public String getErrorMessage(){return errorMessage;}
    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }
    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Parcel
    public static class Restaurant {


        @SerializedName("rest_id")
        int rest_id;

        @SerializedName("name")
        String name;

        @SerializedName("tel")
        String tel;

        @SerializedName("address")
        String address;

        @SerializedName("restpicture")
        String rest_picture;

        @SerializedName("openhour")
        String openhour;

        @SerializedName("holiday")
        String holiday;

        @SerializedName("rating")
        double rating;

        @SerializedName("likes")
        int likes;

        @SerializedName("reviewcount")
        int reviewcount;

        @SerializedName("avg_cooking_time")
        String avg_cooking_time;

        @SerializedName("discount")
        String discount;

        @SerializedName("rest_admin_id")
        int rest_admin_id;


        @SerializedName("distance")
        String distance;



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

        public String getOpenhour() {
            return openhour;
        }

        public String getHoliday() {
            return holiday;
        }

        public double getRating() {
            return rating;
        }

        public int getLikes() {
            return likes;
        }

        public int getRest_admin_id() {
            return rest_admin_id;
        }

        public String getAvg_cooking_time() {
            return avg_cooking_time;
        }

        public String getDiscount(){return discount;}
        public void setAvg_cooking_time(String avg_cooking_time) {
            this.avg_cooking_time = avg_cooking_time;
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

        public void setOpenhour(String openhour) {
            this.openhour = openhour;
        }

        public void setHoliday(String holiday) {
            this.holiday = holiday;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public void setReviewcount(int reviewcount) {
            this.reviewcount = reviewcount;
        }

        public void setRest_admin_id(int rest_admin_id) {
            this.rest_admin_id = rest_admin_id;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

    }


}
