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

public class ViewHolderWithPictureDoubleOption extends RecyclerView.ViewHolder {
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


    @BindView(R.id.menu_detail_total_price)
    public TextView menu_detail_total_price;



    public ViewHolderWithPictureDoubleOption(Context context, View itemView) {
        super(itemView);
        this.context=context;
        ButterKnife.bind(this, itemView);

    }


    public void bind(MenuContentItem menuContentItem, int position) {

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


        if (menuContentItem.description.length() > 1) {
            menu_detail_description.setText(menuContentItem.description);
        } else {
            menu_detail_description.setVisibility(View.GONE);
        }

        menu_detail_order.setText(menuPrice + " 바로 주문");


//        View.OnClickListener optionClickListener = v -> {
//            if (v == menu_detail_necessary_option_layout) {
//                optionDialogArray[0].show();
//            } else {
//                optionDialogArray[1].show();
//            }
//        };
////
//        menu_detail_necessary_option_layout.setOnClickListener(optionClickListener);
//        menu_detail_unnecessary_option_layout.setOnClickListener(optionClickListener);



    }






    /**
     * 필수 항목의 가장 첫번째 옵션 가져와 뷰에 뿌려주고 local db에 추가하기
     * param1 : Menu
     **/

//    void setFirstNecessaryOption(MenuContentItem menuContentItem) {
//        necessaryArrayList.clear();
//        ArrayList<OptionItem> optionCategory = new ArrayList<>();
//        for (int i = 0; i < menuContentItem.option.size(); i++) {
//            if (menuContentItem.option.get(i).necessary.size() > 0) {
//                optionCategory.add(menuContentItem.option.get(i));
//            }
//        }
//
//
//        StringBuffer necessaryCategoryText = new StringBuffer("");
//        StringBuffer necessaryOptionText = new StringBuffer("");
//
//        if (menuContentItem.option.size() == 1) {
//            OptionItem.Option necessaryBasicOption = optionCategory.get(0).necessary.get(0);
//            necessaryArrayList.add(necessaryBasicOption);
//            Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");
//            menu_detail_option_necessary_title.setText(optionCategory.get(0).menu_option_category_name);
//            menu_detail_option_necessary_content.setText(necessaryBasicOption.menu_option_name);
//
//        } else if (menuContentItem.option.size() > 1) {
//            for (int i = 0; i < optionCategory.size(); i++) {
//
//                OptionItem.Option necessaryBasicOption = optionCategory.get(i).necessary.get(0);
//                necessaryArrayList.add(necessaryBasicOption);
//                Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");
//
//                //Category
//                OptionItem categoryOption = optionCategory.get(i);
//
//
//                if (i != optionCategory.size() - 1) {
//                    necessaryCategoryText.append(categoryOption.menu_option_category_name + ",");
//                    necessaryOptionText.append(categoryOption.necessary.get(0).menu_option_name + ",");
//                } else {
//                    necessaryCategoryText.append(categoryOption.menu_option_category_name);
//                    necessaryOptionText.append(categoryOption.necessary.get(0).menu_option_name);
//                }
//            }
//
//            menu_detail_option_necessary_title.setText(necessaryCategoryText);
//            menu_detail_option_necessary_content.setText(necessaryOptionText);
//        }
//
//    }

}
