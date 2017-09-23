package com.example.heojuyeong.foodandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heojuyeong.foodandroid.DetailReviewImageActivity;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.model.MenuItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context context;
    private ArrayList<MenuItem.Review> items;

    public ReviewAdapter(Context context, ArrayList<MenuItem.Review> items){
        this.context=context;
        this.items=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_review_listview_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ReviewAdapter.ViewHolder vh = new ReviewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            final MenuItem.Review reviewItem=items.get(position);
        Picasso.with(context).load(reviewItem.getReviewer().getProfile_image()).fit().into(holder.detailHotMenuReviewerImage);
        holder.detailHotMenuReviewerId.setText(reviewItem.getReviewer().getName());
        holder.detailHotMenuReviewContent.setText(reviewItem.getContent());

        if(reviewItem.getImage().isEmpty()){
            holder.detailHotMenuReviewImage.setVisibility(View.GONE);
        }else{
            Picasso.with(context).load(reviewItem.getImage()).fit().into(holder.detailHotMenuReviewImage);
            holder.detailHotMenuReviewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DetailReviewImageActivity.class);
                    intent.putExtra("reviewImage", reviewItem.getImage());
                    context.startActivity(intent);
                }
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
            detailHotMenuReviewerImage=(ImageView)itemView.findViewById(R.id.detailHotMenuReviewerImage);
            detailHotMenuReviewerId=(TextView)itemView.findViewById(R.id.detailHotMenuReviewerId);
            detailHotMenuReviewDate=(TextView)itemView.findViewById(R.id.detailHotMenuReviewDate);
            detailHotMenuReviewStar=(TextView)itemView.findViewById(R.id.detailHotMenuReviewStar);
            detailHotMenuReviewImage=(ImageView)itemView.findViewById(R.id.detailHotMenuReviewImage);
            detailHotMenuReviewContent=(TextView)itemView.findViewById(R.id.detailHotMenuReviewContent);


        }
    }
}
