package com.example.heojuyeong.foodandroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.DetailRestaurantActivity;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.common.CommonLocationApplication;
import com.example.heojuyeong.foodandroid.http.CurrentLocationListSerivce;
import com.example.heojuyeong.foodandroid.http.GeocodingService;
import com.example.heojuyeong.foodandroid.listview.CurrentLocationListAdapter;
import com.example.heojuyeong.foodandroid.item.CurrentLocationListItem;
import com.example.heojuyeong.foodandroid.settingLocationMapActivity;
import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

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
    final CurrentLocationListSerivce curlocationService = new CurrentLocationListSerivce();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cur_location, container, false);
        commonLocationApplication = (CommonLocationApplication) getActivity().getApplication();
        gps_util = new GPS_Util(getActivity().getBaseContext());
        currentLocationTextview = (TextView) view.findViewById(R.id.currentLocationTextView);
        currentLocationListView = (ListView) view.findViewById(R.id.currentLocationListView);
        currentLocationTextview.setText(commonLocationApplication.getLocationName());
        //MaterialDialog  and button setting
        materialDialog = new MaterialDialog.Builder(getActivity()).customView(R.layout.dialog_current_location, true).build();
        View dialogView = materialDialog.getView();

        final Button dialog_current_location_reload_button = (Button) dialogView.findViewById(R.id.dialog_current_location_reload_button);
        final Button dialog_current_location_map_button = (Button) dialogView.findViewById(R.id.dialog_current_location_map_button);
        final TextView dialog_current_location_cancle_textview = (TextView) dialogView.findViewById(R.id.dialog_current_location_cancle_textview);

        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.currentLocationTextView:
                        materialDialog.show();
                        break;
                    case R.id.dialog_current_location_reload_button:
                        gps_util = new GPS_Util(getActivity().getBaseContext());

                        Logger.d(gps_util.getLatitude());
                        Logger.d(gps_util.getLongitude());
                        commonLocationApplication.setLatLng(gps_util.getLatitude(), gps_util.getLongitude());
                        LocationReloadAsyncTask locationReloadAsyncTask = new LocationReloadAsyncTask();
                        locationReloadAsyncTask.execute(gps_util.getLatLng());
                        getCurLocationRestaurant();
                        materialDialog.dismiss();
                        break;
                    case R.id.dialog_current_location_map_button:
                        materialDialog.dismiss();
                        Intent intent = new Intent(getActivity(), settingLocationMapActivity.class);
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


        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //여기서 kotlin
    }

    @Override
    public void onResume() {
        currentLocationTextview.setText(commonLocationApplication.getLocationName());
        getCurLocationRestaurant();
        super.onResume();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (Activity) activity;
    }


    private class LocationReloadAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Call<GeocodingService> call = GeocodingService.Geolcation.getCall(params[0]);
            return GeocodingService.Geolcation.getLocationName(call);
        }

        @Override
        protected void onPostExecute(String locationName) {
            currentLocationTextview.setText(locationName);
            commonLocationApplication.setLocationName(locationName);
            super.onPostExecute(locationName);
        }
    }



    void getCurLocationRestaurant(){
        Call<CurrentLocationListItem> call = curlocationService.getCall(commonLocationApplication.getLat(), commonLocationApplication.getLng(), 10000000, "일식");
        call.enqueue(new Callback<CurrentLocationListItem>() {
            @Override
            public void onResponse(Call<CurrentLocationListItem> call, final Response<CurrentLocationListItem> response) {
                if (response.isSuccessful()) {
                    //주변에 데이터 없는경우 에러메세지 TOAST
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(mContext, response.body().getErrorMessage(), Toast.LENGTH_LONG);
                    }
                    CurrentLocationListAdapter adapter = new CurrentLocationListAdapter(mContext, R.layout.fragment_cur_location_listview_item, response.body().getRestaurants());
                    currentLocationListView.setAdapter(adapter);

                    //클릭했을 경우 상세보기 activity 실행
                    currentLocationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Parcelable restaurant = Parcels.wrap(parent.getAdapter().getItem(position));
                            Intent detailRestaurantIntent = new Intent(getActivity().getApplicationContext(), DetailRestaurantActivity.class);
                            Bundle extra = new Bundle();
                            extra.putParcelable("restaurant", restaurant);
                            detailRestaurantIntent.putExtras(extra);
                            
                            startActivity(detailRestaurantIntent);
                        }
                    });

                } else {
                    Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<CurrentLocationListItem> call, Throwable t) {
                Logger.d(t);
                Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG);
            }
        });
    }
}
