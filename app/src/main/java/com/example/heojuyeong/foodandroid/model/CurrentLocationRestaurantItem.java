package com.example.heojuyeong.foodandroid.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;


public class CurrentLocationRestaurantItem {
    ArrayList<Restaurant> restaurants;
    private int status;
    private String errorMessage;
    public int getStatus(){return status;}
    public String getErrorMessage(){return errorMessage;}
    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    @Parcel
    public static class Restaurant  {
        int rest_id;
        String name;
        String tel;
        String address;
        @SerializedName("restpicture")
        String rest_picture;
        String openhour;
        String holiday;
        double rating;
        int likes;
        int reviewcount;
        String avg_cooking_time;
        String discount;
        int rest_admin_id;
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



    }


}
