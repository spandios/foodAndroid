package com.example.fooddeuk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.CartActivity;
import com.example.fooddeuk.activity.rest_list_fragment;
import com.example.fooddeuk.activity.settingLocationMapActivity;
import com.example.fooddeuk.adapter.RestaurantAdapter;
import com.example.fooddeuk.model.restaurant.LocationItem;
import com.example.fooddeuk.rx.RxBus;
import com.example.fooddeuk.staticval.StaticVal;
import com.example.fooddeuk.util.DialogUtil;
import com.example.fooddeuk.util.GPS_Util;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.RealmUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;


public class CurLocationRestaurantFragment extends Fragment {
    GPS_Util gps_util;
    MaterialDialog locationSettingDialog;
    Activity mContext;
    LocationItem locationItems;
    String filter = StaticVal.defaultFilter;
    int maxDistance = StaticVal.defaultCurrentLocationMenuMaxDistance;
    String restaurantMenuType;
    RestaurantAdapter restaurantAdapter;
    boolean scrollEnough = false;
    private ViewPager rest_list_view_pager;

    interface testCallback {
        void testCallback(Fragment fragment);
    }

    @BindView(R.id.currentLocationTextView)
    TextView currentLocationTextView;
//    @BindView(R.id.restaurantRecyclerViewInList)
//    RecyclerView restaurantRecyclerViewInList;
    @BindView(R.id.search_layout)
    RelativeLayout search_layout;
    @BindView(R.id.currentLocationTitle)
    RelativeLayout currentLocationTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;
//    @BindView(R.id.restaurantMenuCategoryLayout)
//    RelativeLayout restaurantMenuCategoryLayout;
//    @BindView(R.id.restaurant_menu_type1)
//    TextView restaurant_menu_type1;
//    @BindView(R.id.menu_type_tab_selected)
//    ImageView menu_type_tab_selected;
    @BindView(R.id.restaurantCartButtonInList)
    ImageView restaurantCartButtonInList;
    @BindView(R.id.rest_list_cart_qty)
    TextView rest_list_cart_qty;
    @BindView(R.id.rest_tab_layout)
    TabLayout restTabLayout;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;


    /**
     * 음식 종류에 따른 식당목록리스트 가져오기
     **/

//    @OnClick({R.id.restaurant_menu_type1, R.id.restaurant_menu_type2, R.id.restaurant_menu_type3, R.id.restaurant_menu_type4, R.id.restaurant_menu_type5, R.id.restaurant_menu_type6})
//    public void selectFoodType(TextView restaurant_menu_type) {
//        restaurantMenuType = restaurant_menu_type.getText().toString();
//        for (int i = 0; i < restaurantMenuCategoryLayout.getChildCount(); i++) {
//            if (restaurantMenuCategoryLayout.getChildAt(i) instanceof TextView) {
//                restaurantMenuCategoryLayout.getChildAt(i).setSelected(false);
//                ((TextView) restaurantMenuCategoryLayout.getChildAt(i)).setTypeface(restaurant_menu_type.getTypeface(), Typeface.NORMAL);
//            }
//        }
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//        );
//        params.addRule(RelativeLayout.BELOW, R.id.restaurant_menu_type1);
//        int dp = LayoutUtil.DpToPx(getActivity(), Float.parseFloat(restaurant_menu_type.getTag().toString()));
//        params.setMargins(dp, LayoutUtil.DpToPx(getActivity(), 4), 0, 0);
//        menu_type_tab_selected.setLayoutParams(params);
//        restaurant_menu_type.setSelected(true);
//        restaurant_menu_type.setTypeface(restaurant_menu_type.getTypeface(), Typeface.BOLD);
//        getCurLocationRestaurant();
//    }

    //Onclick Filter
    @OnClick(R.id.filterButton)
    public void setFilterRestaurant() {
        DialogUtil.setRestaurantFilter(getActivity(), (distance, filterValue) -> {
            if (distance > 0) {
                maxDistance = distance;
            } else {
                filter = filterValue;
            }
//            getCurLocationRestaurant();
        });

    }

    //OnClick Cart in restaurantList
    @OnClick(R.id.restaurantCartButtonInList)
    public void goToCart(View imageview) {
        imageview.setOnClickListener(v -> {
            IntentUtil.startActivity(getActivity(), CartActivity.class);
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

        RealmResults<LocationItem> realmResults = RealmUtil.findDataAll(LocationItem.class);
        if (realmResults.size() > 0) {
            locationItems = realmResults.get(0);
            Logger.d(locationItems.getLat());
        } else {
            GPS_Util gps_util = new GPS_Util(getActivity());
            new Handler().postDelayed(() -> {
                RealmResults<LocationItem> realmResults2 = RealmUtil.findDataAll(LocationItem.class);
                if (realmResults2.size() > 0) {
                    locationItems = realmResults.get(0);
                    currentLocationTextView.setText(locationItems.getLocationName());
//                    getCurLocationRestaurant();
                }
            }, 1000);
        }


    }

    //todo floatingbutton
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, view);
        setLocationSettingDialog();
//        restaurant_menu_type1.setSelected(true);
        rest_list_view_pager = (ViewPager) view.findViewById(R.id.rest_list_view_pager);

//        restaurantRecyclerViewInList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE & scrollEnough) {
//                    fab.show();
//                } else {
//                    fab.hide();
//                }
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 10) {
//                    scrollEnough = true;
//                } else {
//                    scrollEnough = false;
//                }
//
//            }
//        });
//        rest_list_cart_qty.bringToFront();
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String rest_name = editSearch.getText().toString();
//                getCurLocationRestaurantSearch(rest_name);
                return true;
            }
            return false;
        });
        RelativeLayout r = (RelativeLayout) view.findViewById(R.id.currentLocationTitle);
        r.bringChildToFront(r.findViewById(R.id.rest_list_cart_qty));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (locationItems != null) {
            currentLocationTextView.setText(locationItems.getLocationName());
            rest_list_view_pager.setAdapter(new fragmentPagerAdapter(getChildFragmentManager()));
            restTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            restTabLayout.setupWithViewPager(rest_list_view_pager);
            restTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(),R.color.text_sub),ContextCompat.getColor(getActivity(),R.color.tangerine));
            restTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(),R.color.tangerine));
//            getCurLocationRestaurant();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //지도로 수동 위치를 지정하면 그 위치를 현재위치로 설정한뒤 근처 식당 재검색
        RxBus.subscribe(o -> {
            if ((boolean) o) {
                locationItems = RealmUtil.findDataAll(LocationItem.class).get(0);
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
//                    getCurLocationRestaurant();
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


//    void getCurLocationRestaurantSearch(String rest_name) {
//        //기본 메뉴조건
//
//        RestaurantService.getCurrentLocationRestaurant(locationItems.getLat(), locationItems.getLng(), maxDistance, restaurantMenuType, filter, rest_name).enqueue(new Callback<RestaurantItem>() {
//
//            @Override
//            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
//                setRestaurantAdapter(response);
//            }
//
//            @Override
//            public void onFailure(Call<RestaurantItem> call, Throwable t) {
//                Logger.d(t);
//                Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    void updateRestaurant(testCallback testCallback) {
//        RealmResults<LocationItem> realmResults = RealmUtil.findDataAll(LocationItem.class);
//
//        RestaurantService.getCurrentLocationRestaurant(realmResults.get(0).getLat(), realmResults.get(0).getLng(), maxDistance, restaurantMenuType, filter, "").enqueue(new Callback<RestaurantItem>() {
//            @Override
//            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
//                testFragment= rest_list_fragment.newInstance(response.body().getRestaurants());
//                testCallback.testCallback(rest_list_fragment.newInstance(response.body().getRestaurants()));
//
//            }
//
//            @Override
//            public void onFailure(Call<RestaurantItem> call, Throwable t) {
//                Logger.d(t);
//                Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }


//    void getCurLocationRestaurant() {
//        //기본 메뉴조건
//        RestaurantService.getCurrentLocationRestaurant(locationItems.getLat(), locationItems.getLng(), maxDistance, restaurantMenuType, filter, "").enqueue(new Callback<RestaurantItem>() {
//            @Override
//            public void onResponse(Call<RestaurantItem> call, final Response<RestaurantItem> response) {
////                setRestaurantAdapter(response);
//            }
//
//            @Override
//            public void onFailure(Call<RestaurantItem> call, Throwable t) {
//                Logger.d(t);
//                Toast.makeText(mContext, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    public void setRestaurantAdapter(final Response<RestaurantItem> response) {
//        if (response.isSuccessful()) {
//            //주변에 데이터 없는경우 에러메세지 TOAST
//            if (response.body().getStatus().equals("FAIL")) {
//                Toast.makeText(getActivity(), response.body().getErrorMessage(), Toast.LENGTH_LONG).show();
//                restaurantRecyclerViewInList.setAdapter(null);
//            } else {
//                if (response.body().getRestaurants().size() > 0) {
//                    restaurantAdapter = new RestaurantAdapter(getActivity(), response.body().getRestaurants());
//                    /**Go to DetailRestaurant**/
//                    /**Go to DetailRestaurant**/
//                    /**Go to DetailRestaurant**/
//                    restaurantAdapter.setRestaurantItemClickListener(restaurant -> {
//                        Parcelable restaurantParcel = Parcels.wrap(restaurant);
//                        Bundle extra = new Bundle();
//                        extra.putParcelable("restaurant", restaurantParcel);
//                        IntentUtil.startActivity(getActivity(), DetailRestaurantActivity.class, extra);
//                    });
//
//                    LayoutUtil.RecyclerViewSetting(getActivity(), restaurantRecyclerViewInList);
//                    restaurantRecyclerViewInList.setAdapter(restaurantAdapter);
//
//                } else {
//                    Toast.makeText(mContext, "검색결과가 없습니다", Toast.LENGTH_LONG).show();
//                    restaurantRecyclerViewInList.setAdapter(null);
//
//                }
//            }
//        }
//    }

    public class fragmentPagerAdapter extends FragmentPagerAdapter {

        public fragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0:
                    Logger.d("ff");
                    return rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "일식", filter, "");

                case 1:
                    return rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "치킨", filter, "");

                default:
                    return rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, "일식", filter, "");
            }
//            if(position==0){
//                final Fragment[] frgment = new Fragment[1];
//                ArrayList<RestaurantItem.Restaurant> list = new ArrayList<>();
//                RestaurantItem.Restaurant rest = new RestaurantItem.Restaurant(3,"a","a","a","a","a","a","a",3,3,3,"a","a","a","a",null);
//                list.add(rest);
//                return rest_list_fragment.newInstance(locationItems.getLat(), locationItems.getLng(), maxDistance, restaurantMenuType, filter, "");
//            }else{
//                ArrayList<RestaurantItem.Restaurant> list = new ArrayList<>();
//                RestaurantItem.Restaurant rest = new RestaurantItem.Restaurant(3,"b","b","b","b","b","b","b",3,3,3,"b","b","b","b",null);
//                list.add(rest);
//                return rest_list_fragment.newInstance(list);
//            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "일식";
                case 1:
                    return "치킨";
                default:return null;
            }
        }
    }


}
