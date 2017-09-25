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
    int rest_id;
    String name;
    int price;
    String description;
    String avgtime;
    String menupicture;
    String rating;
    ArrayList<Review> reviews;
    @Parcel
    public static class Review {
        int menu_review_id;
        String created_at;
        double rating;
        String image;
        String content;
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
            int user_id;
            String profile_image;
            String provider;
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


    ArrayList<Options> options=new ArrayList<>();
    int viewcount;
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
        int menu_option_category_id;
        String menu_category_name;
        int necessary;
        int multiple;
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
            int menu_option_id;
            String menu_option_name;
            int menu_option_price;
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
