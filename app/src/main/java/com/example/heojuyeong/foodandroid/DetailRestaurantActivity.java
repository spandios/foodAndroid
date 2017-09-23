package com.example.heojuyeong.foodandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heojuyeong.foodandroid.adapter.MenuListHotAdapter;
import com.example.heojuyeong.foodandroid.http.MenuCategoryService;
import com.example.heojuyeong.foodandroid.http.MenuService;
import com.example.heojuyeong.foodandroid.model.CurrentLocationRestaurantItem;
import com.example.heojuyeong.foodandroid.model.MenuCategoryItem;
import com.example.heojuyeong.foodandroid.model.MenuItem;
import com.example.heojuyeong.foodandroid.util.IntentUtil;
import com.example.heojuyeong.foodandroid.util.LayoutUtil;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestaurantActivity extends AppCompatActivity implements MenuListHotAdapter.OnItemClickListener, MenuListHotAdapter.OnCartAddClickListener, View.OnClickListener {
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
    @BindView(R.id.detailRestaurantScrollView)
    NestedScrollView detailRestaurantScrollView;
    @BindView(R.id.cartCount)
    TextView cartCount;
    @BindView(R.id.detailMenuCategoryParentLayoutInContentScrollView)
    HorizontalScrollView detailMenuCategoryParentLayoutInContentScrollView;
    @BindView(R.id.detailMenuCategoryParentLayoutInHeaderScrollView)
    HorizontalScrollView detailMenuCategoryParentLayoutInHeaderScrollView;


    CurrentLocationRestaurantItem.Restaurant restaurant;
    boolean headerScroll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurant=Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        setContentView(R.layout.activity_detail_restaurant);

        ButterKnife.bind(this);
        viewSetting();
        setScrollEvent();
        menuCategoryShow(restaurant.getRest_id());
        menuShow(40);



    }

    //Implement CartAddClick -> 장바구니 개수 가져옴
    @Override
    public void onCartAddClick(int cartSize) {
        cartCount.setText(String.valueOf(cartSize));
    }

    //Implement ItemClick -> 메뉴 클릭 시 상세 레이아웃 펼쳐짐
    @Override
    public void onItemClick(View view, int position) {
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; i++) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            viewHolder.itemView.findViewById(R.id.masterHotMenu).setVisibility(View.VISIBLE);
            viewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.GONE);
        }
//                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position,5);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        viewHolder.itemView.findViewById(R.id.masterHotMenu).setVisibility(View.GONE);
        viewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.VISIBLE);
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
                Logger.d("rr");
                IntentUtil.startActivity(this, CartActivity.class);
                break;
            case R.id.cartButton:
                Logger.d("rr");
                IntentUtil.startActivity(this, CartActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        headerScroll = false;
    }


    void viewSetting(){
        rest_name.setText(restaurant.getName());
        Picasso.with(this).load(restaurant.getRest_picture()).resize(150, 150).into(restImage);
        detailRating.setText(Double.toString(restaurant.getRating()));
        discount.setText(restaurant.getDiscount());
        cartButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        detailTopCartButton.setOnClickListener(this);
    }




    void setScrollEvent() {
        final View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> detailRestaurantScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {

                    if (detailRestaurantScrollView.getScrollY() > detailMenuCategoryParentLayoutInContentScrollView.getY()) {
                        if (!headerScroll) {
                            detailMenuCategoryParentLayoutInContentScrollView.setVisibility(View.GONE);
                            detailMenuCategoryParentLayoutInHeaderScrollView.setVisibility(View.VISIBLE);
                            headerScroll = true;
                        }
                    } else {
                        if (headerScroll) {
                            detailMenuCategoryParentLayoutInHeaderScrollView.setVisibility(View.GONE);
                            detailMenuCategoryParentLayoutInContentScrollView.setVisibility(View.VISIBLE);
                            headerScroll = false;
                        }
                    }
                }));
    }

    void menuCategoryShow(int rest_id) {
        LinearLayout detailMenuCategoryParentLayoutInContent=(LinearLayout)findViewById(R.id.detailMenuCategoryParentLayoutInContent);
        LinearLayout detailMenuCategoryParentLayoutInHeader=(LinearLayout)findViewById(R.id.detailMenuCategoryParentLayoutInHeader);
        Call<MenuCategoryItem> call = MenuCategoryService.getMenuCategory(rest_id);
        call.enqueue(new Callback<MenuCategoryItem>() {
            @Override
            public void onResponse(Call<MenuCategoryItem> call, Response<MenuCategoryItem> response) {
                if (response.body().getStatus().equals("SUCCESS")) {
                    ArrayList<MenuCategoryItem.MenuCategory> items = response.body().getResults();

                    for (int i = 0; i < items.size(); i++) {
                        detailMenuCategoryParentLayoutInContent.addView(makeMenuCategory(items.get(i).getCateNAme(), items.get(i).getMenu_cate_gory_id()));
                        detailMenuCategoryParentLayoutInHeader.addView(makeMenuCategory(items.get(i).getCateNAme(), items.get(i).getMenu_cate_gory_id()));
                    }
                    //default popular menu show

                }
            }
            @Override
            public void onFailure(Call<MenuCategoryItem> call, Throwable t) {
                Logger.d(t);
            }
        });
    }


    void menuShow(int menu_category_id) {
        Call<ArrayList<MenuItem>> call = MenuService.getCall(menu_category_id);
        call.enqueue(new Callback<ArrayList<MenuItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItem>> call, final Response<ArrayList<MenuItem>> response) {
                final MenuListHotAdapter adapter = new MenuListHotAdapter(DetailRestaurantActivity.this, response.body(), restaurant);
                adapter.setOnCartAddClickListener(DetailRestaurantActivity.this);
                adapter.setmOnItemClickListener(DetailRestaurantActivity.this);
                recyclerView.setFocusable(false);
                recyclerView.setFocusableInTouchMode(false);
                LayoutUtil.RecyclerViewSetting(DetailRestaurantActivity.this.getApplicationContext(),recyclerView);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<MenuItem>> call, Throwable t) {
                Logger.d(t);
            }
        });
    }


    //메뉴카테고리 텍스트뷰 생성
    TextView makeMenuCategory(String name, int id) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(name);
        textView.setTextSize(20);
        textView.setId(id);
        //menuShow
        textView.setOnClickListener(view -> menuShow(view.getId()));
        textView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(60, 0, 0, 0);
        textView.setLayoutParams(params);
        return textView;
    }

}
