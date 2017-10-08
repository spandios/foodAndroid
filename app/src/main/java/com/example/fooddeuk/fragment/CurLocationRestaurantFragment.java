package com.example.fooddeuk.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.activity.DetailRestaurantActivity;
import com.example.fooddeuk.activity.settingLocationMapActivity;
import com.example.fooddeuk.adapter.RestaurantAdapter;
import com.example.fooddeuk.R;
import com.example.fooddeuk.http.RestaurantService;
import com.example.fooddeuk.model.restaurant.LocationItem;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.rx.RxBus;
import com.example.fooddeuk.util.DialogUtil;
import com.example.fooddeuk.util.GPS_Util;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.RealmUtil;
import com.example.fooddeuk.staticval.StaticVal;
import com.orhanobut.logger.Logger;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CurLocationRestaurantFragment extends Fragment {
    GPS_Util gps_util;
    MaterialDialog locationSettingDialog;
    Activity mContext;
    LocationItem locationItems;
    String filter=StaticVal.defaultFilter;
    int maxDistance = StaticVal.defaultCurrentLocationMenuMaxDistance;
    String restaurantMenuType;


    @BindView(R.id.currentLocationTextView)
    TextView currentLocationTextView;
    @BindView(R.id.currentLocationListView)
    ListView currentLocationListView;
    @BindView(R.id.search_layout)
    RelativeLayout search_layout;
    @BindView(R.id.currentLocationTitle)
    RelativeLayout currentLocationTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;

    /**
     * 음식 종류에 따른 식당목록리스트 가져오기
     **/
    @OnClick({R.id.menu_type_japan, R.id.menu_type_chicken,R.id.menu_type_chinese})
    public void selectFoodType(TextView restaurant_menu_type) {
        restaurantMenuType=restaurant_menu_type.getText().toString().substring(1, restaurant_menu_type.getText().toString().length());
        getCurLocationRestaurant();
    }

    @OnClick(R.id.filterButton)
    public void setFilterRestaurant() {
        DialogUtil.setFilterRestaurant(getActivity(), new DialogUtil.FilterCallback() {
            @Override
            public void filterResult(int distance, String filterValue) {
                if(distance>0){
                    maxDistance=distance;

                }else{
                    filter=filterValue;

                }


                getCurLocationRestaurant();
            }
        });

    }
    @OnClick(R.id.searchButton)
    public void serachRestaurant(){
        search_layout.setVisibility(View.VISIBLE);
        currentLocationTitle.setVisibility(View.GONE);

    }

    @OnClick(R.id.cancel_search)
    public void cancelSearch(){
        search_layout.setVisibility(View.GONE);
        currentLocationTitle.setVisibility(View.VISIBLE);
        getCurLocationRestaurant();
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
                    getCurLocationRestaurant();
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
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    String rest_name=editSearch.getText().toString();
                    getCurLocationRestaurantSearch(rest_name);
                    return true;
                }
                return false;
            }
        });
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
            getCurLocationRestaurant();
        }
        //현재위치명
    }

    @Override
    public void onResume() {
        super.onResume();
        //지도로 수동 위치를 지정하면 그 위치를 현재위치로 설정한뒤 근처 식당 재검색
        RxBus.subscribe(o -> {
            if ((boolean) o) {
                locationItems = RealmUtil.findDataAll(LocationItem.class).get(0);
                currentLocationTextView.setText(locationItems.getLocationName());
                getCurLocationRestaurant();
            }
        });


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
                    getCurLocationRestaurant();
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
    void getCurLocationRestaurantSearch(String rest_name) {
        //기본 메뉴조건

        RestaurantService.getCurrentLocationRestaurant(locationItems.getLat(), locationItems.getLng(), maxDistance, restaurantMenuType,filter,rest_name).enqueue(new Callback<RestaurantItem>() {

            @Override
            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
                if (response.isSuccessful()) {

                    //주변에 데이터 없는경우 에러메세지 TOAST
                    if (response.body().getStatus().equals("FAIL")) {
                        Toast.makeText(mContext, response.body().getErrorMessage(), Toast.LENGTH_LONG).show();
                        currentLocationListView.setAdapter(null);
                    }else{
                        if(response.body().getRestaurants().size()>0){
                            RestaurantAdapter adapter = new RestaurantAdapter(mContext, R.layout.item_restaurant, response.body().getRestaurants());
                            currentLocationListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            /**Go to DetailRestaurant**/
                            /**Go to DetailRestaurant**/
                            /**Go to DetailRestaurant**/
                            currentLocationListView.setOnItemClickListener((parent, view, position, id) -> {
                                Parcelable restaurant = Parcels.wrap(parent.getAdapter().getItem(position));
                                Bundle extra = new Bundle();
                                extra.putParcelable("restaurant", restaurant);
                                IntentUtil.startActivity(getActivity(), DetailRestaurantActivity.class, extra);
                            });
                        }else{
                            Toast.makeText(mContext, "검색결과가 없습니다", Toast.LENGTH_LONG).show();
                            currentLocationListView.setAdapter(null);

                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<RestaurantItem> call, Throwable t) {
                Logger.d(t);
                Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG);
            }
        });
    }

    void getCurLocationRestaurant() {
        //기본 메뉴조건

        RestaurantService.getCurrentLocationRestaurant(locationItems.getLat(), locationItems.getLng(), maxDistance, restaurantMenuType,filter,"").enqueue(new Callback<RestaurantItem>() {

            @Override
            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
                if (response.isSuccessful()) {

                    //주변에 데이터 없는경우 에러메세지 TOAST
                    if (response.body().getStatus().equals("FAIL")) {
                        Toast.makeText(mContext, response.body().getErrorMessage(), Toast.LENGTH_LONG).show();
                        currentLocationListView.setAdapter(null);
                    }else{
                        if(response.body().getRestaurants().size()>0){
                            RestaurantAdapter adapter = new RestaurantAdapter(mContext, R.layout.item_restaurant, response.body().getRestaurants());
                            currentLocationListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            /**Go to DetailRestaurant**/
                            /**Go to DetailRestaurant**/
                            /**Go to DetailRestaurant**/
                            currentLocationListView.setOnItemClickListener((parent, view, position, id) -> {
                                Parcelable restaurant = Parcels.wrap(parent.getAdapter().getItem(position));
                                Bundle extra = new Bundle();
                                extra.putParcelable("restaurant", restaurant);
                                IntentUtil.startActivity(getActivity(), DetailRestaurantActivity.class, extra);
                            });
                        }else{
                            Toast.makeText(mContext, "검색결과가 없습니다", Toast.LENGTH_LONG).show();
                            currentLocationListView.setAdapter(null);

                        }
                    }

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




