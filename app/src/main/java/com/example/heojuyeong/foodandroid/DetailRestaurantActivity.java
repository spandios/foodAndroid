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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private int price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        CurrentLocationListItem.Restaurant restaurant = (CurrentLocationListItem.Restaurant) getIntent().getExtras().get("serialData");
        rest_name = (TextView) findViewById(R.id.rest_name);
        backButton = (Button) findViewById(R.id.detailBackButton);
        mapButton = (Button) findViewById(R.id.detailMapButton);
        cartButton = (Button) findViewById(R.id.detailCartButton);
        restImage = (ImageView) findViewById(R.id.detailRestImage);
        detailRating=(TextView)findViewById(R.id.detailRating);
        discount=(TextView)findViewById(R.id.detailDiscount);
        recyclerView=(RecyclerView)findViewById(R.id.detailMenuListView);
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
        MenuCategoryService menuCategoryService=new MenuCategoryService();
        Call<MenuCategoryItem> call=menuCategoryService.getCall(rest_id);
        call.enqueue(new Callback<MenuCategoryItem>() {
            @Override
            public void onResponse(Call<MenuCategoryItem> call, Response<MenuCategoryItem> response) {
                if(response.body().getStatus().equals("SUCCESS")){
                    ArrayList<MenuCategoryItem.MenuCategory> items=response.body().getResults();

                    for(int i=0; i< items.size();i++){
                        TextView textView=new TextView(getApplicationContext());
                        textView.setText(items.get(i).getCateNAme());
                        textView.setTextSize(20);
                        textView.setId(items.get(i).getMenu_cate_gory_id());
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
                        layout.addView(textView);
                    }

                    //default popular menu show
                    menuShow(items.get(0).getMenu_cate_gory_id());


                }else{
                    TextView textView=new TextView(getApplicationContext());
                    textView.setText("메뉴 불러오기 실패했습니다.");
                    layout.addView(textView);LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

                final MenuListHotAdapter adapter=new MenuListHotAdapter(getApplicationContext(), response.body(), new MenuListHotAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        for(int childCount=recyclerView.getChildCount(), i=0; i<childCount; i++){
                            RecyclerView.ViewHolder viewHolder=recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                            viewHolder.itemView.findViewById(R.id.masterHotMenu).setVisibility(View.VISIBLE);
                            viewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.GONE);
                        }

                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position,10);
//                                recyclerView.scrollTo(0,view.getHeight());
                        RecyclerView.ViewHolder viewHolder= recyclerView.findViewHolderForAdapterPosition(position);
                        viewHolder.itemView.findViewById(R.id.masterHotMenu).setVisibility(View.GONE);
                        viewHolder.itemView.findViewById(R.id.detailHotMenu).setVisibility(View.VISIBLE);

                    }
                },new MenuListHotAdapter.OnSizeClickListener(){
                    @Override
                    public void onSizeClick(final View view,final int position) {
                        final MaterialDialog materialDialog=new MaterialDialog.Builder(DetailRestaurantActivity.this).customView(R.layout.dialog_menu_list_size, true).build();
                        View dialogView=materialDialog.getView();

                        final LinearLayout dialog_menu_list_size_layout=(LinearLayout)dialogView.findViewById(R.id.dialog_menu_size_layout);

                        if(response.body().get(position).getSize().size()>0){
                            for(final MenuItem.Size size:response.body().get(position).getSize()){
                                Button button=new Button(getApplicationContext());
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        RecyclerView.ViewHolder viewHolder= recyclerView.findViewHolderForAdapterPosition(position);
                                        TextView textView=(TextView)viewHolder.itemView.findViewById(R.id.detailHotMenuSizeText);
                                        TextView textView1=(TextView)viewHolder.itemView.findViewById(R.id.detailHotMenuPrice);
                                        textView.setText(size.getSize_name());
                                        textView1.setText(size.getSize_price());
                                        materialDialog.dismiss();
                                    }
                                });
                                button.setText(size.getSize_name()+"                   "+size.getSize_price()+"원");
                                dialog_menu_list_size_layout.addView(button);

                            }
                            materialDialog.show();
                        }
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
}
