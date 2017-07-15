package com.example.heojuyeong.foodandroid.common;

import android.app.Application;
import android.content.Context;

import com.example.heojuyeong.foodandroid.http.GeocodingService;
import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CommonLocationApplication extends Application {
    private String locationName;
    private double lat;
    private double lng;
    private String latlng;
    private GPS_Util gps;
    private GeocodingService geocodingService;

    private String tempLocationName;
    private double tempLat;
    private double tempLng;
    private String tempLatlng;


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
    public void settingLocation(LatLng latLng){
        try{
            tempLat=latLng.latitude;
            tempLng=latLng.longitude;
            tempLatlng=String.valueOf(tempLat)+","+String.valueOf(tempLng);
            Retrofit client=new Retrofit.Builder().baseUrl("http://maps.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build();
            GeocodingService.GeocodingInterface service=client.create(GeocodingService.GeocodingInterface.class);
            Call<GeocodingService> call=service.get_location_name_retrofit(tempLatlng);
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
                            tempLocationName=sb.toString();
                        }else{
                            tempLocationName="위치정보 실패 ";
                            Logger.d("google map api status fail ");
                        }
                    }else{
                        tempLocationName="위치정보 실패 ";
                        Logger.d("response error");
                    }
                }



                @Override
                public void onFailure(Call<GeocodingService> call, Throwable t) {
                    Logger.d("Fail connect url");
                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
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
                                sb.append(geocodingService.getResults().get(3).getAddressComponentsList().get(1).getLong_name());
                                sb.append(" ");
                                sb.append(geocodingService.getResults().get(3).getAddressComponentsList().get(0).getLong_name());

                                locationName=sb.toString();
                                Logger.d(sb.toString());

                            }else{
                                locationName="다시 시도 ";
                                Logger.d("google map api status fail ");
                            }
                        }else{
                            locationName="다시시도";
                            Logger.d("response error");
                        }

                    }

                    @Override
                    public void onFailure(Call<GeocodingService> call, Throwable t) {
                        Logger.d("Fail connect url");
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
    public String getTempLocationName(){return tempLocationName;}
    public String getLocationName() {
        return locationName;
    }

    public void settingTemptoValue(){
        this.lat=this.tempLat;
        this.lng=this.tempLng;
        this.latlng=this.latlng;
        this.locationName=this.tempLocationName;
    }
    public double getLat() {
        return lat;
    }



    public double getLng() {
        return lng;
    }


}
