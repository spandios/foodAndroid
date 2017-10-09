package com.example.fooddeuk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.util.LayoutUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by heojuyeong on 2017. 7. 20..
 */


public class RestaurantAdapter2 extends ArrayAdapter<RestaurantItem.Restaurant>{
    private Context context;
    private int resource;
    private ArrayList<RestaurantItem.Restaurant> items;

    public RestaurantAdapter2(Context context, int resource, ArrayList<RestaurantItem.Restaurant> items){
        super(context,resource,items);
        this.context=context;
        this.resource=resource;
        this.items=items;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        CurrentListViewHolder viewHolder;
        if(row==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            row=inflater.inflate(resource,parent,false);
            viewHolder=new CurrentListViewHolder();
            //Image
            viewHolder.restaurantImageInList=(ImageView)row.findViewById(R.id.restaurantImageInList);
            //name
            viewHolder.restaurantNameInList=(TextView)row.findViewById(R.id.restaurantNameInList);
            //reviewCount
            viewHolder.restaurantReviewCountInList=(TextView)row.findViewById(R.id.restaurantReviewCountInList);
            //adminComment
            viewHolder.restaurantAdminCommentInList=(TextView)row.findViewById(R.id.restaurantAdminCommentInList);
            //star layout
            viewHolder.restaurantRatingStarInList=(LinearLayout)row.findViewById(R.id.restaurantRatingStarInList);

            row.setTag(viewHolder);
        }else{
            viewHolder=(CurrentListViewHolder)row.getTag();
        }

        HashMap<String,Integer> starDpMap= LayoutUtil.DpToLayoutParams(context,12,(float)11.5);
        RestaurantItem.Restaurant item=items.get(position);
        float rating=item.rating;
        if(rating>0.0){
            ImageView blackStar=new ImageView(context);
                                        blackStar.setImageResource(R.drawable.ic_star);
                                        blackStar.setLayoutParams(new ViewGroup.LayoutParams(starDpMap.get("width"),starDpMap.get("height")));

            viewHolder.restaurantRatingStarInList.addView(blackStar);
        }




        Picasso.with(context).load(item.getRest_picture()).resize(150,150).into(viewHolder.restaurantImageInList);
        viewHolder.restaurantNameInList.setText(item.getName());
//        viewHolder.cur_location_item_distance.setText(item.getDistance());
        viewHolder.restaurantReviewCountInList.setText(Integer.toString(item.getReviewcount()));

        //TODO restaurant comment!!!!
        viewHolder.restaurantAdminCommentInList.setText("123");


        return row;
    }


    private static class CurrentListViewHolder{
        public ImageView restaurantImageInList;
        public TextView restaurantNameInList;
        public TextView restaurantReviewCountInList;
        public TextView restaurantAdminCommentInList;
        public LinearLayout restaurantRatingStarInList;

//        public TextView cur_location_item_distance;

    }
}
