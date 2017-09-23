package com.example.heojuyeong.foodandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by heojuyeong on 2017. 8. 1..
 */

public abstract class BaseViewHolder<ITEM> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView){
        super(itemView);
    }

    public abstract void onBindView(ITEM item);

}
