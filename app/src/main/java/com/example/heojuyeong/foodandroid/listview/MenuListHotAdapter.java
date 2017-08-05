package com.example.heojuyeong.foodandroid.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.item.MenuItem;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuListHotAdapter extends RecyclerView.Adapter<MenuListHotAdapter.ViewHolder> {
    private ArrayList<MenuItem> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private OnSizeClickListener mOnSizeClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnSizeClickListener{
        void onSizeClick(View view,int position);
    }


    public MenuListHotAdapter(Context context, ArrayList<MenuItem> items, OnItemClickListener onItemClickListener, OnSizeClickListener onSizeClickListener) {
        this.items = items;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
        this.mOnSizeClickListener=onSizeClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_listview_hot_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MenuItem menuItem = items.get(position);

        Picasso.with(context).load(menuItem.getMenupicture()).fit().into(holder.hotMenuPicture);
        Picasso.with(context).load(menuItem.getMenupicture()).transform(new CropCircleTransformation()).into(holder.detailHotMenuPicture);
        holder.hotMenuName.setText(menuItem.getName());
        holder.hotMenuRating.setText(menuItem.getRating());
        holder.hotMenuPrice.setText(menuItem.getPrice());
        holder.detailHotMenuSizeLayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSizeClickListener.onSizeClick(v,holder.getAdapterPosition());
            }
        });
        if (menuItem.getSize().size() == 0) {
            holder.detailHotMenuSizeArrow.setVisibility(View.INVISIBLE);
            holder.detailHotMenuSizeText.setText("기본사이즈");

        } else {
            holder.detailHotMenuSizeText.setText(menuItem.getSize().get(0).getSize_name());
        }

        holder.detailHotMenuName.setText(menuItem.getName());
        holder.detailHotMenuPrice.setText(menuItem.getPrice());
        holder.detailHotMenuRating.setText(menuItem.getRating());
        holder.detailHotMenuDesrciption.setText(menuItem.getDescription());

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                        holder.detailHotMenu.setVisibility(View.VISIBLE);
//                        holder.masterHotMenu.setVisibility(View.GONE);
//                        clickFlag=true;
//
//                }
//            });
//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView hotMenuPicture;
        TextView hotMenuName;
        TextView hotMenuRating;
        TextView hotMenuPrice;
        ImageView detailHotMenuPicture;
        TextView detailHotMenuName;
        TextView detailHotMenuPrice;
        TextView detailHotMenuSizeText;
        TextView detailHotMenuDesrciption;
        TextView detailHotMenuRating;
        ImageView detailHotMenuSizeArrow;
        RelativeLayout detailHotMenuSizeLayOut;


        public ViewHolder(View itemView) {
            super(itemView);
            hotMenuPicture = (ImageView) itemView.findViewById(R.id.hotMenuPicture);
            hotMenuName = (TextView) itemView.findViewById(R.id.hotMenuName);
            hotMenuRating = (TextView) itemView.findViewById(R.id.hotMenuRating);
            hotMenuPrice = (TextView) itemView.findViewById(R.id.hotMenuPrice);
            detailHotMenuPicture = (ImageView) itemView.findViewById(R.id.detailHotMenuPicture);
            detailHotMenuName = (TextView) itemView.findViewById(R.id.detailHotMenuName);
            detailHotMenuPrice = (TextView) itemView.findViewById(R.id.detailHotMenuPrice);
            detailHotMenuSizeText = (TextView) itemView.findViewById(R.id.detailHotMenuSizeText);
            detailHotMenuDesrciption = (TextView) itemView.findViewById(R.id.detailHotMenuDescritption);
            detailHotMenuRating = (TextView) itemView.findViewById(R.id.detailHotMenuRating);
            detailHotMenuSizeArrow=(ImageView)itemView.findViewById(R.id.detailHotMenuSizeArrow);
            detailHotMenuSizeLayOut = (RelativeLayout) itemView.findViewById(R.id.detailHotMenuSizeLayOut);
        }


    }

}
