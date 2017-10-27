package com.example.fooddeuk.util;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import java.util.HashMap;

/**
 * Created by heojuyeong on 2017. 9. 22..
 */

public class LayoutUtil {



    public static void RecyclerViewSetting(Context context, RecyclerView recyclerView){
        final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context);
//        nmLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(nmLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public static HashMap<String, Integer> DpToLayoutParams(Context context,float widthDp,float heightDp){
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, context.getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp, context.getResources().getDisplayMetrics());
        HashMap<String,Integer> dpMap=new HashMap<>();
        dpMap.put("width",width);
        dpMap.put("height",height);
        return dpMap;
    }

    public static int DpToPx(Context context,float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }







}
