package com.example.fooddeuk.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fooddeuk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by heo on 2018. 1. 3..
 */

public class ImageViewPager extends PagerAdapter{

    Context context;
    ArrayList<String> images;
    LayoutInflater layoutInflater;


    public ImageViewPager(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_rest_detail_image_viewpager, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.rest_detail_image);

        Picasso.with(context).load(images.get(position)).fit().into(imageView);
        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

