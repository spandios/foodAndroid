package com.example.fooddeuk.model.menu;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 30..
 */
@Parcel
public class MenuResponse {
    @SerializedName("results")
    ArrayList<MenuCategory> results;

    public ArrayList<MenuCategory> getResults() {
        return results;
    }

}

