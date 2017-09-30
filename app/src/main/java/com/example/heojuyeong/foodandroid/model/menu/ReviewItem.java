package com.example.heojuyeong.foodandroid.model.menu;

/**
 * Created by heojuyeong on 2017. 9. 27..
 */

public class ReviewItem {
    int menu_review_id;
    String created_at;
    double rating;
    String image;
    String content;

    int user_id;
    String profile_image;
    String provider;
    String name;

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getProvider() {
        return provider;
    }

    public double getRating() {
        return rating;
    }

    public int getMenu_review_id() {
        return menu_review_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

}
