package com.example.fooddeuk.viewholder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.model.menu.MenuContentItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by heo on 2018. 1. 20..
 */

public class ViewHolderWithPictureUnNecessary extends RecyclerView.ViewHolder {
    public Context context;
    //Layout
    @BindView(R.id.menu_master_layout)
    public ConstraintLayout menu_master_layout;
    @BindView(R.id.menu_detail_layout)
    public LinearLayout menu_detail_layout;

    //Master
    @BindView(R.id.menu_master_picture)
    public ImageView menu_master_picture;
    @BindView(R.id.menu_master_name)
    public TextView menu_master_name;
    @BindView(R.id.menu_master_price)
    public TextView menu_master_price;

    //Detail
    @BindView(R.id.menu_detail_picture)
    public ImageView detailHotMenuPicture;
    @BindView(R.id.menu_detail_name)
    public TextView menu_detail_name;
    @BindView(R.id.menu_detail_price)
    public TextView menu_detail_price;
    @BindView(R.id.menu_detail_description)
    public TextView menu_detail_description;
    @BindView(R.id.menu_detail_rating)
    public TextView menu_detail_rating;
    @BindView(R.id.menu_detail_rating_bar)
    public RatingBar menu_detail_rating_bar;
    @BindView(R.id.menu_detail_review)
    public TextView menu_detail_review;
    @BindView(R.id.menu_detail_cart)
    public Button menu_detail_cart;
    @BindView(R.id.menu_detail_order)
    public Button menu_detail_order;


    @BindView(R.id.menu_detail_unnecessary_option_layout)
    public RelativeLayout menu_detail_unnecessary_option_layout;

    @BindView(R.id.menu_detail_option_unnecessary_content)
    public TextView menu_detail_option_unnecessary_content;


    @BindView(R.id.menu_detail_total_price)
    public TextView menu_detail_total_price;


//        @BindView(R.id.menu_master_basic_price)
//        TextView menu_master_basic_price;

    public ViewHolderWithPictureUnNecessary(Context context,View itemView) {
        super(itemView);
        this.context=context;
        ButterKnife.bind(this, itemView);

    }


    //선택옵션 바텀시트 다이어로그


    public void bind(MenuContentItem menuContentItem) {
        String reviewCount = "(" + menuContentItem.reviewCnt + ")";
        String menuPrice = menuContentItem.price+"원";
        if (menuContentItem.rating.length() == 1) {
            menuContentItem.rating += ".0";
        }

        Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(menu_master_picture);
        Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(detailHotMenuPicture);
        menu_master_name.setText(menuContentItem.name);
        menu_master_price.setText(menuPrice);
        menu_detail_name.setText(menuContentItem.name);
        menu_detail_name.setText(menuContentItem.name);
        menu_detail_price.setText(menuPrice);
        menu_detail_rating.setText(menuContentItem.rating);
        menu_detail_review.setText(reviewCount);
        menu_detail_rating_bar.setRating(Float.parseFloat(menuContentItem.rating));
        menu_detail_rating_bar.setStepSize(0.5f);
        menu_detail_order.setText(menuPrice + " 바로주문");
        if (menuContentItem.description.length() > 1) {
            menu_detail_description.setText(menuContentItem.description);
        } else {
            menu_detail_description.setVisibility(View.GONE);
        }

    }
}
