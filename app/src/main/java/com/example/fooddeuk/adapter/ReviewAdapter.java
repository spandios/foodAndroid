package com.example.fooddeuk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.DetailReviewImageActivity;
import com.example.fooddeuk.model.menu.ReviewItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context context;
    private ArrayList<ReviewItem> items;


    public ReviewAdapter(Context context, ArrayList<ReviewItem> items){
        this.context=context;
        this.items=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_review, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ReviewAdapter.ViewHolder vh = new ReviewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReviewItem reviewItem=items.get(position);
        Picasso.with(context).load(reviewItem.getProfile_image()).fit().into(holder.detailHotMenuReviewerImage);
        holder.detailHotMenuReviewerId.setText(reviewItem.getName());
        holder.detailHotMenuReviewContent.setText(reviewItem.getContent());
        if(reviewItem.getImage().isEmpty()){
            holder.detailHotMenuReviewImage.setVisibility(View.GONE);
        }else{
            Picasso.with(context).load(reviewItem.getImage()).fit().into(holder.detailHotMenuReviewImage);
            holder.detailHotMenuReviewImage.setOnClickListener(v -> {
                Intent intent=new Intent(context, DetailReviewImageActivity.class);
                intent.putExtra("reviewImage", reviewItem.getImage());
                context.startActivity(intent);
            });
        }

        holder.detailHotMenuReviewDate.setText(reviewItem.getCreated_at().substring(0,10));
        holder.detailHotMenuReviewStar.setText(Double.toString(reviewItem.getRating()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView detailHotMenuReviewerImage;
        TextView detailHotMenuReviewerId;
        TextView detailHotMenuReviewDate;
        TextView detailHotMenuReviewStar;
        ImageView detailHotMenuReviewImage;
        TextView detailHotMenuReviewContent;


        public ViewHolder(View itemView) {
            super(itemView);
            detailHotMenuReviewerImage= itemView.findViewById(R.id.detailHotMenuReviewerImage);
            detailHotMenuReviewerId= itemView.findViewById(R.id.detailHotMenuReviewerId);
            detailHotMenuReviewDate= itemView.findViewById(R.id.detailHotMenuReviewDate);
            detailHotMenuReviewStar= itemView.findViewById(R.id.detailHotMenuReviewStar);
            detailHotMenuReviewImage= itemView.findViewById(R.id.detailHotMenuReviewImage);
            detailHotMenuReviewContent= itemView.findViewById(R.id.detailHotMenuReviewContent);


        }
    }
}
