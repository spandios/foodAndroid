package com.example.fooddeuk.model.user;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 10. 6..
 */

public class UserItemRealm extends RealmObject{
    @PrimaryKey
    int id=1;

    public int user_id;
    public String email;
    public String provider_id;
    public String user_name;
    public String fcm_token;
    public String phone;
    public String profile_image;
    public String provider;
    public UserItemRealm() {
    }
    public UserItemRealm(int user_id, String email, String provider_id, String user_name, String fcm_token,String providero) {
        this.user_id = user_id;
        this.email = email;
        this.provider_id = provider_id;
        this.user_name = user_name;
        this.fcm_token = fcm_token;
        this.phone = "";
        this.profile_image = "";
        this.provider=provider;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
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
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", fcm_token='" + fcm_token + '\'' +
                ", phone='" + phone + '\'' +
                ", profile_image='" + profile_image + '\'' +
                '}';
    }
}

