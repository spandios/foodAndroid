package com.example.fooddeuk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.DetailRestaurantActivity;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.http.MenuService;
import com.example.fooddeuk.model.menu.MenuItem;
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

//RecyclerView Implement in Fragment
public class RestMenuFragment extends Fragment  {
    @BindView(R.id.rest_detail_menu_list)
    RecyclerView rest_detail_menu_list;
    private Context context;
    private int menu_category_id;
    private MenuListAdapter menuListAdapter;


    public RestMenuFragment(){

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DetailRestaurantActivity){
            this.context=context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void getMenuList(){
        MenuService.getMenu(menu_category_id).enqueue(new Callback<ArrayList<MenuItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItem>> call, final Response<ArrayList<MenuItem>> response) {
                if(response.isSuccessful()){
                    if(response.body().size()>0){
                        menuListAdapter = new MenuListAdapter(getActivity(), response.body());
                        menuListAdapter.setOnItemClickListener((DetailRestaurantActivity)context);
                        LayoutUtil.RecyclerViewSetting(getActivity(),rest_detail_menu_list);
                        rest_detail_menu_list.setFocusable(true);
                        rest_detail_menu_list.setFocusableInTouchMode(true);
                        rest_detail_menu_list.setNestedScrollingEnabled(false);
                        rest_detail_menu_list.setAdapter(menuListAdapter);
                    }else{
                        Toast.makeText(context,"No Menu Item",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<MenuItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
