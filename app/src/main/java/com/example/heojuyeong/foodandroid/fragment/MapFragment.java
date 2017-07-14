package com.example.heojuyeong.foodandroid.fragment;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.common.CommonLocationApplication;
import com.example.heojuyeong.foodandroid.common.CommonLocationApplication2;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_map_fragment,container,false);
        final TextView currentLocationTextview=(TextView)view.findViewById(R.id.currentLocationTextView);
        //getLocationName
        final CommonLocationApplication2 commonLocationApplication=(CommonLocationApplication2)getActivity().getApplication();
        currentLocationTextview.setText(commonLocationApplication.getLocationName());

        //MaterialDialog  and button setting
        final MaterialDialog materialDialog=new MaterialDialog.Builder(getActivity()).customView(R.layout.dialog_current_location,true).build();
        View dialogView=materialDialog.getView();

        final Button dialog_current_location_reload_button=(Button)dialogView.findViewById(R.id.dialog_current_location_reload_button);
        final Button dialog_current_location_map_button=(Button)dialogView.findViewById(R.id.dialog_current_location_map_button);
        final TextView dialog_current_location_cancle_textview=(TextView) dialogView.findViewById(R.id.dialog_current_location_cancle_textview);

        Button.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.currentLocationTextView:
                        materialDialog.show();
                        break;
                    case R.id.dialog_current_location_reload_button:
                        commonLocationApplication.settingLocation(getActivity());
                        currentLocationTextview.setText(commonLocationApplication.getLocationName());
                        materialDialog.dismiss();
                        break;
                    case R.id.dialog_current_location_map_button:
                        break;
                    case R.id.dialog_current_location_cancle_textview:
                        materialDialog.dismiss();
                        break;
                }
            }
        };
        currentLocationTextview.setOnClickListener(onClickListener);
        dialog_current_location_reload_button.setOnClickListener(onClickListener);
        dialog_current_location_map_button.setOnClickListener(onClickListener);
        dialog_current_location_cancle_textview.setOnClickListener(onClickListener);


        return view;
    }

}
