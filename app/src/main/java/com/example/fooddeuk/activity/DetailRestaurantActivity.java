package com.example.fooddeuk.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.adapter.ReviewAdapter;
import com.example.fooddeuk.http.MenuCategoryService;
import com.example.fooddeuk.http.MenuReviewService;
import com.example.fooddeuk.http.MenuService;
import com.example.fooddeuk.model.cart.CartItem;
import com.example.fooddeuk.model.menu.MenuCategoryItem;
import com.example.fooddeuk.model.menu.MenuItem;
import com.example.fooddeuk.model.menu.ReviewItem;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.LayoutUtil;
import com.example.fooddeuk.util.RealmUtil;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestaurantActivity extends AppCompatActivity implements MenuListAdapter.OnItemClickListener, MenuListAdapter.OnCartCountClickListener, View.OnClickListener {


    @BindView(R.id.rest_detail_back_btn)
    Button rest_detail_back_btn;

    @BindView(R.id.rest_detail_name)
    TextView rest_detail_name;

    @BindView(R.id.rest_detail_map)
    Button rest_detail_map;

    @BindView(R.id.rest_detail_image)
    ImageView rest_detail_image;

    @BindView(R.id.rest_detail_rating)
    TextView rest_detail_rating;

    @BindView(R.id.rest_detail_rating_star_layout)
    LinearLayout rest_detail_rating_star_layout;

    @BindView(R.id.rest_detail_menu_list)
    RecyclerView rest_detail_menu_list;

    @BindView(R.id.rest_detail_cart_count)
    TextView rest_detail_cart_count;

    @BindView(R.id.rest_detail_menu_category_layout)
    LinearLayout rest_detail_menu_category_layout;




    @BindView(R.id.rest_detail_cart_btn)
    Button rest_detail_cart_btn;




    RestaurantItem.Restaurant restaurant;
    HashMap<String, Integer> starDpMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        ButterKnife.bind(this);
        //Parcel로 선택된 식당 정보 가져옴
        /**from CurLocationRestaurantFragment**/
        /**from CurLocationRestaurantFragment**/
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        starDpMap=LayoutUtil.DpToLayoutParams(getApplicationContext(), 12,11);
        viewSetting();
        setStarView(restaurant.rating,rest_detail_rating_star_layout);

        menuCategoryShow(restaurant.getRest_id());


    }


    //Implement CartAddClick -> 메뉴 추가 한 뒤 장바구니 개수 새로고침
    @Override
    public void onCartCount(int cartSize) {
        rest_detail_cart_count.setText(String.valueOf(cartSize));
    }


    //Implement ItemClick -> 해당 position의 상세 레이아웃 펼쳐짐
    @Override
    public void onItemClick(View view, int position, MenuItem menuItem) {



        //메뉴 클릭 시 우선 모든 레이아웃 상세 레이아웃 접은 뒤
        for (int childCount = rest_detail_menu_list.getChildCount(), i = 0; i < childCount; i++) {
            RecyclerView.ViewHolder allViewHolder = rest_detail_menu_list.getChildViewHolder(rest_detail_menu_list.getChildAt(i));
            allViewHolder.itemView.findViewById(R.id.menu_master_layout).setVisibility(View.VISIBLE);
            allViewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.GONE);
        }





        //클릭한 해당 포지션 상세 레이아웃 펼쳐짐
        ((LinearLayoutManager) rest_detail_menu_list.getLayoutManager()).scrollToPositionWithOffset(position, 5);
        view.findViewById(R.id.menu_master_layout).setVisibility(View.GONE);
        view.findViewById(R.id.detailHotMenu).setVisibility(View.VISIBLE);

        View.OnClickListener reviewClick= v -> MenuReviewService.getReview(menuItem.getMenu_id()).enqueue(new Callback<ArrayList<ReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewItem>> call, Response<ArrayList<ReviewItem>> response) {
                reviewDialogShow(DetailRestaurantActivity.this,response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ReviewItem>> call, Throwable t) {

            }
        });

        //해당 메뉴의 리뷰를 가져옴
        TextView reviewText=(TextView)view.findViewById(R.id.detailHotMenuReviewTextView);
        if(menuItem.getReview_count()>0){
            reviewText.setText("리뷰[" + menuItem.getReview_count() + "]");
        }else{
            reviewText.setText("리뷰[0]");
        }

        view.findViewById(R.id.detailHotMenuRating).setOnClickListener(reviewClick);
        reviewText.setOnClickListener(reviewClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rest_detail_back_btn:
                finish();
                break;

            case R.id.rest_detail_map:
                break;

//            case R.id.detailTopCartButton:
//                IntentUtil.startActivity(this, CartActivity.class);
//                break;
            case R.id.rest_detail_cart_btn:
                IntentUtil.startActivity(this, CartActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        rest_detail_cart_count.setText(String.valueOf(RealmUtil.getDataSize(CartItem.class)));

    }


    private void viewSetting() {
        //MenuList
        rest_detail_menu_list.setFocusable(false);
        rest_detail_menu_list.setFocusableInTouchMode(false);
        LayoutUtil.RecyclerViewSetting(getApplicationContext(), rest_detail_menu_list);
        rest_detail_name.setText(restaurant.getName());
        Picasso.with(this).load(restaurant.getRest_picture()).resize(150, 150).into(rest_detail_image);
        rest_detail_rating.setText(Double.toString(restaurant.getRating()));
//        discount.setText(restaurant.getDiscount());
        rest_detail_cart_btn.setOnClickListener(this);
        rest_detail_back_btn.setOnClickListener(this);
//        detailTopCartButton.setOnClickListener(this);
    }

    /** 메뉴 카테고리 사이즈 만큼 텍스트 생성  추가 , 가장 처음 메뉴카테고리의 메뉴를 기본으로 보여줌*/
    private void menuCategoryShow(int rest_id) {

        MenuCategoryService.getMenuCategory(rest_id).enqueue(new Callback<MenuCategoryItem>() {
            @Override
            public void onResponse(Call<MenuCategoryItem> call, Response<MenuCategoryItem> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("SUCCESS")) {
                        ArrayList<MenuCategoryItem.MenuCategory> items = response.body().getResults();
                        //default first  menu show
                        if(items.size()>0){
                            menuShow(items.get(0).getMenu_cate_gory_id());
                            for (int i = 0; i < items.size(); i++) {
                                rest_detail_menu_category_layout.addView(makeMenuCategory(items.get(i).getCateName(), items.get(i).getMenu_cate_gory_id()));
                            }
                        }else{
                            Logger.d("not result menuCategory");
                        }

                    } else {
                        Logger.d("not result menuCategory");
                    }
                }
            }

            @Override
            public void onFailure(Call<MenuCategoryItem> call, Throwable t) {
                Logger.d(t);
            }
        });
    }

    /** 메뉴 리스트  param:메뉴 카테고리 아이디   */
    private void menuShow(int menu_category_id) {
        MenuService.getMenu(menu_category_id).enqueue(new Callback<ArrayList<MenuItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItem>> call, final Response<ArrayList<MenuItem>> response) {
                final MenuListAdapter adapter = new MenuListAdapter(DetailRestaurantActivity.this, response.body(), restaurant);
                adapter.setOnCartCountClickListener(DetailRestaurantActivity.this);
                adapter.setOnItemClickListener(DetailRestaurantActivity.this);
                rest_detail_menu_list.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<MenuItem>> call, Throwable t) {
                Logger.d(t);
            }
        });
    }

    //리뷰 다이아로그
    private void reviewDialogShow(Activity activity, ArrayList<ReviewItem> reviewItem) {
        MaterialDialog reviewDialog = new MaterialDialog.Builder(activity).customView(R.layout.dialog_review_listview, true).build();
        View reviewDialogView = reviewDialog.getView();
        RecyclerView reviewListView = (RecyclerView) reviewDialogView.findViewById(R.id.reviewListView);
        ReviewAdapter reviewAdapter = new ReviewAdapter(activity.getApplicationContext(), reviewItem);
        LayoutUtil.RecyclerViewSetting(activity.getApplicationContext(), reviewListView);
        reviewListView.setAdapter(reviewAdapter);
        reviewDialog.show();
    }

    //메뉴카테고리 텍스트뷰 생성
    private TextView makeMenuCategory(String name, int id) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(name);
        textView.setTextSize(20);
        //menuShow
        textView.setOnClickListener(view -> menuShow(view.getId()));
        textView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(60, 0, 0, 0);
        textView.setLayoutParams(params);
        textView.setOnClickListener(v -> menuShow(id));
        return textView;
    }

    private void setRest_detail_menu_list(ArrayList<MenuItem> menuItems){

    }

    private void setStarView(float rating, LinearLayout rest_detail_rating_star_layout) {
        for (int j = 0; j < 5; j++) {
            ImageView star = new ImageView(getApplicationContext());
            star.setLayoutParams(new ViewGroup.LayoutParams(starDpMap.get("width"), starDpMap.get("height")));
            if (rating == 0) {
                star.setImageResource(R.drawable.ic_star);
                rest_detail_rating_star_layout.addView(star);

            } else if (rating >= 1.0) {
                rating -= 1.0;
                star.setImageResource(R.drawable.ic_star_ranked);
                rest_detail_rating_star_layout.addView(star);

            } else if (rating == 0.5) {
                rating -= 0.5;
                star.setImageResource(R.drawable.ic_star_ranked_half);
                rest_detail_rating_star_layout.addView(star);

            }
        }
    }


}
