package com.example.fooddeuk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.DetailRestaurantActivity;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.model.menu.MenuContentItem;
import com.example.fooddeuk.util.LayoutUtil;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heo on 2017. 11. 5..
 */

//RecyclerView Implement in Fragment
public class RestMenuFragment extends Fragment  {
    @BindView(R.id.rest_detail_menu_list)
    RecyclerView rest_detail_menu_list;
    private Context context;
    ArrayList<MenuContentItem> menu;
    int rest_id;
    private MenuListAdapter menuListAdapter;


    public RestMenuFragment(){

    }



    public static RestMenuFragment newInstance(ArrayList<MenuContentItem> menu,int rest_id){

        Bundle args=new Bundle();
        args.putInt("rest_id",rest_id);
        args.putParcelable("menuContent", Parcels.wrap(menu));
        RestMenuFragment restMenuFragment=new RestMenuFragment();
        restMenuFragment.setArguments(args);
        return restMenuFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){

//            menu_category_id=getArguments().getInt("menu_category_id");
            menu=Parcels.unwrap(getArguments().getParcelable("menuContent"));
            rest_id=getArguments().getInt("rest_id");

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
        MenuListAdapter menuListAdapter=new MenuListAdapter(getActivity(),menu,rest_id);
//        menuListAdapter = new MenuListAdapter(getActivity(), menu,rest_id);
        menuListAdapter.setOnItemClickListener((DetailRestaurantActivity)context);
        LayoutUtil.RecyclerViewSetting(getActivity(),rest_detail_menu_list);
        rest_detail_menu_list.setFocusable(true);
        rest_detail_menu_list.setFocusableInTouchMode(true);
        rest_detail_menu_list.setNestedScrollingEnabled(true);
        rest_detail_menu_list.setAdapter(menuListAdapter);
        menuListAdapter.notifyDataSetChanged();

//        Logger.d(menu_category_id);
//        MenuService.getMenu(menu_category_id).enqueue(new Callback<ArrayList<MenuContentItem>>() {
//            @Override
//            public void onResponse(Call<ArrayList<MenuContentItem>> call, final Response<ArrayList<MenuContentItem>> response) {
//                if(response.isSuccessful()){
//                    if(response.body().size()>0){
//                        menuListAdapter = new MenuListAdapter(getActivity(), response.body());
//                        menuListAdapter.setOnItemClickListener((DetailRestaurantActivity)context);
//                        LayoutUtil.RecyclerViewSetting(getActivity(),rest_detail_menu_list);
//                        rest_detail_menu_list.setFocusable(true);
//                        rest_detail_menu_list.setFocusableInTouchMode(true);
//                        rest_detail_menu_list.setNestedScrollingEnabled(true);
//
//                        rest_detail_menu_list.setAdapter(menuListAdapter);
//                    }else{
//                        Toast.makeText(context,"No Menu Item",Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<MenuContentItem>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }


}
