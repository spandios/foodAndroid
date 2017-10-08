package com.example.fooddeuk.util;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;

/**
 * Created by heojuyeong on 2017. 10. 8..
 */

public class DialogUtil {
    Activity activity;



    public static void setFilterRestaurant(Activity activity,FilterCallback filterCallback) {
        MaterialDialog filterDialog = new MaterialDialog.Builder(activity).customView(R.layout.dialog_filter, true).build();
        View filterDialogView = filterDialog.getView();
        Button distance2km = (Button) filterDialogView.findViewById(R.id.distance2);
        Button distance4km = (Button) filterDialogView.findViewById(R.id.distance4);
        Button distance6km = (Button) filterDialogView.findViewById(R.id.distance6);
        Button restaurantRating = (Button) filterDialogView.findViewById(R.id.restaurantRating);
        Button distanceDefault=(Button)filterDialogView.findViewById(R.id.distanceDefault);
        //TODO FILTER
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.distance2:
                        filterCallback.filterResult(8000,null);
                        break;
                    case R.id.distance4:
                        filterCallback.filterResult(10000,null);
                        break;
                    case R.id.distance6:
                        filterCallback.filterResult(10000,null);
                        break;
                    case R.id.distanceDefault:
                        filterCallback.filterResult(0,"distance");
                        break;
                    case R.id.restaurantRating:
                        filterCallback.filterResult(0,"rating");
                        break;
                }
                filterDialog.dismiss();
            }
        };
        distance2km.setOnClickListener(onClickListener);
        distance4km.setOnClickListener(onClickListener);
        distance6km.setOnClickListener(onClickListener);
        restaurantRating.setOnClickListener(onClickListener);
        distanceDefault.setOnClickListener(onClickListener);

        filterDialog.show();
    }


    public interface FilterCallback{
        void filterResult(int distance,String filterValue);
    }
}
