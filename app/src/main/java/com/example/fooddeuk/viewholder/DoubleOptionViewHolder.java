package com.example.fooddeuk.viewholder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.MenuDetailVPAdapter;
import com.example.fooddeuk.model.menu.Menu;
import com.example.fooddeuk.network.HTTP;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.example.fooddeuk.GlobalApplication.httpService;

/**
 * Created by heo on 2018. 1. 20..
 */

public class DoubleOptionViewHolder extends RecyclerView.ViewHolder {
    public Context context;
    //Layout
    @BindView(R.id.menu_master_layout)
    public RelativeLayout menu_master_layout;
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
    @BindView(R.id.vp_menu_detail)
    public ViewPager vp_menu_detail;

    @BindView(R.id.menu_detail_order)
    public Button menu_detail_order;


    @BindView(R.id.menu_detail_necessary_option_layout)
    public RelativeLayout menu_detail_necessary_option_layout;

    @BindView(R.id.menu_detail_option_necessary_title)
    public TextView menu_detail_option_necessary_title;

    @BindView(R.id.menu_detail_option_necessary_content)
    public TextView menu_detail_option_necessary_content;

    @BindView(R.id.menu_detail_option_unnecessary_content)
    public TextView menu_detail_option_unnecessary_content;

    @BindView(R.id.menu_detail_unnecessary_option_layout)
    public RelativeLayout menu_detail_unnecessary_option_layout;



    public DoubleOptionViewHolder(Context context, View itemView) {
        super(itemView);
        this.context=context;
        ButterKnife.bind(this, itemView);
    }


    public void bind(Menu menu) {

        String menuPrice = menu.price+"원";
        if (menu.rating.length() == 1) {
            menu.rating += ".0";
        }

        HTTP.INSTANCE.Single(httpService.getReview(menu.menu_id)).subscribe(reviewResponse -> {

            if(reviewResponse.getSuccess()){
                MenuDetailVPAdapter menuDetailVPAdapter=new MenuDetailVPAdapter(context,menu,reviewResponse.getResult());
                vp_menu_detail.setAdapter(menuDetailVPAdapter);
                OverScrollDecoratorHelper.setUpOverScroll(vp_menu_detail);
            }
        }, Throwable::printStackTrace);
        Picasso.with(context).load(menu.picture[0]).fit().transform(new CropCircleTransformation()).into(menu_master_picture);
        menu_master_name.setText(menu.name);
        menu_master_price.setText(menuPrice);
        menu_detail_order.setText(menuPrice + " 바로 주문");



    }
}
