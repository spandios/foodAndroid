package com.example.heojuyeong.foodandroid.http;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class GeocodingService {

    @SerializedName("status")
    String status;
    @SerializedName("results")
    List<Results> results = new ArrayList<>();


    public String getStatus() {
        return status;
    }

    public List<Results> getResults() {
        return results;
    }

    public class Results {
        @SerializedName("address_components")
        public List<address_components> addressComponentsList = new ArrayList<>();
        @SerializedName("formatted_address")
        public String formatted_address;

        public String getFormatted_address() {
            return formatted_address;
        }

        public List<address_components> getAddressComponentsList() {
            return addressComponentsList;
        }

        public class address_components {
            @SerializedName("long_name")
            String long_name;
            @SerializedName("short_name")
            String short_name;
            @SerializedName("types")
            ArrayList<String> types = new ArrayList<>();

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

    public interface GeocodingInterface {
        @GET("maps/api/geocode/json?language=ko")
        Call<GeocodingService> get_location_name_retrofit(@Query("latlng") String latlng);
    }

    public static class Geolcation {

        public static Call<GeocodingService> getCall(LatLng latLng) {
            String latlng = String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude);
            Retrofit client = new Retrofit.Builder().baseUrl("http://maps.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build();
            GeocodingService.GeocodingInterface service = client.create(GeocodingService.GeocodingInterface.class);
            Call<GeocodingService> call = service.get_location_name_retrofit(latlng);
            return call;
        }

        public static Call<GeocodingService> getCall(String latLng) {
            Retrofit client = new Retrofit.Builder().baseUrl("http://maps.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build();
            GeocodingService.GeocodingInterface service = client.create(GeocodingService.GeocodingInterface.class);
            Call<GeocodingService> call = service.get_location_name_retrofit(latLng);
            return call;
        }

        public static String getLocationName(Call<GeocodingService> call) {
            String locationName = null;
            try {
                GeocodingService geocodingService = call.execute().body();
                if (geocodingService.getStatus().equals("OK")) {
                    locationName = geocodingService.getResults().get(3).formatted_address.substring(11);
                    return locationName;


                } else {
                    Logger.d("GOOGLE GEOCODER SERIVCE ERROR");
                    locationName = "google geocoder ";
                    return locationName;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return locationName;
        }
    }
}





