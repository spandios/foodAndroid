package com.example.heojuyeong.foodandroid.listview;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 18..
 */

public class CurrentLocationListItem {
    @SerializedName("restaurants")
    private ArrayList<Restaurant> restaurants;

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public class Restaurant {
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
        int rating;

        @SerializedName("likes")
        int likes;
        @SerializedName("reviewcount")
        int reviewcount;

        @SerializedName("rest_admin_id")
        int rest_admin_id;

        @SerializedName("distance")
        double distance;

        public int getReviewcount(){
            return reviewcount;
        }
        public double getDistance(){
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

        public int getRating() {
            return rating;
        }

        public int getLikes() {
            return likes;
        }

        public int getRest_admin_id() {
            return rest_admin_id;
        }
    }


}
