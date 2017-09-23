package com.example.heojuyeong.foodandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.model.CurrentLocationRestaurantItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 20..
 */


public class CurrentLocationListAdapter extends ArrayAdapter<CurrentLocationRestaurantItem.Restaurant>{
    private Context context;
    private int resource;
    private ArrayList<CurrentLocationRestaurantItem.Restaurant> items;

    public CurrentLocationListAdapter(Context context, int resource, ArrayList<CurrentLocationRestaurantItem.Restaurant> items){
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
            viewHolder.cur_location_item_image=(ImageView)row.findViewById(R.id.cur_location_item_image);
            viewHolder.cur_location_item_rest_name=(TextView)row.findViewById(R.id.cur_location_item_rest_name);
            viewHolder.cur_location_item_rating=(TextView)row.findViewById(R.id.cur_location_item_rating);
            viewHolder.cur_location_item_reviewcount=(TextView)row.findViewById(R.id.cur_location_item_reviewcount);
            viewHolder.cur_location_item_distance=(TextView)row.findViewById(R.id.cur_location_item_distance);
            row.setTag(viewHolder);
        }else{
            viewHolder=(CurrentListViewHolder)row.getTag();
        }

        CurrentLocationRestaurantItem.Restaurant item=items.get(position);
        Picasso.with(context).load(item.getRest_picture()).resize(150,150).into(viewHolder.cur_location_item_image);
        viewHolder.cur_location_item_rest_name.setText(item.getName());
        viewHolder.cur_location_item_rating.setText(Double.toString(item.getRating()));
        viewHolder.cur_location_item_distance.setText(item.getDistance());
        viewHolder.cur_location_item_reviewcount.setText(Integer.toString(item.getReviewcount()));
        return row;
    }


    private static class CurrentListViewHolder{

        public ImageView cur_location_item_image;
        public TextView cur_location_item_rest_name;
        public TextView cur_location_item_rating;
        public TextView cur_location_item_reviewcount;
        public TextView cur_location_item_distance;
    }
}
