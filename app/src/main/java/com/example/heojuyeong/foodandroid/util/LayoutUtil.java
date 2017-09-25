package com.example.heojuyeong.foodandroid.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.heojuyeong.foodandroid.R;

/**
 * Created by heojuyeong on 2017. 9. 22..
 */

public class LayoutUtil {



    public static void RecyclerViewSetting(Context context, RecyclerView recyclerView){
        final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context);
        nmLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(nmLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_white));
        recyclerView.addItemDecoration(dividerItemDecoration);


        recyclerView.setHasFixedSize(true);

    }




}
