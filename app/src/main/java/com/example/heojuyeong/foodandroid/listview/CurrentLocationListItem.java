package com.example.heojuyeong.foodandroid.listview;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class CurrentLocationListItem  {
    @SerializedName("restaurants")
    private ArrayList<Restaurant> restaurants;
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
    public class Restaurant implements Serializable{


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

        public void setDistance(double distance) {
            this.distance = distance;
        }

    }


}
