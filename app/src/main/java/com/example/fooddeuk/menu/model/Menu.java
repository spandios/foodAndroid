package com.example.fooddeuk.menu.model;

import com.example.fooddeuk.option.deprecated.OptionCategory;
import com.google.gson.annotations.SerializedName;
import io.realm.annotations.Ignore;
import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class Menu {

  @SerializedName("_id")
  public String menu_id;
  public String name;
  public String price;
  public String description;
  public String avgtime;
  public ArrayList<String> picture;
  public String rating;
  public int viewCnt;
  public int likeCnt;
  public int reviewCnt;
  public String distance;
  @Ignore
  public ArrayList<OptionCategory> option;

  public Menu() {

  }

  public Menu(String menu_id, String name, String price, String description, String avgtime,
      String rating, int viewCnt, int likeCnt, int reviewCnt, ArrayList<OptionCategory> option) {
    this.menu_id = menu_id;
    this.name = name;
    this.price = price;
    this.description = description;
    this.avgtime = avgtime;
    this.rating = rating;
    this.viewCnt = viewCnt;
    this.likeCnt = likeCnt;
    this.reviewCnt = reviewCnt;
    this.option = option;
  }

  public String getMenu_id() {
    return menu_id;
  }

  public void setMenu_id(String menu_id) {
    this.menu_id = menu_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAvgtime() {
    return avgtime;
  }

  public void setAvgtime(String avgtime) {
    this.avgtime = avgtime;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public int getViewCnt() {
    return viewCnt;
  }

  public void setViewCnt(int viewCnt) {
    this.viewCnt = viewCnt;
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

  public ArrayList<OptionCategory> getOption() {
    return option;
  }

  public void setOption(ArrayList<OptionCategory> option) {
    this.option = option;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}




