package com.example.fooddeuk.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;

/**
 * Created by heojuyeong on 2017. 9. 22..
 */

public class LayoutUtil {

    public static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static void RecyclerViewSetting(Context context, RecyclerView recyclerView){
        final LinearLayoutManager nmLayoutManager = new LinearLayoutManager(context);
//        nmLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(nmLayoutManager);
        recyclerView.setHasFixedSize(true);
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



    public static float convertDpToPixel(float dp, Context context){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }
    public static int convertDpToPixelInt(int dp, Context context){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return Math.round(px);
    }




    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
    public static View getDivider(Context context,int left,int top,int right,int bottom){
        View division=new View(context);
        division.setBackgroundColor(Color.parseColor("#817e7e"));
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Math.round(LayoutUtil.convertDpToPixel((float)0.3,context)));
        layoutParams.setMargins(Math.round(LayoutUtil.convertDpToPixel(left,context)),top,right,bottom);
        return division;
    }






}
