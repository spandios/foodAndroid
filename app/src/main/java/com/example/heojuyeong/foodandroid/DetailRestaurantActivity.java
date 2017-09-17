package com.example.heojuyeong.foodandroid;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.http.MenuCategoryService;
import com.example.heojuyeong.foodandroid.http.MenuService;
import com.example.heojuyeong.foodandroid.item.CurrentLocationListItem;
import com.example.heojuyeong.foodandroid.item.MenuCategoryItem;
import com.example.heojuyeong.foodandroid.item.MenuItem;
import com.example.heojuyeong.foodandroid.listview.MenuListHotAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestaurantActivity extends AppCompatActivity {
    private TextView rest_name;
    private Button backButton;
    private Button mapButton;
    private Button cartButton;
    private ImageView restImage;
    private TextView detailRating;
    private TextView discount;
    private RecyclerView recyclerView;
    CurrentLocationListItem.Restaurant restaurant;
    private ScrollView detailRestaurantScrollView;
    private MaterialDialog materialDialog;
    private HorizontalScrollView detailMenuCategoryParentScrollView;
    private HorizontalScrollView detailMenuCategoryParentScrollView2;
    private RelativeLayout detailHotMenuHeader;
    private boolean test=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
//        restaurant=(CurrentLocationListItem.Restaurant) getIntent().getExtras().get("restaurant");
        restaurant= Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        detailRestaurantScrollView=(ScrollView)findViewById(R.id.detailRestaurantScrollView);

        detailHotMenuHeader=(RelativeLayout)findViewById(R.id.detailHotMenuHeader);
        detailMenuCategoryParentScrollView=(HorizontalScrollView)findViewById(R.id.detailMenuCategoryParentScrollView);
        detailMenuCategoryParentScrollView2=(HorizontalScrollView)findViewById(R.id.detailMenuCategoryParentScrollView2);
        recyclerView=(RecyclerView)findViewById(R.id.detailMenuListView);
        //scroll
        detailRestaurantScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                if(detailRestaurantScrollView.getScrollY()>detailMenuCategoryParentScrollView.getY()){
                    if(!test) {
                        detailMenuCategoryParentScrollView.setVisibility(View.GONE);
//                    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.addRule(RelativeLayout.BELOW,R.id.detailHotMenuHeader);
//                    detailMenuCategoryParentScrollView2.setLayoutParams(layoutParams);
                        detailMenuCategoryParentScrollView2.setVisibility(View.VISIBLE);
                        test = true;
//                    layoutParams.addRule(RelativeLayout.BELOW,R.id.detailMenuCategoryParentScrollView2);
                    }
                }else{
                    if(test){
                        detailMenuCategoryParentScrollView2.setVisibility(View.GONE);
                        detailMenuCategoryParentScrollView.setVisibility(View.VISIBLE);
                        test=false;
                    }

                }
            }
        });


        rest_name = (TextView) findViewById(R.id.rest_name);
        backButton = (Button) findViewById(R.id.detailBackButton);
        mapButton = (Button) findViewById(R.id.detailMapButton);
        cartButton = (Button) findViewById(R.id.detailCartButton);
        restImage = (ImageView) findViewById(R.id.detailRestImage);
        detailRating=(TextView)findViewById(R.id.detailRating);
        discount=(TextView)findViewById(R.id.detailDiscount);



        final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(nmLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),nmLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        rest_name.setText(restaurant.getName());
        Picasso.with(this).load(restaurant.getRest_picture()).resize(150, 150).into(restImage);
        detailRating.setText(Double.toString(restaurant.getRating()));
        discount.setText(restaurant.getDiscount());
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.detailBackButton:
                        finish();
                        break;

                    case R.id.detailMapButton:
                        break;

                    case R.id.detailCartButton:
                        break;
                }
            }
        };

        backButton.setOnClickListener(onClickListener);
        //menuCategory Function
        menuCategoryShow(restaurant.getRest_id());

    }




    void menuCategoryShow(int rest_id){
        final LinearLayout layout=(LinearLayout)findViewById(R.id.detailMenuCategoryParent);
        final LinearLayout layout2=(LinearLayout)findViewById(R.id.detailMenuCategoryParent2);
        MenuCategoryService menuCategoryService=new MenuCategoryService();
        Call<MenuCategoryItem> call=menuCategoryService.getCall(rest_id);
        call.enqueue(new Callback<MenuCategoryItem>() {
            @Override
            public void onResponse(Call<MenuCategoryItem> call, Response<MenuCategoryItem> response) {
                if(response.body().getStatus().equals("SUCCESS")){
                    ArrayList<MenuCategoryItem.MenuCategory> items=response.body().getResults();

                    for(int i=0; i< items.size();i++){
                        layout.addView(makeMenuCategory(items.get(i).getCateNAme(),items.get(i).getMenu_cate_gory_id()));

                        layout2.addView(makeMenuCategory(items.get(i).getCateNAme(),items.get(i).getMenu_cate_gory_id()));

                    }
                    //default popular menu show
                    menuShow(items.get(0).getMenu_cate_gory_id());
                }else{
                    TextView textView=new TextView(getApplicationContext());
                    textView.setText("메뉴 불러오기 실패했습니다.");

                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.setMargins(50,0,0,0);
                    textView.setLayoutParams(params);
                    layout.addView(textView);

                }
            }

            @Override
            public void onFailure(Call<MenuCategoryItem> call, Throwable t) {
                Logger.d(t);
            }
        });

    }



    void menuShow(int menu_category_id){
        final MenuService menuService=new MenuService();
        Call<ArrayList<MenuItem>> call=menuService.getCall(menu_category_id);
        call.enqueue(new Callback<ArrayList<MenuItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItem>> call, final Response<ArrayList<MenuItem>> response) {

                final MenuListHotAdapter adapter=new MenuListHotAdapter(DetailRestaurantActivity.this, response.body(),restaurant, new MenuListHotAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        for(int childCount=recyclerView.getChildCount(), i=0; i<childCount; i++){
                            RecyclerView.ViewHolder viewHolder=recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                            viewHolder.itemView.findViewById(R.id.masterHotMenu).setVisibility(View.VISIBLE);
                            viewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.GONE);
                        }

//                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position,5);
                        RecyclerView.ViewHolder viewHolder= recyclerView.findViewHolderForAdapterPosition(position);
                        viewHolder.itemView.findViewById(R.id.masterHotMenu).setVisibility(View.GONE);
                        viewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.VISIBLE);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<MenuItem>> call, Throwable t) {
                Logger.d(t);
            }
        });
    }


    TextView makeMenuCategory(String name, int id){
        TextView textView=new TextView(getApplicationContext());
        textView.setText(name);
        textView.setTextSize(20);
        textView.setId(id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuShow(view.getId());
            }
        });
        textView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(60,0,0,0);
        textView.setLayoutParams(params);
        return textView;
    }
}
