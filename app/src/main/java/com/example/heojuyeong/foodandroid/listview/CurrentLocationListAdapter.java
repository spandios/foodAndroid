package com.example.heojuyeong.foodandroid.listview;

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
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 7. 20..
 */

public class CurrentLocationListAdapter extends ArrayAdapter<CurrentLocationListItem.Restaurant>{
    private Context context;
    private int resource;
    private ArrayList<CurrentLocationListItem.Restaurant> items;


    public CurrentLocationListAdapter(Context context, int resource, ArrayList<CurrentLocationListItem.Restaurant> items){
        super(context,resource,items);
        this.context=context;
        this.resource=resource;
        this.items=items;
        if(context==null){
            Logger.d("context is null");
        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        CurrentListViewHolder viewHolder;

        //리스트 뷰  처음 호출 시 converview는 null이므로 첫 1회 viewholder를 생성한다.
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
            //캐쉬된이후로는 viewholder를 가져와 findViewByID를 호출하지 않으므로 메모리 효율이 좋아진다 .
            viewHolder=(CurrentListViewHolder)row.getTag();
        }
        CurrentLocationListItem.Restaurant item=items.get(position);

        Picasso.with(context).load(item.rest_picture).resize(150,150).into(viewHolder.cur_location_item_image);

        viewHolder.cur_location_item_rest_name.setText(item.getName());
        viewHolder.cur_location_item_rating.setText(Integer.toString(item.rating));
        viewHolder.cur_location_item_distance.setText(Double.toString(item.distance));
        viewHolder.cur_location_item_reviewcount.setText(Integer.toString(item.reviewcount));
        return row;
    }


    static class CurrentListViewHolder{
        public ImageView cur_location_item_image;
        public TextView cur_location_item_rest_name;
        public TextView cur_location_item_rating;
        public TextView cur_location_item_reviewcount;
        public TextView cur_location_item_distance;
    }
}
