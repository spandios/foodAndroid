package com.example.heojuyeong.foodandroid.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.DetailRestaurantActivity;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.http.MenuService;
import com.example.heojuyeong.foodandroid.item.MenuItem;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuListHotAdapter extends RecyclerView.Adapter<MenuListHotAdapter.ViewHolder> {
    private ArrayList<MenuItem> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    private MaterialDialog materialDialog;



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
        holder.hotMenuRating.setText(menuItem.getRating());
        holder.hotMenuPrice.setText(menuItem.getPrice());

        holder.detailHotMenuName.setText(menuItem.getName());
        holder.detailHotMenuPrice.setText(menuItem.getPrice());
        holder.detailHotMenuRating.setText(menuItem.getRating());
        holder.detailHotMenuDesrciption.setText(menuItem.getDescription());

        //옵션이 없을 때
        if(menuItem.getOptions().size()==0){
            holder.detailHotMenuOptionLayOut1.setVisibility(View.GONE);
            holder.detailHotMenuOptionLayOut2.setVisibility(View.GONE);

        //옵션 2개 다 있을 경우
        }else if(menuItem.getOptions().size()==2) {

            //필수사항인 경우
            if(menuItem.getOptions().get(0).getNecessary()==1&&menuItem.getOptions().get(1).getNecessary()==1){
                holder.detailHotMenuOptionCateName1.setText(context.getString(R.string.necessary, menuItem.getOptions().get(0).getMenu_category_name()));
                holder.detailHotMenuOptionName1.setText(menuItem.getOptions().get(0).getOption().get(0).getMenu_option_name());


                holder.detailHotMenuOptionCateName2.setText(context.getString(R.string.necessary, menuItem.getOptions().get(1).getMenu_category_name()));
                holder.detailHotMenuOptionName2.setText(menuItem.getOptions().get(1).getOption().get(0).getMenu_option_name());

            }


            //선택사항인 경우
            else if (menuItem.getOptions().get(0).getNecessary() == 0 && menuItem.getOptions().get(1).getNecessary() == 0) {
                holder.detailHotMenuOptionCateName1.setText(context.getString(R.string.notnecessary, menuItem.getOptions().get(0).getMenu_category_name()));
                holder.detailHotMenuOptionCateName2.setText(context.getString(R.string.notnecessary, menuItem.getOptions().get(1).getMenu_category_name()));

            }
            //1. 필수 2. 선택일경우
            else if (menuItem.getOptions().get(0).getNecessary() == 1 && menuItem.getOptions().get(1).getNecessary() == 0) {
                holder.detailHotMenuOptionCateName1.setText(context.getString(R.string.necessary, menuItem.getOptions().get(0).getMenu_category_name()));
                holder.detailHotMenuOptionName1.setText(menuItem.getOptions().get(0).getOption().get(0).getMenu_option_name());


                holder.detailHotMenuOptionCateName2.setText(context.getString(R.string.notnecessary, menuItem.getOptions().get(1).getMenu_category_name()));

            }



        }
        //옵션 한개인경우
        else if(menuItem.getOptions().size()==1){
                holder.secondOptionLine.setVisibility(View.GONE);
                holder.detailHotMenuOptionLayOut2.setVisibility(View.GONE);

            //필수
            if(menuItem.getOptions().get(0).getNecessary()==1){
                holder.detailHotMenuOptionCateName1.setText(context.getString(R.string.necessary, menuItem.getOptions().get(0).getMenu_category_name()));
                holder.detailHotMenuOptionName1.setText(menuItem.getOptions().get(0).getOption().get(0).getMenu_option_name());
            }
            //선택

            else{
                holder.detailHotMenuOptionCateName1.setText(context.getString(R.string.notnecessary, menuItem.getOptions().get(0).getMenu_category_name()));

            }
        }




        holder.detailHotMenuOptionLayOut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여러개 선택 가능한 경우
                if(menuItem.getOptions().get(0).getNecessary()==0){

                    materialDialog=new MaterialDialog.Builder(context).customView(R.layout.dialog_menu_list_option_listview,true).build();
                    View getView=materialDialog.getView();
                    TextView categoryName=(TextView)getView.findViewById(R.id.dialog_option_category_name);
                    categoryName.setText(menuItem.getOptions().get(0).getMenu_category_name());

                    TextView optionCount=(TextView)getView.findViewById(R.id.dialog_option_category_count);
                    optionCount.setText("총"+menuItem.getOptions().get(0).getOption().size()+"개 선택가능");




                    RecyclerView recyclerView=(RecyclerView)getView.findViewById(R.id.dialog_option_listview);
                    final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context.getApplicationContext());
                    recyclerView.setLayoutManager(nmLayoutManager);
                    android.support.v7.widget.DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),nmLayoutManager.getOrientation());
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context,R.drawable.divider));
                    recyclerView.addItemDecoration(dividerItemDecoration);

                    OptionAdapter optionAdapter=new OptionAdapter(menuItem.getOptions().get(0).getOption(), context.getApplicationContext());
                    recyclerView.setAdapter(optionAdapter);



                }else{
                    materialDialog= new MaterialDialog.Builder(context).customView(R.layout.dialog_menu_list_option, true).build();
                    View getView=materialDialog.getView();
                    final LinearLayout menu_option_dialog_layout=(LinearLayout)getView.findViewById(R.id.dialog_menu_option_layout);
                    TextView textView=(TextView)getView.findViewById(R.id.modal_title);
                    textView.setText(menuItem.getOptions().get(0).getMenu_category_name());

                    for(int i=0; i<menuItem.getOptions().get(0).getOption().size(); i++){
                        final String menu_option_name=menuItem.getOptions().get(0).getOption().get(i).getMenu_option_name();
                        final int menu_option_price=menuItem.getOptions().get(0).getOption().get(i).getMenu_price();
                        Button button=new Button(context);
                        button.setText(menu_option_name+"                    "+menu_option_price+"원");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.detailHotMenuOptionName1.setText(menu_option_name);
                                holder.detailHotMenuPrice.setText(String.valueOf(menu_option_price));
                                materialDialog.dismiss();
                            }
                        });
                        menu_option_dialog_layout.addView(button);
                    }
                }

                materialDialog.show();
            }
        });

        holder.detailHotMenuOptionLayOut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(menuItem.getOptions().get(1).getNecessary()==0) {
                    materialDialog=new MaterialDialog.Builder(context).customView(R.layout.dialog_menu_list_option_listview,true).build();
                    View getView=materialDialog.getView();
                    TextView categoryName=(TextView)getView.findViewById(R.id.dialog_option_category_name);
                    categoryName.setText(menuItem.getOptions().get(1).getMenu_category_name());

                    TextView optionCount=(TextView)getView.findViewById(R.id.dialog_option_category_count);
                    optionCount.setText("총"+menuItem.getOptions().get(1).getOption().size()+"개 선택가능");




                    RecyclerView recyclerView=(RecyclerView)getView.findViewById(R.id.dialog_option_listview);
                    final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context.getApplicationContext());
                    recyclerView.setLayoutManager(nmLayoutManager);
                    android.support.v7.widget.DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),nmLayoutManager.getOrientation());
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context,R.drawable.divider));
                    recyclerView.addItemDecoration(dividerItemDecoration);

                    OptionAdapter optionAdapter=new OptionAdapter(menuItem.getOptions().get(1).getOption(), context.getApplicationContext());
                    recyclerView.setAdapter(optionAdapter);

                }else{
                    materialDialog= new MaterialDialog.Builder(context).customView(R.layout.dialog_menu_list_option, true).build();
                    View getView=materialDialog.getView();
                    final LinearLayout menu_option_dialog_layout=(LinearLayout)getView.findViewById(R.id.dialog_menu_option_layout);
                    TextView title=(TextView)getView.findViewById(R.id.modal_title);
                    title.setText(menuItem.getOptions().get(1).getMenu_category_name());
                    for(int i=0; i<menuItem.getOptions().get(1).getOption().size(); i++){
                        final String menu_option_name=menuItem.getOptions().get(1).getOption().get(i).getMenu_option_name();
                        Button button=new Button(context);
                        button.setText(menu_option_name);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.detailHotMenuOptionName2.setText(menu_option_name);
                                materialDialog.dismiss();
                            }
                        });

                        menu_option_dialog_layout.addView(button);
                    }
                }

                materialDialog.show();
            }
        });




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
        TextView detailHotMenuDesrciption;
        TextView detailHotMenuRating;
        TextView detailHotMenuOptionCateName1;
        TextView detailHotMenuOptionName1;
        TextView detailHotMenuOptionCateName2;
        TextView detailHotMenuOptionName2;
        LinearLayout detailHotMenu;
        RelativeLayout detailHotMenuOptionLayOut1;
        RelativeLayout detailHotMenuOptionLayOut2;
        View secondOptionLine;


        public ViewHolder(View itemView) {
            super(itemView);
            hotMenuPicture = (ImageView) itemView.findViewById(R.id.hotMenuPicture);
            hotMenuName = (TextView) itemView.findViewById(R.id.hotMenuName);
            hotMenuRating = (TextView) itemView.findViewById(R.id.hotMenuRating);
            hotMenuPrice = (TextView) itemView.findViewById(R.id.hotMenuPrice);
            detailHotMenuPicture = (ImageView) itemView.findViewById(R.id.detailHotMenuPicture);
            detailHotMenuName = (TextView) itemView.findViewById(R.id.detailHotMenuName);
            detailHotMenuPrice = (TextView) itemView.findViewById(R.id.detailHotMenuPrice);
            detailHotMenuDesrciption = (TextView) itemView.findViewById(R.id.detailHotMenuDescritption);
            detailHotMenuRating = (TextView) itemView.findViewById(R.id.detailHotMenuRating);

            detailHotMenuOptionCateName1=(TextView)itemView.findViewById(R.id.detailHotMenuOptionCateName1);
            detailHotMenuOptionName1=(TextView)itemView.findViewById(R.id.detailHotMenuOptionName1);

            detailHotMenuOptionCateName2=(TextView)itemView.findViewById(R.id.detailHotMenuOptionCateName2);
            detailHotMenuOptionName2=(TextView)itemView.findViewById(R.id.detailHotMenuOptionName2);


            detailHotMenu=(LinearLayout)itemView.findViewById(R.id.detailHotMenu);

            detailHotMenuOptionLayOut1=(RelativeLayout)itemView.findViewById(R.id.detailHotMenuOptionLayOut1);
            detailHotMenuOptionLayOut2=(RelativeLayout)itemView.findViewById(R.id.detailHotMenuOptionLayOut2);

            secondOptionLine=itemView.findViewById(R.id.secondOptionLine);

        }


    }



}
