package com.example.fooddeuk.model.menu;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuItem  {
    public int menu_id;
    public int rest_id;
    public String name;
    public int price;
    public String description;
    public String avgtime;
    public String menupicture;
    public String rating;
    public int viewcount;
    public int likecount;
    public int review_count;



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

    @Override
    public String toString() {
        return "MenuItem{" +
                "menu_id=" + menu_id +
                ", rest_id=" + rest_id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", avgtime='" + avgtime + '\'' +
                ", menupicture='" + menupicture + '\'' +
                ", rating='" + rating + '\'' +
                ", viewcount=" + viewcount +
                ", likecount=" + likecount +
                ", review_count=" + review_count +
                '}';
    }
}




