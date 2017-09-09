package com.example.heojuyeong.foodandroid.listview;

import android.content.Context;

import android.support.v4.content.ContextCompat;

import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import android.widget.RadioButton;
import android.widget.TextView;


import com.afollestad.materialdialogs.MaterialDialog;

import com.example.heojuyeong.foodandroid.R;

import com.example.heojuyeong.foodandroid.item.MenuItem;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by heojuyeong on 2017. 7. 31..
 * MenuList
 */


public class MenuListHotAdapter extends RecyclerView.Adapter<MenuListHotAdapter.ViewHolder> {
    private ArrayList<MenuItem> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private int originalPrice;
    private int totalPrice;
    private MaterialDialog reviewDialog;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public MenuListHotAdapter(Context context, ArrayList<MenuItem> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.context = context;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
                reviewDialogShow(menuItem);
            }
        });


        holder.detailHotMenuReview.setText("리뷰[" + menuItem.getReviews().size() + "]");
        holder.detailHotMenuReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    originalPrice=menuItem.getPrice();
                    optionDialogShow(menuItem.getOptions(), 0);
                } else {

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

    //리뷰 dialog

    private void optionDialogShow(final ArrayList<MenuItem.Options> options, final int count) {
        //처음 시작

        //checkbox 여러개 선택가능할 때
        final ArrayList<MenuItem.Options.Option> optionItem=options.get(count).getOption();
        final MaterialDialog optionDialog = new MaterialDialog.Builder(context).customView(R.layout.dailog_menu_option_listview, true).build();
        final View optionDialogView = optionDialog.getView();

        TextView category_name = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_name);
        TextView category_leftcount = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_count);

        category_name.setText(options.get(count).getMenu_category_name());
        category_leftcount.setText("(" + (count + 1) + "/" + options.size() + ")");
        TextView category_necessary = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_necessary);

        final TextView dialog_totalPrice=(TextView)optionDialogView.findViewById(R.id.dialog_totalPrice);
        dialog_totalPrice.setText(Integer.toString(originalPrice));


        //필수일 때
        if (options.get(count).getNecessary() == 1) {
            category_necessary.setText("필수");
        } else {
            category_necessary.setText("필수아님");
        }

        final RecyclerView optionListView=(RecyclerView)optionDialogView.findViewById(R.id.dialog_menu_option_listview);
        OptionAdapter optionAdapter=new OptionAdapter(optionItem, context, options.get(count).getMultiple(), new OptionAdapter.RadioClickListener() {
            @Override
            public void onClickRadio(boolean isChecked, int position) {
                if(!isChecked){

                    for(int childCount=optionListView.getChildCount(), i=0; i<childCount; i++){
                        RecyclerView.ViewHolder viewHolder=optionListView.getChildViewHolder(optionListView.getChildAt(i));
                        RadioButton radioButton=(RadioButton)viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                        TextView optionPrice=(TextView)viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
                        if(radioButton.isChecked()){
                            radioButton.setChecked(false);
                            Logger.d(optionPrice);
                        }

                    }
                    RecyclerView.ViewHolder viewHolder=optionListView.getChildViewHolder(optionListView.getChildAt(position));
                    RadioButton radioButton=(RadioButton)viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                    radioButton.setChecked(true);




                }else {

                }
            }
        });



        final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context);
        optionListView.setLayoutManager(nmLayoutManager);
        optionListView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(optionListView.getContext(),nmLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context,R.drawable.divider));
        optionListView.addItemDecoration(dividerItemDecoration);
        optionListView.setAdapter(optionAdapter);



        LinearLayout dialog_menu_option_confirm_layout = (LinearLayout) optionDialogView.findViewById(R.id.dialog_menu_option_confirm_layout);
        //옵션 선택 카테고리 마지막이면
        if (options.size() == count + 1) {
            Button addCartButton = new Button(context);
            addCartButton.setText("주문표에 추가");

            //선택항목이 2개이상 일 때만 이전으로 버튼 생성
            if (options.size() != 1) {
                Button prevOptionButton = new Button(context);
                prevOptionButton.setText("이전으로");
                prevOptionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionDialog.dismiss();
                        optionDialogShow(options, count - 1);
                    }
                });
                dialog_menu_option_confirm_layout.addView(prevOptionButton);
            }

            dialog_menu_option_confirm_layout.addView(addCartButton);

        }
        //옵션 카테고리 마지막 x
        else {

            //맨 처음 시작이 아니라면
            if (count != 0) {
                Button prevOptionButton = new Button(context);
                prevOptionButton.setText("이전으로");
                prevOptionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionDialog.dismiss();
                        optionDialogShow(options, count - 1);
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
                    optionDialogShow(options, count + 1);
                }
            });

            dialog_menu_option_confirm_layout.addView(nextOptionButton);
        }

        optionDialog.show();

    }




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


//    public void createRadio(final ArrayList<MenuItem.Options.Option> optionItem, LinearLayout optionParent){
//        final RadioGroup menu_option_parent_layout=new RadioGroup(context);
//        menu_option_parent_layout.setOrientation(LinearLayout.VERTICAL);
//
//        for(int i=0; i<optionItem.size(); i++){
//            final RelativeLayout menu_option_layout = new RelativeLayout(context);
//            DisplayMetrics dm = context.getResources().getDisplayMetrics();
//            int marginSize = Math.round(10 * dm.density);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, marginSize, 0, 0);
//            menu_option_layout.setLayoutParams(params);
//
//            RadioGroup.LayoutParams layoutParams=new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0,marginSize,0,0);
//            menu_option_parent_layout.setLayoutParams(layoutParams);
//
//
//            //radio
//            final RadioButton optionRadio=new RadioButton(context);
//            optionRadio.setText(optionItem.get(i).getMenu_option_name());
//            optionRadio.setTextSize(20);
//            optionRadio.setTag(optionItem.get(i).getMenu_option_id());
//            optionRadio.setId(optionItem.get(i).getMenu_option_id());
//            menu_option_layout.addView(optionRadio);
//
//            menu_option_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(optionRadio.isChecked()){
//
//                    }else{
//
//                        optionRadio.setChecked(true);
//
//
//                    }
//
//                }
//            });
//
//
//            TextView optionPrice = new TextView(context);
//            String price = "+" + optionItem.get(i).getMenu_price() + "원";
//            optionPrice.setText(price);
//            optionPrice.setTextSize(20);
//            RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            priceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            optionPrice.setLayoutParams(priceParams);
//
//            menu_option_layout.addView(optionPrice);
//            View line = new View(context);
//
//            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
//            params1.setMargins(0, marginSize, 0, 0);
//            line.setLayoutParams(params1);
//            line.setBackgroundColor(Color.WHITE);
//
//
//            //radio group
//            menu_option_parent_layout.addView(menu_option_layout);
//
//            menu_option_parent_layout.addView(line);
//
//        }
//        optionParent.addView(menu_option_parent_layout);
//
//
//    }
//    void createCheckBox(ArrayList<MenuItem.Options.Option> optionItem,LinearLayout optionParent){
//        for (int i = 0; i < optionItem.size(); i++) {
//            RelativeLayout menu_option_layout = new RelativeLayout(context);
//            DisplayMetrics dm = context.getResources().getDisplayMetrics();
//            int marginSize = Math.round(10 * dm.density);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, marginSize, 0, 0);
//            menu_option_layout.setLayoutParams(params);
//
//            //checkbox
//            final CheckBox optionCheckbox = new CheckBox(context);
//            optionCheckbox.setText(optionItem.get(i).getMenu_option_name());
//            optionCheckbox.setTextSize(20);
//            optionCheckbox.setTag(optionItem.get(i).getMenu_option_id());
//            optionCheckbox.setId(optionItem.get(i).getMenu_option_id());
//            menu_option_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (optionCheckbox.isChecked()) {
//                        optionCheckbox.setChecked(false);
//                    } else {
//                        optionCheckbox.setChecked(true);
//                    }
//                }
//            });
//            menu_option_layout.addView(optionCheckbox);
//
//
//            //가격 textview
//            TextView optionPrice = new TextView(context);
//            String price = "+" + optionItem.get(i).getMenu_price() + "원";
//            optionPrice.setText(price);
//            optionPrice.setTextSize(20);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            optionPrice.setLayoutParams(layoutParams);
//
//
//            menu_option_layout.addView(optionPrice);
//            //라인
//
//
//            View line = new View(context);
//
//            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
//            params1.setMargins(0, marginSize, 0, 0);
//            line.setLayoutParams(params1);
//            line.setBackgroundColor(Color.WHITE);
//
//
//            optionParent.addView(menu_option_layout);
//            optionParent.addView(line);
//
//
//        }
//    }



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
