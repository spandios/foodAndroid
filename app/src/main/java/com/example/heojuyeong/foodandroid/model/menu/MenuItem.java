package com.example.heojuyeong.foodandroid.model.menu;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuItem  {
    int menu_id;
    int rest_id;
    String name;
    int price;
    String description;
    String avgtime;
    String menupicture;
    String rating;
    int viewcount;
    int likecount;
    int review_count;



    public int getMenu_id() {
        return menu_id;
    }

    public int getRest_id(){
        return rest_id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getAvgtime() {
        return avgtime;
    }

    public String getMenupicture() {
        return menupicture;
    }

    public String getRating() {
        return rating;
    }


    public int getViewcount() {
        return viewcount;
    }

    public int getLikecount() {
        return likecount;
    }


    public int getReview_count() {
        return review_count;
    }
}




