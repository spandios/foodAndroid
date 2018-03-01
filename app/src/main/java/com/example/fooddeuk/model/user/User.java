package com.example.fooddeuk.model.user;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heo on 2018. 1. 31..
 */

public class User extends RealmObject{
    @PrimaryKey
    public int fixedPrimaryKey=0;
    @SerializedName("_id")
    public String user_id;
    public String email;
    public String provider_id;
    @SerializedName("name")
    public String user_name;
    public String fcm_token;
    public String phone;
    public String profile_image;
    public String provider;

    public User(){

    }
    public User(String email, String provider_id, String user_name, String provider,String profile_image) {
        this.email = email;
        this.provider_id = provider_id;
        this.user_name = user_name;
        this.provider=provider;
        this.phone = "";
        this.profile_image = profile_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", fcm_token='" + fcm_token + '\'' +
                ", phone='" + phone + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }
}
