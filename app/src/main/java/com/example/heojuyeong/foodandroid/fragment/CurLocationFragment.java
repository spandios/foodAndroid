package com.example.heojuyeong.foodandroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.common.CommonLocationApplication;
import com.example.heojuyeong.foodandroid.http.CurrentLocationListSerivce;
import com.example.heojuyeong.foodandroid.http.GeocodingService;
import com.example.heojuyeong.foodandroid.listview.CurrentLocationListAdapter;
import com.example.heojuyeong.foodandroid.listview.CurrentLocationListItem;
import com.example.heojuyeong.foodandroid.settingLocationMapActivity;
import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.orhanobut.logger.Logger;

import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurLocationFragment extends Fragment {
    
    private TextView currentLocationTextview;
    private ListView currentLocationListView;
    GPS_Util gps_util;
    private MaterialDialog materialDialog;
    private Activity mContext;
    CommonLocationApplication commonLocationApplication;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_cur_location,container,false);
        gps_util=new GPS_Util(getActivity().getBaseContext());
        currentLocationTextview=(TextView)view.findViewById(R.id.currentLocationTextView);
        currentLocationListView=(ListView)view.findViewById(R.id.currentLocationListView);

        commonLocationApplication=(CommonLocationApplication)getActivity().getApplication();
        //MaterialDialog  and button setting
        materialDialog=new MaterialDialog.Builder(getActivity()).customView(R.layout.dialog_current_location,true).build();
        View dialogView=materialDialog.getView();

        final Button dialog_current_location_reload_button=(Button)dialogView.findViewById(R.id.dialog_current_location_reload_button);
        final Button dialog_current_location_map_button=(Button)dialogView.findViewById(R.id.dialog_current_location_map_button);
        final TextView dialog_current_location_cancle_textview=(TextView) dialogView.findViewById(R.id.dialog_current_location_cancle_textview);

        Button.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.currentLocationTextView:
                        materialDialog.show();
                        break;
                    case R.id.dialog_current_location_reload_button:
                        gps_util=new GPS_Util(getActivity().getBaseContext());
                        commonLocationApplication.setLatLng(gps_util.getLatitude(),gps_util.getLongitude());

                       LocationReloadAsyncTask locationReloadAsyncTask=new LocationReloadAsyncTask();
                        locationReloadAsyncTask.execute(gps_util.getLatLng());
                        materialDialog.dismiss();
                        break;
                    case R.id.dialog_current_location_map_button:
                        Intent intent=new Intent(getActivity(),settingLocationMapActivity.class);
                        startActivity(intent);
                    case R.id.dialog_current_location_cancle_textview:
                        materialDialog.dismiss();
                        break;
                }
            }
        };
        currentLocationTextview.setOnClickListener(onClickListener);
        dialog_current_location_reload_button.setOnClickListener(onClickListener);
        dialog_current_location_map_button.setOnClickListener(onClickListener);
        dialog_current_location_cancle_textview.setOnClickListener(onClickListener);


        CurrentLocationListSerivce curlocationService=new CurrentLocationListSerivce();
        Call<CurrentLocationListItem> call=curlocationService.getCall(commonLocationApplication.getLat(),commonLocationApplication.getLng(),3000,"일식");



        call.enqueue(new Callback<CurrentLocationListItem>() {
            @Override
            public void onResponse(Call<CurrentLocationListItem> call, final Response<CurrentLocationListItem> response) {
                if(response.isSuccessful()){

                            CurrentLocationListAdapter adapter=new CurrentLocationListAdapter(mContext,R.layout.fragment_cur_location_listview_item,response.body().getRestaurants());
                            currentLocationListView.setAdapter(adapter);
                }else{
                    Logger.d("response 실패 ");
                }

            }

            @Override
            public void onFailure(Call<CurrentLocationListItem> call, Throwable t) {
                Logger.d(t);
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        currentLocationTextview.setText(commonLocationApplication.getLocationName());
        super.onResume();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (Activity) activity;
    }


    private class LocationReloadAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            Call<GeocodingService> call=GeocodingService.Geolcation.getCall(params[0]);

            return GeocodingService.Geolcation.getLocationName(call);
        }

        @Override
        protected void onPostExecute(String locationName) {
            currentLocationTextview.setText(locationName);
            super.onPostExecute(locationName);
        }
    }
}