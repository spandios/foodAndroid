package com.example.heojuyeong.foodandroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.heojuyeong.foodandroid.DetailRestaurantActivity;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.adapter.CurrentLocationListAdapter;
import com.example.heojuyeong.foodandroid.http.RestaurantService;
import com.example.heojuyeong.foodandroid.model.CurrentLocationRestaurantItem;
import com.example.heojuyeong.foodandroid.model.LocationItem;
import com.example.heojuyeong.foodandroid.rx.RxBus;
import com.example.heojuyeong.foodandroid.settingLocationMapActivity;
import com.example.heojuyeong.foodandroid.util.GPS_Util;
import com.example.heojuyeong.foodandroid.util.RealmUtil;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.heojuyeong.foodandroid.staticval.StaticVal.defaultCurrentLocationMenuMaxDistance;


public class CurLocationFragment extends Fragment {
    @BindView(R.id.currentLocationTextView)
    TextView currentLocationTextView;
    @BindView(R.id.currentLocationListView)
    ListView currentLocationListView;

    @OnClick({R.id.menu_type_japan, R.id.menu_type_chiken})
    public void selectFoodType(TextView textView){

            getCurLocationRestaurant(maxDistance, textView.getText().toString().substring(1,textView.getText().toString().length()));
    }

    GPS_Util gps_util;
    MaterialDialog materialDialog;
    Activity mContext;
    LocationItem locationItems;
    int maxDistance= defaultCurrentLocationMenuMaxDistance;
    String restaurantMenuType ;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //현재 위치 정보 db에서 가져옴
        locationItems=RealmUtil.findDataAll(LocationItem.class).get(0);
        String menuType=mContext.getResources().getString(R.string.restaurant_menu_type1);
        restaurantMenuType=menuType.substring(1,menuType.length());
        Logger.d(restaurantMenuType);
        super.onCreate(savedInstanceState);
    }

    @Nullable


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cur_location, container, false);
        ButterKnife.bind(this,view);
        setDialog();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCurLocationRestaurant(maxDistance, restaurantMenuType);
        //현재위치명
        currentLocationTextView.setText(locationItems.getLocationName());

    }




    @Override
    public void onResume() {
        //지도로 수동 위치를 지정하면 그 위치를 현재위치로 설정한뒤 근처 식당 재검색
        RxBus.subscribe(o -> {if((boolean)o){
            locationItems=RealmUtil.findDataAll(LocationItem.class).get(0);
            currentLocationTextView.setText(locationItems.getLocationName());
            getCurLocationRestaurant(maxDistance, restaurantMenuType);
        }
        });

        super.onResume();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext =activity;
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
                //현재위치에서 재 검색
                case R.id.dialog_current_location_reload_button:
                    gps_util = new GPS_Util(getActivity().getBaseContext());
                    gps_util.insertDB();
                    locationItems=RealmUtil.findDataAll(LocationItem.class).get(0);
                    currentLocationTextView.setText(locationItems.getLocationName());
                    getCurLocationRestaurant(maxDistance, restaurantMenuType);
                    materialDialog.dismiss();
                    break;
                //지도에서 직접 위치 지정
                case R.id.dialog_current_location_map_button:
                    materialDialog.dismiss();
                    Intent intent = new Intent(getActivity(), settingLocationMapActivity.class);
                    startActivity(intent);

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
    /** Param 위치 거리 음식종류 **/
    void getCurLocationRestaurant(int maxDistance,String menuType){
        //기본 메뉴조건
        Call<CurrentLocationRestaurantItem> call =
        RestaurantService.getCurrentLocationRestaurant(locationItems.getLat(), locationItems.getLng(), maxDistance, menuType);
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

                        /**Go to DetailRestaurant**/
                        currentLocationListView.setOnItemClickListener((parent, view, position, id) -> {

                            Parcelable restaurant = Parcels.wrap(parent.getAdapter().getItem(position));
                            Intent detailRestaurantIntent = new Intent(getActivity().getApplicationContext(), DetailRestaurantActivity.class);
                            Bundle extra = new Bundle();
                            extra.putParcelable("restaurant", restaurant);
                            detailRestaurantIntent.putExtras(extra);
                            startActivity(detailRestaurantIntent);
                        });
                    }


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
