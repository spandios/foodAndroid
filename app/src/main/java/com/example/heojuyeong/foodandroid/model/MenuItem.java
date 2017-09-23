package com.example.heojuyeong.foodandroid.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */
@Parcel
public class MenuItem  {

    @SerializedName("menu_id")
    int menu_id;

    @SerializedName("rest_id")
    int rest_id;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    int price;

    @SerializedName("description")
    String description;

    @SerializedName("avgtime")
    String avgtime;

    @SerializedName("menupicture")
    String menupicture;

    @SerializedName("rating")
    String rating;


    @SerializedName("reviews")
    ArrayList<Review> reviews;


    @Parcel
    public static class Review {
        @SerializedName("menu_review_id")
        int menu_review_id;

        @SerializedName("created_at")
        String created_at;
        @SerializedName("rating")
        double rating;
        @SerializedName("image")
        String image;

        @SerializedName("content")
        String content;


        @SerializedName("reviewer")
        Reviewer reviewer;


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

        public Reviewer getReviewer() {
            return reviewer;
        }
        @Parcel
        public static class Reviewer {
            @SerializedName("user_id")
            int user_id;

            @SerializedName("profile_image")
            String profile_image;

            @SerializedName("provider")
            String provider;

            @SerializedName("name")
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
        }



    }

    @SerializedName("options")
    ArrayList<Options> options=new ArrayList<>();




    @SerializedName("viewcount")
    int viewcount;

    @SerializedName("likecount")
    int likecount;




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





    public ArrayList<Options> getOptions() {
        return options;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public int getViewcount() {
        return viewcount;
    }

    public int getLikecount() {
        return likecount;
    }

    @Parcel
    public static class Options {
        @SerializedName("menu_option_category_id")
        int menu_option_category_id;
        @SerializedName("menu_category_name")
        String menu_category_name;
        @SerializedName("necessary")
        int necessary;

        @SerializedName("multiple")
        int multiple;

        @SerializedName("option")
        ArrayList<Option> option;

        public int getMenu_option_category_id() {
            return menu_option_category_id;
        }

        public String getMenu_category_name() {
            return menu_category_name;
        }

        public int getNecessary() {
            return necessary;
        }

        public int getMultiple(){
            return multiple;
        }

        public ArrayList<Option> getOption() {
            return option;
        }

        @Parcel
        public static class Option {
            @SerializedName("menu_option_id")
            int menu_option_id;
            @SerializedName("menu_option_name")
            String menu_option_name;
            @SerializedName("menu_option_price")
            int menu_option_price;
            @SerializedName("menu_option_description")
            String menu_option_description;

            public int getMenu_option_id() {
                return menu_option_id;
            }

            public String getMenu_option_name() {
                return menu_option_name;
            }

            public int getMenu_price() {
                return menu_option_price;
            }

            public String getMenu_description() {
                return menu_option_description;
            }
        }

    }



}
