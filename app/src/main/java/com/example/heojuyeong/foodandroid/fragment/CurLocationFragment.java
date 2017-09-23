package com.example.heojuyeong.foodandroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
import com.example.heojuyeong.foodandroid.adapter.CurrentLocationListAdapter;
import com.example.heojuyeong.foodandroid.common.CommonLocationApplication;
import com.example.heojuyeong.foodandroid.http.CurrentLocationListService;
import com.example.heojuyeong.foodandroid.http.GeocodingService;
import com.example.heojuyeong.foodandroid.model.CurrentLocationRestaurantItem;
import com.example.heojuyeong.foodandroid.model.LocationItem;
import com.example.heojuyeong.foodandroid.rx.RxBus;
import com.example.heojuyeong.foodandroid.settingLocationMapActivity;
import com.example.heojuyeong.foodandroid.staticval.StaticVal;
import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.example.heojuyeong.foodandroid.util.RealmUtil;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurLocationFragment extends Fragment {
    @BindView(R.id.currentLocationTextView)
    TextView currentLocationTextView;
    @BindView(R.id.currentLocationListView)
    ListView currentLocationListView;

    GPS_Util gps_util;
    MaterialDialog materialDialog;
    Activity mContext;
    LocationItem locationItems;
    CommonLocationApplication commonLocationApplication;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        locationItems=RealmUtil.findDataAll(LocationItem.class).get(0);
        super.onCreate(savedInstanceState);
    }

    @Nullable


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cur_location, container, false);
        ButterKnife.bind(this,view);
        commonLocationApplication = (CommonLocationApplication) getActivity().getApplication();
        setDialog();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCurLocationRestaurant();
        if(locationItems.getLocationName()!=null){
            currentLocationTextView.setText(locationItems.getLocationName());
        }else{
            currentLocationTextView.setText("오류");
        }


        //ui변경가능

    }




    @Override
    public void onResume() {
        RxBus.subscribe(o -> {if((int)o==1){
            locationItems=RealmUtil.findDataAll(LocationItem.class).get(0);
            currentLocationTextView.setText(locationItems.getLocationName());
            getCurLocationRestaurant();
        }
        });

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
            currentLocationTextView.setText(locationName);
            commonLocationApplication.setLocationName(locationName);
            super.onPostExecute(locationName);
        }
    }


    public void setDialog(){
        materialDialog = new MaterialDialog.Builder(getActivity()).customView(R.layout.dialog_current_location, true).build();
        View dialogView = materialDialog.getView();
        final Button dialog_current_location_reload_button = (Button) dialogView.findViewById(R.id.dialog_current_location_reload_button);
        final Button dialog_current_location_map_button = (Button) dialogView.findViewById(R.id.dialog_current_location_map_button);
        final TextView dialog_current_location_cancel_textview = (TextView) dialogView.findViewById(R.id.dialog_current_location_cancel_textview);
        Button.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.currentLocationTextView:
                    materialDialog.show();
                    break;
                case R.id.dialog_current_location_reload_button:
                    gps_util = new GPS_Util(getActivity().getBaseContext());
                    gps_util.insertDB();
                    locationItems=RealmUtil.findDataAll(LocationItem.class).get(0);
                    Logger.d(locationItems.getLocationName());
                    currentLocationTextView.setText(locationItems.getLocationName());
//                    commonLocationApplication.setLatLng(gps_util.getLatitude(), gps_util.getLongitude());
//                    LocationReloadAsyncTask locationReloadAsyncTask = new LocationReloadAsyncTask();
//                    locationReloadAsyncTask.execute(gps_util.getLatLng());
                    getCurLocationRestaurant();
                    materialDialog.dismiss();
                    break;
                case R.id.dialog_current_location_map_button:
                    materialDialog.dismiss();
                    Intent intent = new Intent(getActivity(), settingLocationMapActivity.class);
                    startActivityForResult(intent, StaticVal.LocationSelectByMapRequest);
                case R.id.dialog_current_location_cancel_textview:
                    materialDialog.dismiss();
                    break;
            }
        };
        currentLocationTextView.setOnClickListener(onClickListener);
        dialog_current_location_reload_button.setOnClickListener(onClickListener);
        dialog_current_location_map_button.setOnClickListener(onClickListener);
        dialog_current_location_cancel_textview.setOnClickListener(onClickListener);
    }

    void getCurLocationRestaurant(){
        Call<CurrentLocationRestaurantItem> call = CurrentLocationListService.getCall(locationItems.getLat(), locationItems.getLng(), 10000000, "일식");
        call.enqueue(new Callback<CurrentLocationRestaurantItem>() {
            @Override
            public void onResponse(Call<CurrentLocationRestaurantItem> call, final Response<CurrentLocationRestaurantItem> response) {
                if (response.isSuccessful()) {
                    //주변에 데이터 없는경우 에러메세지 TOAST
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(mContext, response.body().getErrorMessage(), Toast.LENGTH_LONG);
                    }
                    if(response.body().getRestaurants()!=null){
                        CurrentLocationListAdapter adapter = new CurrentLocationListAdapter(mContext, R.layout.fragment_cur_location_listview_item, response.body().getRestaurants());
                        currentLocationListView.setAdapter(adapter);
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
                    }


                    //클릭했을 경우 상세보기 activity 실행


                } else {
                    Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<CurrentLocationRestaurantItem> call, Throwable t) {
                Logger.d(t);
                Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG);
            }
        });
    }
}
