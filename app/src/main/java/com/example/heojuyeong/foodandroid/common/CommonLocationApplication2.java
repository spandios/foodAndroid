package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;

import com.example.heojuyeong.foodandroid.http.GeocodingService;
import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heojuyeong on 2017. 7. 12..
 */

public class CommonLocationApplication2 extends Application {
    private String locationName;
    private double lat;
    private double lng;
    private String latlng;
    private GPS_Util gps;
    private GeocodingService geocodingService;

    @Override
    public void onCreate() {
        locationName="";
        latlng="";
        gps=null;
        geocodingService=null;
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void settingLocation(Context context){
        try{
            gps=new GPS_Util(context);
            if(gps.isGetLocation()){
                lat=gps.getLatitude();
                lng=gps.getLongitude();
                latlng=String.valueOf(gps.getLatitude())+","+String.valueOf(gps.getLongitude());
                Retrofit client=new Retrofit.Builder().baseUrl("http://maps.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build();
                GeocodingService.GeocodingInterface service=client.create(GeocodingService.GeocodingInterface.class);
                Call<GeocodingService> call=service.get_location_name_retrofit(latlng);
                call.enqueue(new Callback<GeocodingService>() {
                    @Override
                    public void onResponse(Call<GeocodingService> call, Response<GeocodingService> response) {
                        if(response.isSuccessful()){
                            geocodingService=response.body();
                            if(geocodingService.getStatus().equals("OK")){

                                StringBuilder sb = new StringBuilder();
                                sb.append(geocodingService.getResults().get(4).getAddressComponentsList().get(1).getLong_name());
                                sb.append(" ");
                                sb.append(geocodingService.getResults().get(4).getAddressComponentsList().get(0).getLong_name());
                                if(sb.toString()==null){
                                    locationName="올바르지 않는 이름";
                                }
                                locationName=sb.toString();

                                Logger.d(sb.toString());

                            }else{
                                Logger.d("google map api status fail ");
                            }
                        }else{
                            Logger.d("response error");
                        }

                    }

                    @Override
                    public void onFailure(Call<GeocodingService> call, Throwable t) {
                        Logger.d("Fail Call");
                    }
                });


                if(locationName==null){
                    locationName="올바르지 않은 이름";
                }
            }else{
                gps.showSettingAlert();
            }






        }catch (Exception e){
            e.printStackTrace();
        }




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
