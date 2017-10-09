package com.example.fooddeuk.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.example.fooddeuk.R;

import java.util.HashMap;

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

    }

    public static HashMap<String, Integer> DpToLayoutParams(Context context,float widthDp,float heightDp){
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, context.getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp, context.getResources().getDisplayMetrics());
        HashMap<String,Integer> dpMap=new HashMap<>();
        dpMap.put("width",width);
        dpMap.put("height",height);
        return dpMap;
    }






}
