package com.example.fooddeuk.user;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heo on 2018. 1. 31..
 */

public class User extends RealmObject{
    @PrimaryKey
    public int fixedPrimaryKey=0;
    public String user_id;
    public String email;
    public String provider_id;
    @SerializedName("name")
    public String user_name;
    public String fcm_token;
    public String phone;
    public String profile_image;
    public String provider;
    public RealmList<String> rest_id;

    public User(){

    }

    public User(String email, String provider_id, String user_name, String provider,String profile_image) {
        this.email = email;
        this.provider_id = provider_id;
        this.user_name = user_name;
        this.provider=provider;
        this.phone = "";
        this.profile_image = profile_image;
        this.rest_id=new RealmList<>();
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
