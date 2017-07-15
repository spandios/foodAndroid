package com.example.heojuyeong.foodandroid.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.heojuyeong.foodandroid.settingLocationMapActivity;

public class MapFragment extends Fragment {
    private TextView currentLocationTextview;
    private MaterialDialog materialDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_map_fragment,container,false);
        currentLocationTextview=(TextView)view.findViewById(R.id.currentLocationTextView);

        //MaterialDialog  and button setting
        materialDialog=new MaterialDialog.Builder(getActivity()).customView(R.layout.dialog_current_location,true).build();
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
                        LocationReloadAsyncTask locationReloadAsyncTask=new LocationReloadAsyncTask();
                        locationReloadAsyncTask.execute();
                        materialDialog.dismiss();
                        break;
                    case R.id.dialog_current_location_map_button:
                        Intent intent=new Intent(getActivity(),settingLocationMapActivity.class);
                        startActivity(intent);
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

    @Override
    public void onResume() {
        LocationReloadAsyncTask locationReloadAsyncTask=new LocationReloadAsyncTask();
        locationReloadAsyncTask.execute();
        super.onResume();
    }

    private class LocationReloadAsyncTask extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPostExecute(String locationName) {
            currentLocationTextview.setText(locationName);
        }

        @Override
        protected String doInBackground(Void... params) {
            CommonLocationApplication commonLocationApplication=(CommonLocationApplication)getActivity().getApplication();
            commonLocationApplication.settingLocation(getActivity().getBaseContext());
            return commonLocationApplication.getLocationName();
        }
    }
}
