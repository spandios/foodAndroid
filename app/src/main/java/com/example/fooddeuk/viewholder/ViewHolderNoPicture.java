package com.example.fooddeuk.viewholder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.model.menu.Menu;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heo on 2018. 1. 20..
 */

public class ViewHolderNoPicture extends RecyclerView.ViewHolder {
    //Layout
    @BindView(R.id.menu_master_layout)
    public ConstraintLayout menu_master_layout;
    @BindView(R.id.menu_detail_layout)
    public LinearLayout menu_detail_layout;


    //Master
    @BindView(R.id.menu_master_name)
    public TextView menu_master_name;
    @BindView(R.id.menu_master_price)
    public TextView menu_master_price;

    //Detail
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


    public ViewHolderNoPicture(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Menu menu, int position) {
        String reviewCount = "(" + menu.reviewCnt + ")";
        String menuPrice = menu.price + "원";
        if (menu.rating.length() == 1) {
            menu.rating += ".0";
        }
        menu_master_name.setText(menu.getName());
        menu_master_price.setText(menuPrice);
        menu_detail_name.setText(menu.getName());
        menu_detail_name.setText(menu.getName());
        menu_detail_price.setText(menuPrice);
        menu_detail_rating.setText(menu.rating);
        menu_detail_review.setText(reviewCount);
        menu_detail_rating_bar.setRating(Float.parseFloat(menu.rating));
        menu_detail_rating_bar.setStepSize(0.5f);
        menu_detail_description.setText(menu.getDescription());

    }


}
