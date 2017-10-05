package com.example.heojuyeong.foodandroid.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.adapter.MenuListAdapter;
import com.example.heojuyeong.foodandroid.adapter.ReviewAdapter;
import com.example.heojuyeong.foodandroid.http.MenuCategoryService;
import com.example.heojuyeong.foodandroid.http.MenuReviewService;
import com.example.heojuyeong.foodandroid.http.MenuService;
import com.example.heojuyeong.foodandroid.model.cart.CartItem;
import com.example.heojuyeong.foodandroid.model.menu.MenuCategoryItem;
import com.example.heojuyeong.foodandroid.model.menu.MenuItem;
import com.example.heojuyeong.foodandroid.model.menu.ReviewItem;
import com.example.heojuyeong.foodandroid.model.restaurant.RestaurantItem;
import com.example.heojuyeong.foodandroid.util.IntentUtil;
import com.example.heojuyeong.foodandroid.util.LayoutUtil;
import com.example.heojuyeong.foodandroid.util.RealmUtil;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestaurantActivity extends AppCompatActivity implements MenuListAdapter.OnItemClickListener, MenuListAdapter.OnCartCountClickListener, View.OnClickListener {
    @BindView(R.id.rest_name)
    TextView rest_name;
    @BindView(R.id.detailBackButton)
    Button backButton;
    @BindView(R.id.detailMapButton)
    Button mapButton;
    @BindView(R.id.detailTopCartButton)
    Button detailTopCartButton;
    @BindView(R.id.cartButton)
    Button cartButton;
    @BindView(R.id.detailRestImage)
    ImageView restImage;
    @BindView(R.id.detailRating)
    TextView detailRating;
    @BindView(R.id.detailDiscount)
    TextView discount;
    @BindView(R.id.detailMenuListView)
    RecyclerView recyclerView;
    @BindView(R.id.cartCount)
    TextView cartCount;
    @BindView(R.id.detailMenuCategoryParentLayoutInContentScrollView)
    HorizontalScrollView detailMenuCategoryParentLayoutInContentScrollView;
    @BindView(R.id.detailMenuCategoryLayout)
    LinearLayout detailMenuCategoryLayout;


    RestaurantItem.Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Parcel로 선택된 식당 정보 가져옴
        /**from CurLocationFragment**/
        /**from CurLocationFragment**/
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        setContentView(R.layout.activity_detail_restaurant);
        ButterKnife.bind(this);
        viewSetting();
        menuCategoryShow(restaurant.getRest_id());


    }


    //Implement CartAddClick -> 메뉴 추가 한 뒤 장바구니 개수 새로고침
    @Override
    public void onCartCount(int cartSize) {
        cartCount.setText(String.valueOf(cartSize));
    }


    //Implement ItemClick -> 해당 position의 상세 레이아웃 펼쳐짐
    @Override
    public void onItemClick(View view, int position, MenuItem menuItem) {



        //메뉴 클릭 시 우선 모든 레이아웃 상세 레이아웃 접은 뒤
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; i++) {
            RecyclerView.ViewHolder allViewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            allViewHolder.itemView.findViewById(R.id.masterHotMenu).setVisibility(View.VISIBLE);
            allViewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.GONE);
        }





        //클릭한 해당 포지션 상세 레이아웃 펼쳐짐
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 5);
        view.findViewById(R.id.masterHotMenu).setVisibility(View.GONE);
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
            case R.id.detailBackButton:
                finish();
                break;

            case R.id.detailMapButton:
                break;

            case R.id.detailTopCartButton:
                IntentUtil.startActivity(this, CartActivity.class);
                break;
            case R.id.cartButton:
                IntentUtil.startActivity(this, CartActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCount.setText(String.valueOf(RealmUtil.getDataSize(CartItem.class)));

    }


    void viewSetting() {
        recyclerView.setFocusable(false);
        recyclerView.setFocusableInTouchMode(false);
        LayoutUtil.RecyclerViewSetting(DetailRestaurantActivity.this.getApplicationContext(), recyclerView);
        rest_name.setText(restaurant.getName());
        Picasso.with(this).load(restaurant.getRest_picture()).resize(150, 150).into(restImage);
        detailRating.setText(Double.toString(restaurant.getRating()));
        discount.setText(restaurant.getDiscount());
        cartButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        detailTopCartButton.setOnClickListener(this);
    }

    /** 메뉴 카테고리 사이즈 만큼 텍스트 생성  추가 , 가장 처음 메뉴카테고리의 메뉴를 기본으로 보여줌*/
    void menuCategoryShow(int rest_id) {

        MenuCategoryService.getMenuCategory(rest_id).enqueue(new Callback<MenuCategoryItem>() {
            @Override
            public void onResponse(Call<MenuCategoryItem> call, Response<MenuCategoryItem> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("SUCCESS")) {
                        ArrayList<MenuCategoryItem.MenuCategory> items = response.body().getResults();
                        //default first  menu show
                        menuShow(items.get(0).getMenu_cate_gory_id());
                        for (int i = 0; i < items.size(); i++) {
                            detailMenuCategoryLayout.addView(makeMenuCategory(items.get(i).getCateName(), items.get(i).getMenu_cate_gory_id()));
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
    void menuShow(int menu_category_id) {
        MenuService.getMenu(menu_category_id).enqueue(new Callback<ArrayList<MenuItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItem>> call, final Response<ArrayList<MenuItem>> response) {
                final MenuListAdapter adapter = new MenuListAdapter(DetailRestaurantActivity.this, response.body(), restaurant);
                adapter.setOnCartCountClickListener(DetailRestaurantActivity.this);
                adapter.setOnItemClickListener(DetailRestaurantActivity.this);
                recyclerView.setAdapter(adapter);

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
    TextView makeMenuCategory(String name, int id) {
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


}
