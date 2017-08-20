package com.example.heojuyeong.foodandroid.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.item.MenuItem;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by jinchengjun-- on 2017-08-20.
 */

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder>{
    private ArrayList<MenuItem.Options.Option> items;
    private Context context;

    public OptionAdapter(ArrayList<MenuItem.Options.Option> items, Context context){
        this.items=items;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_menu_list_option_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        OptionAdapter.ViewHolder vh = new OptionAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MenuItem.Options.Option option=items.get(position);
        holder.optionCheckBox.setText(option.getMenu_option_name());

        holder.optionPrice.setText("+"+option.getMenu_price()+"원");

        //쳌크하기
        holder.optionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.optionCheckBox.isChecked()){
                    holder.optionCheckBox.setChecked(FALSE);
                }else{
                    holder.optionCheckBox.setChecked(TRUE);
                }

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       CheckBox optionCheckBox;
       TextView optionPrice;
       RelativeLayout optionLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            optionCheckBox=(CheckBox)itemView.findViewById(R.id.dialog_menu_option_checkbox);
            optionPrice=(TextView)itemView.findViewById(R.id.dialog_menu_option_price);
            optionLayout=(RelativeLayout)itemView.findViewById(R.id.dialog_menu_option_item_layout);


        }
    }


}
