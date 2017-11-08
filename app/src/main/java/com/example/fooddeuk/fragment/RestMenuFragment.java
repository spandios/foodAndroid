package com.example.fooddeuk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.http.MenuReviewService;
import com.example.fooddeuk.http.MenuService;
import com.example.fooddeuk.model.menu.MenuItem;
import com.example.fooddeuk.model.menu.ReviewItem;
import com.example.fooddeuk.util.LayoutUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heo on 2017. 11. 5..
 */

public class RestMenuFragment extends Fragment  implements MenuListAdapter.OnItemClickListener,MenuListAdapter.OnCartCountClickListener{
    int menu_category_id;
    @BindView(R.id.rest_detail_menu_list)
    RecyclerView rest_detail_menu_list;

    public RestMenuFragment(){

    }



    //Implement CartAddClick -> 메뉴 추가 한 뒤 장바구니 개수 새로고침
    @Override
    public void onCartCount(int cartSize) {

//        rest_detail_cart_count.setText(String.valueOf(cartSize));
    }


    //Implement ItemClick -> 해당 position의 상세 레이아웃 펼쳐짐
    @Override
    public void onItemClick(View view, int position, MenuItem menuItem) {



//        메뉴 클릭 시 우선 모든 레이아웃 상세 레이아웃 접은 뒤
        for (int childCount = rest_detail_menu_list.getChildCount(), i = 0; i < childCount; i++) {
            RecyclerView.ViewHolder allViewHolder = rest_detail_menu_list.getChildViewHolder(rest_detail_menu_list.getChildAt(i));
            allViewHolder.itemView.findViewById(R.id.menu_master_layout).setVisibility(View.VISIBLE);
            allViewHolder.itemView.findViewById(R.id.menu_detail_layout).setVisibility(View.GONE);
        }





        //클릭한 해당 포지션 상세 레이아웃 펼쳐짐
//        ((LinearLayoutManager) rest_detail_menu_list.getLayoutManager()).scrollToPositionWithOffset(position, 5);
        view.findViewById(R.id.menu_master_layout).setVisibility(View.GONE);
        view.findViewById(R.id.menu_detail_layout).setVisibility(View.VISIBLE);

        View.OnClickListener reviewClick= v -> MenuReviewService.getReview(menuItem.getMenu_id()).enqueue(new Callback<ArrayList<ReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewItem>> call, Response<ArrayList<ReviewItem>> response) {
//                reviewDialogShow(DetailRestaurantActivity.this,response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<ReviewItem>> call, Throwable t) {

            }
        });

        //해당 메뉴의 리뷰를 가져옴
        TextView reviewText=(TextView)view.findViewById(R.id.menu_detail_review);
        if(menuItem.getReview_count()>0){
            reviewText.setText("리뷰[" + menuItem.getReview_count() + "]");
        }else{
            reviewText.setText("리뷰[0]");
        }

        view.findViewById(R.id.menu_detail_rating).setOnClickListener(reviewClick);
        reviewText.setOnClickListener(reviewClick);

    }



    public static RestMenuFragment newInstance(int menu_category_id){
        Bundle args=new Bundle();
        args.putInt("menu_category_id",menu_category_id);
        RestMenuFragment restMenuFragment=new RestMenuFragment();
        restMenuFragment.setArguments(args);
        return restMenuFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            menu_category_id=getArguments().getInt("menu_category_id");

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest_menu, container, false);
        ButterKnife.bind(this, view);
        getMenuList();

        return view;
    }



    public void getMenuList(){
        MenuService.getMenu(menu_category_id).enqueue(new Callback<ArrayList<MenuItem>>() {

            @Override
            public void onResponse(Call<ArrayList<MenuItem>> call, final Response<ArrayList<MenuItem>> response) {
                if(response.isSuccessful()){
                    if(response.body().size()>0){
                        final MenuListAdapter adapter = new MenuListAdapter(getActivity(), response.body());
                        adapter.setOnCartCountClickListener(RestMenuFragment.this);
                        adapter.setOnItemClickListener(RestMenuFragment.this);
                        LayoutUtil.RecyclerViewSetting(getActivity(),rest_detail_menu_list);
                        rest_detail_menu_list.setFocusable(false);
                        rest_detail_menu_list.setFocusableInTouchMode(false);
                        rest_detail_menu_list.setNestedScrollingEnabled(false);
                        rest_detail_menu_list.setAdapter(adapter);
                    }
                }

//                adapter.setOnCartCountClickListener(RestMenuFragment.this);
//                adapter.setOnItemClickListener(RestMenuFragment.this);
//                rest_detail_menu_list.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<MenuItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
