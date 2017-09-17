package com.example.heojuyeong.foodandroid.listview;

import android.content.Context;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;


import android.widget.RadioButton;
import android.widget.TextView;


import com.afollestad.materialdialogs.MaterialDialog;

import com.example.heojuyeong.foodandroid.CartActivity;
import com.example.heojuyeong.foodandroid.R;

import com.example.heojuyeong.foodandroid.item.CurrentLocationListItem;
import com.example.heojuyeong.foodandroid.item.MenuItem;

import com.example.heojuyeong.foodandroid.item.OrderItem;
import com.example.heojuyeong.foodandroid.util.PriceCalculatorUtil;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by heojuyeong on 2017. 7. 31..
 * MenuList
 */


public class MenuListHotAdapter extends RecyclerView.Adapter<MenuListHotAdapter.ViewHolder> {
    private ArrayList<MenuItem> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private MaterialDialog reviewDialog;
    private CurrentLocationListItem.Restaurant restaurant;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public MenuListHotAdapter(Context context, ArrayList<MenuItem> items, CurrentLocationListItem.Restaurant restaurant, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.context = context;
        this.restaurant=restaurant;
        this.mOnItemClickListener = onItemClickListener;


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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MenuItem menuItem = items.get(position);


        Picasso.with(context).load(menuItem.getMenupicture()).fit().into(holder.hotMenuPicture);
        Picasso.with(context).load(menuItem.getMenupicture()).transform(new CropCircleTransformation()).into(holder.detailHotMenuPicture);
        holder.hotMenuName.setText(menuItem.getName());
//        holder.hotMenuRating.setText(menuItem.getRating());
        holder.hotMenuPrice.setText(String.valueOf(menuItem.getPrice()));

        holder.detailHotMenuName.setText(menuItem.getName());
        holder.detailHotMenuPrice.setText(String.valueOf(menuItem.getPrice()));
        holder.detailHotMenuRating.setText(menuItem.getRating());
        holder.detailHotMenuRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuItem.getReviews().size()>=1)
                    reviewDialogShow(menuItem);
            }
        });
        holder.detailHotMenuReview.setText("리뷰[" + menuItem.getReviews().size() + "]");
        holder.detailHotMenuReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuItem.getReviews().size()>=1)
                reviewDialogShow(menuItem);
            }
        });

        holder.detailHotMenuDesrciption.setText(menuItem.getDescription());


        //주문하기
        holder.detailHotMenuOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //옵션 선택사항이 있을 경우
                if (menuItem.getOptions().size() > 1) {

                    optionDialogShow(menuItem);
                } else {
                    //바로 주문

                }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });

    }

    //메뉴옵션 함수
    void optionDialogShow(final MenuItem menuItem) {

        ArrayList<MenuItem.Options> options=menuItem.getOptions();
        final ArrayList<MaterialDialog> optionDialogArray = new ArrayList<>();

        for (int i = 0; i < options.size(); i++) {
            final int index = i;
            final ArrayList<MenuItem.Options.Option> optionItem = options.get(i).getOption();
            final MaterialDialog optionDialog = new MaterialDialog.Builder(context).customView(R.layout.dailog_menu_option_listview, true).build();
            final View optionDialogView = optionDialog.getView();

            TextView category_name = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_name);
            TextView category_leftCount = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_count);
            TextView category_necessary = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_necessary);
            final TextView dialog_totalPrice = (TextView) optionDialogView.findViewById(R.id.dialog_totalPrice);

            final RecyclerView optionListView = (RecyclerView) optionDialogView.findViewById(R.id.dialog_menu_option_listview);

            category_name.setText(options.get(i).getMenu_category_name());
            category_leftCount.setText("(" + (i + 1) + "/" + options.size() + ")");
            category_necessary.setText((options.get(i).getNecessary() == 1) ? "필수" : "필수아님");


            dialog_totalPrice.setText(String.format(context.getResources().getString(R.string.won),String.valueOf(menuItem.getPrice())));


            OptionAdapter.SelectCLickListener selectCLickListener = new OptionAdapter.SelectCLickListener() {
                @Override
                public void onClickCheck(boolean isChecked, int position) {
                    RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(position));
                    TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
                    dialog_totalPrice.setText((isChecked) ? PriceCalculatorUtil.plusPrice(optionPrice, dialog_totalPrice) : PriceCalculatorUtil.minusPrice(optionPrice, dialog_totalPrice));
                }
            };


            OptionAdapter.RadioClickListener radioClickListener = new OptionAdapter.RadioClickListener() {

                @Override
                public void onClickRadio(boolean isChecked, int position) {
                    
                    if (!isChecked) {
                        for (int childCount = optionListView.getChildCount(), i = 0; i < childCount; i++) {
                            RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(i));
                            RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                            if (radioButton.isChecked()) {
                                radioButton.setChecked(false);
                                TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
                                dialog_totalPrice.setText(String.format(context.getResources().getString(R.string.won), PriceCalculatorUtil.unCheckRadio(dialog_totalPrice, optionPrice)));
                            }
                        }

                        RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(position));
                        RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                        TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);

                        radioButton.setChecked(true);
                        dialog_totalPrice.setText(String.format(context.getResources().getString(R.string.won), PriceCalculatorUtil.checkRadio(dialog_totalPrice, optionPrice)));

                    } else {

                    }
                }
            };


            final OptionAdapter optionAdapter = new OptionAdapter(optionItem, context, options.get(i).getMultiple(), radioClickListener, selectCLickListener);

            final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context);
            optionListView.setLayoutManager(nmLayoutManager);
            optionListView.setItemAnimator(new DefaultItemAnimator());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(optionListView.getContext(), nmLayoutManager.getOrientation());
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
            optionListView.addItemDecoration(dividerItemDecoration);
            optionListView.setAdapter(optionAdapter);

            LinearLayout dialog_menu_option_confirm_layout = (LinearLayout) optionDialogView.findViewById(R.id.dialog_menu_option_confirm_layout);
            //옵션 선택 카테고리 마지막이면
            if (options.size() == i + 1) {
                Button addCartButton=new Button(context);
                addCartButton.setText("장바구니 담기");
                addCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderItem orderItem=initOrder(optionDialogArray,menuItem);
                        optionDialog.dismiss();
                        Intent intent=new Intent(context, CartActivity.class);
                        context.startActivity(intent);

                    }
                });




                Button directOrderButton= new Button(context);
                directOrderButton.setText("바로 주문");
                directOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        OrderItem orderItem=initOrder(optionDialogArray,menuItem);
                        optionDialog.dismiss();
                    }
                });

                //선택항목이 2개이상 일 때만 이전으로 버튼 생성
                if (options.size() != 1) {
                    Button prevOptionButton = new Button(context);
                    prevOptionButton.setText("이전으로");
                    prevOptionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            optionDialog.dismiss();
                            TextView totalPrice = (TextView) optionDialogArray.get(index - 1).getView().findViewById(R.id.dialog_totalPrice);
                            totalPrice.setText(dialog_totalPrice.getText().toString());
                            optionDialogArray.get(index - 1).show();
                        }
                    });
                    dialog_menu_option_confirm_layout.addView(prevOptionButton);
                }
                dialog_menu_option_confirm_layout.addView(addCartButton);
                dialog_menu_option_confirm_layout.addView(directOrderButton);


            }
            //옵션 카테고리 마지막 x
            else {

                //맨 처음 시작이 아니라면
                if (i != 0) {
                    Button prevOptionButton = new Button(context);
                    prevOptionButton.setText("이전으로");
                    prevOptionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            optionDialog.dismiss();
                            TextView totalPrice = (TextView) optionDialogArray.get(index - 1).getView().findViewById(R.id.dialog_totalPrice);
                            totalPrice.setText(dialog_totalPrice.getText().toString());
                            optionDialogArray.get(index - 1).show();

                        }
                    });

                    dialog_menu_option_confirm_layout.addView(prevOptionButton);
                }


                Button nextOptionButton = new Button(context);
                nextOptionButton.setText("다음으로");
                nextOptionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionDialog.dismiss();
                        TextView totalPrice = (TextView) optionDialogArray.get(index + 1).getView().findViewById(R.id.dialog_totalPrice);
                        totalPrice.setText(dialog_totalPrice.getText().toString());
                        optionDialogArray.get(index + 1).show();
                    }
                });

                dialog_menu_option_confirm_layout.addView(nextOptionButton);
            }

            optionDialogArray.add(optionDialog);

        }
        optionDialogArray.get(0).show();

    }


    //리뷰dialogshow 함수
    public void reviewDialogShow(MenuItem menuItem) {

        reviewDialog = new MaterialDialog.Builder(context).customView(R.layout.dialog_review_listview, true).build();
        View reviewDialogView = reviewDialog.getView();
        RecyclerView reviewListView = (RecyclerView) reviewDialogView.findViewById(R.id.reviewListView);
        ReviewAdapter reviewAdapter = new ReviewAdapter(context, menuItem.getReviews());
        final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reviewListView.getContext(), nmLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        reviewListView.addItemDecoration(dividerItemDecoration);
        reviewListView.setLayoutManager(nmLayoutManager);
        reviewListView.setAdapter(reviewAdapter);
        reviewDialog.show();
    }



    public OrderItem initOrder(ArrayList<MaterialDialog> optionDialogArray,MenuItem menuItem){
        OrderItem orderItem;

        if(optionDialogArray.size()>=1){
            ArrayList<Integer> menuOptionNeccesary=new ArrayList<>();
            ArrayList<Integer> menuOptionNotNeccesary=new ArrayList<>();

            for(MaterialDialog optionDialog: optionDialogArray){
                View optionDialogView = optionDialog.getView();
                final RecyclerView optionListView = (RecyclerView) optionDialogView.findViewById(R.id.dialog_menu_option_listview);
                for (int childCount = optionListView.getChildCount(), i = 0; i < childCount; i++) {
                    RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(i));
                    RadioButton optionRadio=(RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                    CheckBox optionCheckBox=(CheckBox)viewHolder.itemView.findViewById(R.id.dialog_menu_option_checkbox);
                    if(optionRadio.getVisibility()==View.VISIBLE){

                        if(optionRadio.isChecked()){
                            menuOptionNeccesary.add(Integer.parseInt(optionRadio.getTag().toString()));
                        }

                    }else{
                        if(optionCheckBox.isChecked()){
                            menuOptionNotNeccesary.add(Integer.parseInt(optionCheckBox.getTag().toString()));
                        }
                    }
                }
            }

            orderItem=new OrderItem(menuItem.getMenu_id(),menuItem.getRest_id(),1,menuItem.getAvgtime(),restaurant.getName(),restaurant.getAddress(),menuOptionNeccesary,menuOptionNotNeccesary);
            Logger.d(orderItem.toString());
            return orderItem;
        }else{
            orderItem=new OrderItem(menuItem.getMenu_id(),menuItem.getRest_id(),1,menuItem.getAvgtime(),restaurant.getName(),restaurant.getAddress());
            Logger.d(orderItem.toString());
            return orderItem;
        }

    }







    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView hotMenuPicture;
        TextView hotMenuName;
        TextView hotMenuRating;
        TextView hotMenuPrice;
        ImageView detailHotMenuPicture;
        TextView detailHotMenuName;
        TextView detailHotMenuPrice;
        TextView detailHotMenuDesrciption;
        TextView detailHotMenuRating;
        TextView detailHotMenuReview;
        Button detailHotMenuCart;
        Button detailHotMenuOrder;


        public ViewHolder(View itemView) {
            super(itemView);
            hotMenuPicture = (ImageView) itemView.findViewById(R.id.hotMenuPicture);
            hotMenuName = (TextView) itemView.findViewById(R.id.hotMenuName);
//            hotMenuRating = (TextView) itemView.findViewById(R.id.hotMenuRating);
            hotMenuPrice = (TextView) itemView.findViewById(R.id.hotMenuPrice);
            detailHotMenuPicture = (ImageView) itemView.findViewById(R.id.detailHotMenuPicture);
            detailHotMenuName = (TextView) itemView.findViewById(R.id.detailHotMenuName);
            detailHotMenuPrice = (TextView) itemView.findViewById(R.id.detailHotMenuPrice);
            detailHotMenuDesrciption = (TextView) itemView.findViewById(R.id.detailHotMenuDescritption);
            detailHotMenuRating = (TextView) itemView.findViewById(R.id.detailHotMenuRating);
            detailHotMenuReview = (TextView) itemView.findViewById(R.id.detailHotMenuReview);
            detailHotMenuCart = (Button) itemView.findViewById(R.id.detailHotMenuCart);
            detailHotMenuOrder = (Button) itemView.findViewById(R.id.detailHotMenuOrder);

        }

    }


}
