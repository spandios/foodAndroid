package com.example.fooddeuk.util;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;

/**
 * Created by heojuyeong on 2017. 10. 8..
 */

public class DialogUtil {
    Activity activity;


    public static void setRestaurantFilter(Activity activity, FilterCallback filterCallback) {
        MaterialDialog filterDialog = new MaterialDialog.Builder(activity).customView(R.layout.dialog_filter, true).build();
        View view = filterDialog.getView();
        ImageView rest_filter_exit = (ImageView) view.findViewById(R.id.rest_filter_exit);
        TextView rest_filter_sort_distance=(TextView)view.findViewById(R.id.rest_filter_sort_distance);
        TextView rest_filter_sort_order = (TextView) view.findViewById(R.id.rest_filter_sort_order);
        TextView rest_filter_sort_rating = (TextView) view.findViewById(R.id.rest_filter_sort_rating);
        TextView rest_filter_sort_review = (TextView) view.findViewById(R.id.rest_filter_sort_review);
        TextView rest_filter_sort_dangol = (TextView) view.findViewById(R.id.rest_filter_sort_dangol);
        TextView rest_filter_sort_discount = (TextView) view.findViewById(R.id.rest_filter_sort_discount);
        TextView distance2km = (TextView) view.findViewById(R.id.rest_filter_distance2km);
        TextView distance4km = (TextView) view.findViewById(R.id.rest_filter_distance4km);
        TextView distance6km = (TextView) view.findViewById(R.id.rest_filter_distance6km);

        rest_filter_sort_distance.setSelected(true);
        rest_filter_sort_distance.setTypeface(rest_filter_sort_distance.getTypeface(), Typeface.BOLD);
        //TODO FILTER
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            //todo SORT
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rest_filter_exit:
                        break;
                    case R.id.rest_filter_sort_distance:
                        filterCallback.filterResult(0, "distance");
                        break;
                    case R.id.rest_filter_sort_order:
//                        filterCallback.filterResult(0, "order"); is default
                        break;
                    case R.id.rest_filter_sort_rating:
                        filterCallback.filterResult(0, "rating");
                        break;
                    case R.id.rest_filter_sort_review:
//                        filterCallback.filterResult(0, "review");
                        break;
                    case R.id.rest_filter_sort_dangol:
//                        filterCallback.filterResult(0, "dangol");
                        break;
                    case R.id.rest_filter_sort_discount:
//                        filterCallback.filterResult(0, "discount");
                        break;
                    case R.id.rest_filter_distance2km:
                        filterCallback.filterResult(8000, null);
                        break;
                    case R.id.rest_filter_distance4km:
                        filterCallback.filterResult(10000, null);
                        break;
                    case R.id.rest_filter_distance6km:
                        filterCallback.filterResult(10000, null);
                        break;

                }
                filterDialog.hide();
            }
        };
        rest_filter_exit.setOnClickListener(onClickListener);
        distance2km.setOnClickListener(onClickListener);
        distance4km.setOnClickListener(onClickListener);
        distance6km.setOnClickListener(onClickListener);
        rest_filter_sort_distance.setOnClickListener(onClickListener);

        filterDialog.show();
    }


    public interface FilterCallback {
        void filterResult(int distance, String filterValue);
    }
}
