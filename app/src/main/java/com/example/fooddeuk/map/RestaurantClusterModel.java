package com.example.fooddeuk.map;

import com.example.fooddeuk.menu.model.MenuCategory;
import com.example.fooddeuk.restaurant.model.Restaurant;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import java.util.ArrayList;

/**
 * Created by heo on 2018. 3. 22..
 */

public class RestaurantClusterModel implements ClusterItem{
  public LatLng location;
  public String _id;
  public String rest_id;
  public String name;
  public String tel;
  public String address;
  public ArrayList<String> picture;
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
  public ArrayList<MenuCategory> menuCategory;

  public RestaurantClusterModel(Restaurant restaurant){
    location=new LatLng(restaurant.lat,restaurant.lng);
    _id=restaurant._id;
    rest_id=restaurant.rest_id;
    name=restaurant.name;
    rating=restaurant.rating;
    tel=restaurant.tel;
    address=restaurant.address;
    picture=new ArrayList<>();
    picture.addAll(restaurant.picture);
    openTime=restaurant.openTime;
    closeTime=restaurant.closeTime;
    holiday=restaurant.holiday;
    dangolCnt= restaurant.dangolCnt;
    likeCnt=restaurant.likeCnt;
    reviewCnt=restaurant.reviewCnt;
    avg_cooking_time=restaurant.avg_cooking_time;
    discount=restaurant.discount;
    owner_id=restaurant.owner_id;
    discount=restaurant.discount;
    distance=restaurant.distance;
    orderCnt=restaurant.orderCnt;
    lat=restaurant.lat;
    lng=restaurant.lng;
    menuCategory=restaurant.menuCategory;
  }
  @Override
  public LatLng getPosition() {
    return location;
  }
}
