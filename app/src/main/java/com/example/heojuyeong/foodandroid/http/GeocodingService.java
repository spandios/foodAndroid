package com.example.heojuyeong.foodandroid.http;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class GeocodingService {

    @SerializedName("status")
    String status;
    @SerializedName("results")
    List<Results> results=new ArrayList<>();


    public String getStatus() {
        return status;
    }
    public List<Results>getResults(){
        return results;
    }

    public class Results {
        @SerializedName("address_components")
        public List<address_components> addressComponentsList = new ArrayList<>();
        @SerializedName("formatted_address")
        public String formatted_address;

        public String getFormatted_address(){
            return formatted_address;}

        public List<address_components> getAddressComponentsList() {
            return addressComponentsList;
        }

        public class address_components {
            @SerializedName("long_name")
            String long_name;
            @SerializedName("short_name")
            String short_name;
            @SerializedName("types")
            ArrayList<String> types=new ArrayList<>();

            public String getLong_name() {
                return long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public ArrayList<String> getTypes() {
                return types;
            }
        }

    }

    public interface GeocodingInterface{
        @GET("maps/api/geocode/json?language=ko")
        Call<GeocodingService> get_location_name_retrofit(@Query("latlng") String latlng);
    }


}


