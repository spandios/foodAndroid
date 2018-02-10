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
import com.example.fooddeuk.model.restaurant.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heojuyeong on 2017. 10. 9..
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context context;
    public ArrayList<Restaurant> restaurantItem;
    private RestaurantItemClickListener restaurantItemClickListener;
//    HashMap<String, Integer> starDpMap;


    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurantItem) {
        this.context = context;
        this.restaurantItem = restaurantItem;
//        starDpMap = LayoutUtil.DpToLayoutParams(context, 12, (float) 11.5);
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
        Restaurant item = restaurantItem.get(position);

        Picasso.with(context).load(item.picture).resize(150, 150).into(holder.restaurantImageInList);
        holder.restaurantNameInList.setText(item.name);
        holder.restaurantReviewCountInList.setText(String.valueOf(item.reviewCnt));
        holder.restaurantAdminCommentInList.setText("todo");
        holder.restaurantRatingStarInList.removeAllViews();
//        setStarView(item.rating, holder.restaurantRatingStarInList);


    }

    @Override
    public int getItemCount() {
        return restaurantItem.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            restaurantItemClickListener.onItemClickListener(restaurantItem.get(getAdapterPosition()));
        }

        @BindView(R.id.restaurantImageInList)
        ImageView restaurantImageInList;
        @BindView(R.id.restaurantNameInList)
        TextView restaurantNameInList;
        @BindView(R.id.restaurantReviewCountInList)
        TextView restaurantReviewCountInList;
        @BindView(R.id.restaurantAdminCommentInList)
        TextView restaurantAdminCommentInList;
        @BindView(R.id.restaurantRatingStarInList)
        LinearLayout restaurantRatingStarInList;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
    }

//    private void setStarView(float rating, LinearLayout restaurantRatingStarInList) {
//        for (int j = 0; j < 5; j++) {
//            ImageView star = new ImageView(context);
//            star.setLayoutParams(new ViewGroup.LayoutParams(starDpMap.get("width"), starDpMap.get("height")));
//            if (rating == 0) {
//                star.setImageResource(R.drawable.ic_star);
//                restaurantRatingStarInList.addView(star);
//
//            } else if (rating >= 1.0) {
//                rating -= 1.0;
//                star.setImageResource(R.drawable.ic_star_ranked);
//                restaurantRatingStarInList.addView(star);
//
//            } else if (rating == 0.5) {
//                rating -= 0.5;
//                star.setImageResource(R.drawable.ic_star_ranked_half);
//                restaurantRatingStarInList.addView(star);
//
//            }
//        }
//    }

    public interface RestaurantItemClickListener {
        void onItemClickListener(Restaurant restaurant);
    }

    public void setRestaurantItemClickListener(RestaurantItemClickListener restaurantItemClickListener) {
        this.restaurantItemClickListener = restaurantItemClickListener;
    }

    public void updateRestaurant(ArrayList<Restaurant> restaurant){
            restaurantItem.clear();
            restaurantItem.addAll(restaurant);
            notifyDataSetChanged();
    }
}
