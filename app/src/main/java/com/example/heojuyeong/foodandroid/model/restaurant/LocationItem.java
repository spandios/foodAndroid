package com.example.heojuyeong.foodandroid.model.restaurant;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 9. 23..
 */

public class LocationItem extends RealmObject{
    @PrimaryKey
    int id=1;
    String locationName;
    double lat;
    double lng;

    public LocationItem(){

    }
    public LocationItem(String locationName, double lat, double lng) {
        this.locationName = locationName;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
}
