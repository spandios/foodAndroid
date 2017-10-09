package com.example.fooddeuk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by heojuyeong on 2017. 10. 9..
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RestaurantItem.Restaurant> restaurantItem;
    HashMap<String,Integer> starDpMap;

    public RestaurantAdapter(Context context, ArrayList<RestaurantItem.Restaurant> restaurantItem) {
        this.context = context;
        this.restaurantItem = restaurantItem;
        starDpMap= LayoutUtil.DpToLayoutParams(context,12,(float)11.5);
    }

    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        // set the view's size, margins, paddings and layout parameters
        RestaurantAdapter.ViewHolder vh = new RestaurantAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RestaurantItem.Restaurant item = restaurantItem.get(position);
        float rating=item.rating;
        Picasso.with(context).load(item.getRest_picture()).resize(150,150).into(holder.restaurantImageInList);
        holder.restaurantNameInList.setText(item.name);
        holder.restaurantReviewCountInList.setText(String.valueOf(item.reviewcount));
        holder.restaurantAdminCommentInList.setText("todo");



        for(int j=0; j<5; j++){
            ImageView star=new ImageView(context);
            star.setLayoutParams(new ViewGroup.LayoutParams(starDpMap.get("width"),starDpMap.get("height")));
            if(rating==0){
                //                ImageView blackStar=new ImageView(context);
//                blackStar.setImageResource(R.drawable.ic_star);
                star.setImageResource(R.drawable.ic_star);
                holder.restaurantRatingStarInList.addView(star);

            }else if(rating>=1.0){
                rating-=1.0;
                ImageView starRanked=new ImageView(context);
//                starRanked.setImageResource(R.drawable.ic_star_ranked);
                star.setImageResource(R.drawable.ic_star_ranked);

                holder.restaurantRatingStarInList.addView(star);

            }else if(rating==0.5){
                rating-=0.5;
//                ImageView halfRanked=new ImageView(context);
//                halfRanked.setImageResource(R.drawable.ic_star_ranked_half);
                star.setImageResource(R.drawable.ic_star_ranked_half);
                holder.restaurantRatingStarInList.addView(star);

            }
        }
    }

    @Override
    public int getItemCount() {
        return restaurantItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantImageInList;
        TextView restaurantNameInList;
        TextView restaurantReviewCountInList;
        TextView restaurantAdminCommentInList;
        LinearLayout restaurantRatingStarInList;


        ViewHolder(View itemView) {
            super(itemView);

            restaurantImageInList = (ImageView) itemView.findViewById(R.id.restaurantImageInList);
            restaurantNameInList = (TextView) itemView.findViewById(R.id.restaurantNameInList);
            restaurantReviewCountInList = (TextView) itemView.findViewById(R.id.restaurantReviewCountInList);
            restaurantAdminCommentInList = (TextView) itemView.findViewById(R.id.restaurantAdminCommentInList);
            restaurantRatingStarInList = (LinearLayout) itemView.findViewById(R.id.restaurantRatingStarInList);
        }
    }



}
