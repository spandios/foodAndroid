package com.example.fooddeuk.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heojuyeong on 2017. 9. 18..
 */


public class UserItem {

    @SerializedName("exist")
    public boolean exist;

    @SerializedName("user")
    public User user;

    public UserItem(){

    }
    public UserItem(boolean exist, User user) {
        this.exist = exist;
        this.user = user;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserItem{" +
                "exist=" + exist +
                ", user=" + user +
                '}';
    }

    public class User {
        public int user_id;
        public String email;
        public String provider_id;
        @SerializedName("name")
        public String user_name;
        public String fcm_token;
        public String phone;
        public String profile_image;
        public String provider;

        public User(int user_id, String email, String provider_id, String user_name, String fcm_token,String provider) {
            this.user_id = user_id;
            this.email = email;
            this.provider_id = provider_id;
            this.user_name = user_name;
            this.fcm_token = fcm_token;
            this.provider=provider;
            this.phone = "";
            this.profile_image = "";
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





}
