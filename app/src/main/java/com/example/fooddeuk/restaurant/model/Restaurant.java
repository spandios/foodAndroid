package com.example.fooddeuk.restaurant.model;

import com.example.fooddeuk.map.RestaurantClusterModel;
import com.example.fooddeuk.menu.model.MenuCategory;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import java.util.ArrayList;

/**
 * Created by heo on 2018. 2. 4..
 */
public class Restaurant extends RealmObject {

    public String _id;
    public String rest_id;
    public String name;
    public String tel;
    public String address;
    @SerializedName("picture")
    public RealmList<String> picture;
    public String openTime;
    public String closeTime;
    public String holiday;
    public float rating;
    public String description;
    public int dangolCnt;
    public int likeCnt;
    public int reviewCnt;
    public String avg_cooking_time;
    public String discount;
    public int owner_id;
    public String distance;
    public int orderCnt;
    public double lat;
    public double lng;
    @Ignore
    @SerializedName("menu")
    public ArrayList<MenuCategory> menuCategory;
  @PrimaryKey
  int fixedPrimarykey = 0;





    public Restaurant(){

    }
    public Restaurant(RestaurantClusterModel restaurantClusterModel){
      _id=restaurantClusterModel._id;
      rest_id=restaurantClusterModel.rest_id;
      name=restaurantClusterModel.name;
      tel=restaurantClusterModel.tel;
      address=restaurantClusterModel.address;
      picture=new RealmList<>();
      picture.addAll(restaurantClusterModel.picture);
      openTime=restaurantClusterModel.openTime;
      closeTime=restaurantClusterModel.closeTime;
      holiday=restaurantClusterModel.holiday;
      rating=restaurantClusterModel.rating;
      description=restaurantClusterModel.description;
      dangolCnt=restaurantClusterModel.dangolCnt;
      likeCnt=restaurantClusterModel.likeCnt;
      reviewCnt=restaurantClusterModel.reviewCnt;
      avg_cooking_time=restaurantClusterModel.avg_cooking_time;
      discount=restaurantClusterModel.discount;
      owner_id=restaurantClusterModel.owner_id;
      distance=restaurantClusterModel.distance;
      orderCnt=restaurantClusterModel.orderCnt;
      lat=restaurantClusterModel.lat;
      lng=restaurantClusterModel.lng;
      menuCategory=restaurantClusterModel.menuCategory;

    }

  public Restaurant(String rest_id, String name, String tel, String address,
      RealmList<String> picture, String openTime, String closeTime, String holiday, float rating,
      String description, int dangolCnt, int likeCnt, int reviewCnt, String avg_cooking_time,
      String discount, int owner_id, String distance, int orderCnt, double lat, double lng,
      ArrayList<MenuCategory> menuCategory) {
        this.rest_id = rest_id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.picture = picture;
        this.openTime=openTime;
        this.closeTime=closeTime;
        this.holiday = holiday;
        this.rating = rating;
        this.description = description;
        this.dangolCnt = dangolCnt;
        this.likeCnt = likeCnt;
        this.reviewCnt = reviewCnt;
        this.avg_cooking_time = avg_cooking_time;
        this.discount = discount;
        this.owner_id = owner_id;
        this.distance = distance;
        this.orderCnt = orderCnt;
        this.lat = lat;
        this.lng = lng;
        this.menuCategory = menuCategory;
    }

  public int getFixedPrimarykey() {
    return fixedPrimarykey;
  }

  public void setFixedPrimarykey(int fixedPrimarykey) {
    this.fixedPrimarykey = fixedPrimarykey;
  }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

  public String getRest_id() {
    return rest_id;
  }

  public void setRest_id(String rest_id) {
    this.rest_id = rest_id;
  }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

  public RealmList<String> getPicture() {
        return picture;
    }

  public void setPicture(RealmList<String> picture) {
        this.picture = picture;
    }

  public String getOpenTime() {
    return openTime;
  }

  public void setOpenTime(String openTime) {
    this.openTime = openTime;
  }

  public String getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(String closeTime) {
    this.closeTime = closeTime;
  }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDangolCnt() {
        return dangolCnt;
    }

    public void setDangolCnt(int dangolCnt) {
        this.dangolCnt = dangolCnt;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getReviewCnt() {
        return reviewCnt;
    }

    public void setReviewCnt(int reviewCnt) {
        this.reviewCnt = reviewCnt;
    }

    public String getAvg_cooking_time() {
        return avg_cooking_time;
    }

    public void setAvg_cooking_time(String avg_cooking_time) {
        this.avg_cooking_time = avg_cooking_time;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getOrderCnt() {
        return orderCnt;
    }

    public void setOrderCnt(int orderCnt) {
        this.orderCnt = orderCnt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ArrayList<MenuCategory> getMenuCategory() {
        return menuCategory;
    }

  public void setMenuCategory(
      ArrayList<MenuCategory> menuCategory) {
        this.menuCategory = menuCategory;
    }

  @Override
  public String toString() {
    return "Restaurant{" +
        "_id='" + _id + '\'' +
        ", rest_id='" + rest_id + '\'' +
        ", name='" + name + '\'' +
        ", tel='" + tel + '\'' +
        ", address='" + address + '\'' +
        ", picture=" + picture +
        ", openTime='" + openTime + '\'' +
        ", closeTime='" + closeTime + '\'' +
        ", holiday='" + holiday + '\'' +
        ", rating=" + rating +
        ", description='" + description + '\'' +
        ", dangolCnt=" + dangolCnt +
        ", likeCnt=" + likeCnt +
        ", reviewCnt=" + reviewCnt +
        ", avg_cooking_time='" + avg_cooking_time + '\'' +
        ", discount='" + discount + '\'' +
        ", owner_id=" + owner_id +
        ", distance='" + distance + '\'' +
        ", orderCnt=" + orderCnt +
        ", lat=" + lat +
        ", lng=" + lng +
        ", menuCategory=" + menuCategory +
        ", fixedPrimarykey=" + fixedPrimarykey +
        '}';
  }
}