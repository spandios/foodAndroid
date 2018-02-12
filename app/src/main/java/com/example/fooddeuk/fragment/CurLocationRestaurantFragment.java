package com.example.fooddeuk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.MapActivity;
import com.example.fooddeuk.activity.settingLocationMapActivity;
import com.example.fooddeuk.model.restaurant.LocationItem;
import com.example.fooddeuk.rx.RxBus;
import com.example.fooddeuk.staticval.StaticVal;
import com.example.fooddeuk.util.DialogUtil;
import com.example.fooddeuk.util.GPS_Util;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.RealmUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;


public class CurLocationRestaurantFragment extends Fragment{
    GPS_Util gps_util;
    MaterialDialog locationSettingDialog;
    Activity mContext;
    LocationItem locationItems;
    String filter = StaticVal.INSTANCE.getDefaultFilter();
    int maxDistance = StaticVal.INSTANCE.getDefaultCurrentLocationMenuMaxDistance();
    String restaurantMenuType;
    fragmentPagerAdapter fragmentPagerAdapter;



    @BindView(R.id.currentLocationTextView)
    TextView currentLocationTextView;
    @BindView(R.id.search_layout)
    RelativeLayout search_layout;
    @BindView(R.id.currentLocationTitle)
    RelativeLayout currentLocationTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;

    @BindView(R.id.rest_list_view_pager)
    ViewPager rest_list_view_pager;
    @BindView(R.id.rest_list_view_pager_tab_layout)
    SmartTabLayout restListViewPagerTab;
    RealmResults<LocationItem> realmResults;


    /**
     * 음식 종류에 따른 식당목록리스트 가져오기
     **/

    @OnClick(R.id.restaurant_map_button)
    public void restaurant_map_button(){
        Bundle bundle=new Bundle();
        bundle.putDouble("lat",locationItems.getLat());
        bundle.putDouble("lng",locationItems.getLng());
        bundle.putString("filter",filter);
        bundle.putInt("maxDistance",maxDistance);
        bundle.putString("menuType",restaurantMenuType);
        IntentUtil.startActivity(getActivity(), MapActivity.class,bundle);
    }
    //Onclick Filter
    @OnClick(R.id.filterButton)
    public void setFilterRestaurant() {
        DialogUtil.setRestaurantFilter(getActivity(), (distance, filterValue) -> {

            if (distance > 0) {
                maxDistance = distance;
            } else {
                filter = filterValue;
            }
            updateViewPager();
        });

    }



    //Onclick Search
    @OnClick(R.id.searchButton)
    public void searchRestaurant() {
        search_layout.setVisibility(View.VISIBLE);
        currentLocationTitle.setVisibility(View.GONE);

    }

    @OnClick(R.id.cancel_search)
    public void cancelSearch() {
        search_layout.setVisibility(View.GONE);
        currentLocationTitle.setVisibility(View.VISIBLE);
//        getCurLocationRestaurant();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //현재 위치 정보 db에서 가져옴
        super.onCreate(savedInstanceState);

        restaurantMenuType = mContext.getResources().getString(R.string.restaurant_menu_type1);
        realmResults=RealmUtil.findDataAll(LocationItem.class);




    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, view);
        setLocationSettingDialog();
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String rest_name = editSearch.getText().toString();
//                getCurLocationRestaurantSearch(rest_name);
                return true;
            }
            return false;
        });

        if (realmResults.size() > 0) {
            locationItems = realmResults.get(0);
            fragmentPagerAdapter = new fragmentPagerAdapter(getChildFragmentManager());
            rest_list_view_pager.setAdapter(fragmentPagerAdapter);
            restListViewPagerTab.setViewPager(rest_list_view_pager);

        }else{
            Logger.d("FAIL GPS");

        }
//        } else {
//            Logger.d("fail GPS");
//            GPS_Util gps_util = new GPS_Util(getActivity());
//            new Handler().postDelayed(() -> {
//                RealmResults<LocationItem> realmResults2 = RealmUtil.findDataAll(LocationItem.class);
//                if (realmResults2.size() > 0) {
//                    locationItems = realmResults.get(0);
//                    currentLocationTextView.setText(locationItems.getLocationName());
//
//                }
//            }, 1500);
//        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (locationItems != null) {
            currentLocationTextView.setText(locationItems.getLocationName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //지도로 수동 위치를 지정하면 그 위치를 현재위치로 설정한뒤 근처 식당 재검색
        RxBus.subscribe(o -> {
            if ((boolean) o) {
                locationItems = RealmUtil.findDataAll(LocationItem.class).get(0);
                Logger.d(locationItems);
                currentLocationTextView.setText(locationItems.getLocationName());
//                getCurLocationRestaurant();
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
        final Button location_reload_button = dialogView.findViewById(R.id.btn_get_location);
        final Button location_map_button = dialogView.findViewById(R.id.btn_map_get_location_);
        final TextView location_cancel = dialogView.findViewById(R.id.btn_location_cancel);
        Button.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.currentLocationTextView:
                    locationSettingDialog.show();
                    break;

                //현재위치에서 재 검색
                case R.id.btn_get_location:
                    gps_util = new GPS_Util(getActivity().getApplicationContext());
                    gps_util.insertDB();
                    locationItems = RealmUtil.findDataAll(LocationItem.class).get(0);
                    currentLocationTextView.setText(locationItems.getLocationName());
//                    getCurLocationRestaurant();
                    locationSettingDialog.dismiss();
                    break;
                //지도에서 직접 위치 지정
                case R.id.btn_map_get_location_:
                    locationSettingDialog.dismiss();
                    IntentUtil.startActivity(getActivity(), settingLocationMapActivity.class);
                    //닫기
                case R.id.btn_location_cancel:
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


    public class fragmentPagerAdapter extends FragmentStatePagerAdapter{

        public fragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 8;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "일식", filter, "");

                case 1:
                    return Rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "치킨", filter, "");

                case 2:
                    return Rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "", filter, "");

                case 3:
                    return Rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "중식", filter, "");

                default:
                    return Rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "일식", filter, "");
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "일식";
                case 1:
                    return "치킨";
                case 2:
                    return "전체";
                case 3:
                    return "중식";
                case 4:
                    return "HTTP";
                case 5:
                    return "HTTP";
                case 6:
                    return "HTTP";
                case 7:
                    return "HTTP";

                default:
                    return null;
            }
        }
    }

    public void updateViewPager() {
            rest_list_view_pager.getAdapter().notifyDataSetChanged();
    }



}
