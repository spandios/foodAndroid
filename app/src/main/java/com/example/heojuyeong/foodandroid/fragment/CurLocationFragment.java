package com.example.heojuyeong.foodandroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.activity.DetailRestaurantActivity;
import com.example.heojuyeong.foodandroid.activity.settingLocationMapActivity;
import com.example.heojuyeong.foodandroid.adapter.RestaurantAdapter;
import com.example.heojuyeong.foodandroid.http.RestaurantService;
import com.example.heojuyeong.foodandroid.model.restaurant.LocationItem;
import com.example.heojuyeong.foodandroid.model.restaurant.RestaurantItem;
import com.example.heojuyeong.foodandroid.rx.RxBus;
import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.example.heojuyeong.foodandroid.util.IntentUtil;
import com.example.heojuyeong.foodandroid.util.RealmUtil;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.heojuyeong.foodandroid.staticval.StaticVal.defaultCurrentLocationMenuMaxDistance;


public class CurLocationFragment extends Fragment {
    GPS_Util gps_util;
    MaterialDialog locationSettingDialog;
    Activity mContext;
    LocationItem locationItems;
    int maxDistance = defaultCurrentLocationMenuMaxDistance;
    String restaurantMenuType;
    MaterialDialog materialDialog;

    @BindView(R.id.currentLocationTextView)
    TextView currentLocationTextView;
    @BindView(R.id.currentLocationListView)
    ListView currentLocationListView;

    /**
     * 음식 종류에 따른 식당목록리스트 가져오기
     **/
    @OnClick({R.id.menu_type_japan, R.id.menu_type_chiken})
    public void selectFoodType(TextView restaurant_menu_type) {
        getCurLocationRestaurant(maxDistance, restaurant_menu_type.getText().toString().substring(1, restaurant_menu_type.getText().toString().length()));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //현재 위치 정보 db에서 가져옴
        super.onCreate(savedInstanceState);
        String menuType = mContext.getResources().getString(R.string.restaurant_menu_type1);
        restaurantMenuType = menuType.substring(1, menuType.length());

            RealmResults<LocationItem> realmResults = RealmUtil.findDataAll(LocationItem.class);
            if (realmResults.size() > 0) {
                locationItems = realmResults.get(0);
            } else {
                GPS_Util gps_util = new GPS_Util(getActivity());
                new Handler().postDelayed(() -> {
                    RealmResults<LocationItem> realmResults2 = RealmUtil.findDataAll(LocationItem.class);
                    if (realmResults2.size() > 0) {
                        locationItems = realmResults.get(0);
                        currentLocationTextView.setText(locationItems.getLocationName());
                        getCurLocationRestaurant(maxDistance, restaurantMenuType);
                    }
                }, 1000);
            }




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_current_location, container, false);
        ButterKnife.bind(this, view);
        setLocationSettingDialog();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        RealmResults<LocationItem> location=RealmUtil.findDataAll(LocationItem.class);
//
//        if(location.size()>0){
//            locationItems=RealmUtil.findDataAll(LocationItem.class).get(0);
//        }else{
//            Logger.d("LOCATION NULL");
//        }

        if (locationItems != null) {
            currentLocationTextView.setText(locationItems.getLocationName());
            getCurLocationRestaurant(maxDistance, restaurantMenuType);
        }
        //현재위치명
    }

    @Override
    public void onResume() {
        //지도로 수동 위치를 지정하면 그 위치를 현재위치로 설정한뒤 근처 식당 재검색
        RxBus.subscribe(o -> {
            if ((boolean) o) {
                locationItems = RealmUtil.findDataAll(LocationItem.class).get(0);
                currentLocationTextView.setText(locationItems.getLocationName());
                getCurLocationRestaurant(maxDistance, restaurantMenuType);
            }
        });

        super.onResume();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    //위치 셋팅 다이아로그
    public void setLocationSettingDialog() {
        locationSettingDialog = new MaterialDialog.Builder(getActivity()).customView(R.layout.dialog_current_location, true).build();
        View dialogView = locationSettingDialog.getView();
        final Button location_reload_button = (Button) dialogView.findViewById(R.id.dialog_current_location_reload_button);
        final Button location_map_button = (Button) dialogView.findViewById(R.id.dialog_current_location_map_button);
        final TextView location_cancel = (TextView) dialogView.findViewById(R.id.dialog_current_location_cancel_textview);
        Button.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.currentLocationTextView:
                    locationSettingDialog.show();
                    break;

                //현재위치에서 재 검색
                case R.id.dialog_current_location_reload_button:
                    gps_util = new GPS_Util(getActivity().getApplicationContext());
                    gps_util.insertDB();
                    locationItems = RealmUtil.findDataAll(LocationItem.class).get(0);
                    currentLocationTextView.setText(locationItems.getLocationName());
                    getCurLocationRestaurant(maxDistance, restaurantMenuType);
                    locationSettingDialog.dismiss();
                    break;
                //지도에서 직접 위치 지정
                case R.id.dialog_current_location_map_button:
                    locationSettingDialog.dismiss();
                    IntentUtil.startActivity(getActivity(), settingLocationMapActivity.class);
                    //닫기
                case R.id.dialog_current_location_cancel_textview:
                    locationSettingDialog.dismiss();
                    break;
            }
        };
        currentLocationTextView.setOnClickListener(onClickListener);
        location_reload_button.setOnClickListener(onClickListener);
        location_map_button.setOnClickListener(onClickListener);
        location_cancel.setOnClickListener(onClickListener);
    }


    /**
     * 식당 리스트 뷰 Param 위치(locationItem) 최대거리(maxDistance) 식당메뉴타입(menuType)
     **/
    void getCurLocationRestaurant(int maxDistance, String menuType) {
        //기본 메뉴조건
        Call<RestaurantItem> call =
                RestaurantService.getCurrentLocationRestaurant(locationItems.getLat(), locationItems.getLng(), maxDistance, menuType);
        call.enqueue(new Callback<RestaurantItem>() {
            @Override
            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
                if (response.isSuccessful()) {

                    //주변에 데이터 없는경우 에러메세지 TOAST
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(mContext, response.body().getErrorMessage(), Toast.LENGTH_LONG);
                    }

                    if (response.body().getRestaurants() != null) {

                        RestaurantAdapter adapter = new RestaurantAdapter(mContext, R.layout.item_restaurant, response.body().getRestaurants());

                        currentLocationListView.setAdapter(adapter);

                        /**Go to DetailRestaurant**/
                        /**Go to DetailRestaurant**/
                        /**Go to DetailRestaurant**/
                        currentLocationListView.setOnItemClickListener((parent, view, position, id) -> {
                            Parcelable restaurant = Parcels.wrap(parent.getAdapter().getItem(position));
                            Bundle extra = new Bundle();
                            extra.putParcelable("restaurant", restaurant);
                            IntentUtil.startActivity(getActivity(), DetailRestaurantActivity.class, extra);
                        });
                    }

                } else {
                    Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<RestaurantItem> call, Throwable t) {
                Logger.d(t);
                Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG);
            }
        });
    }
}




