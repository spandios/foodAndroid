package com.example.fooddeuk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.model.menu.Option;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OptionContentAdapter extends RecyclerView.Adapter<OptionContentAdapter.ViewHolder> {
    private ArrayList<Option> items;
    private Context context;
    private RadioClickListener radioClickListener;
    private SelectCLickListener selectCLickListener;
    private boolean multiple;

    public OptionContentAdapter(ArrayList<Option> items, Context context, boolean multiple, RadioClickListener radioClickListener, SelectCLickListener selectCLickListener) {
        this.items = items;
        this.context = context;
        this.multiple = multiple;
        this.radioClickListener = radioClickListener;
        this.selectCLickListener = selectCLickListener;
    }

    public interface RadioClickListener {
        void onClickRadio(int position,TextView plusTextView);
    }

    public interface SelectCLickListener {
        void onClickCheck(String isPlus, TextView priceTextView);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_menu_option_content_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        OptionContentAdapter.ViewHolder vh = new OptionContentAdapter.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Option option = items.get(position);

        //체크박스 셋팅
        if (multiple == true) {
            holder.optionCheckBox.setText(option.menu_option_name);
            holder.optionCheckBox.setTag(option);
            holder.optionLayout.setOnClickListener(v -> {
                //체크되어있으면 체크해제하고 값 minus
                if (holder.optionCheckBox.isChecked()) {
                    holder.optionCheckBox.setChecked(false);
                    selectCLickListener.onClickCheck("minus", holder.optionPrice);
                } else {
                    selectCLickListener.onClickCheck("plus", holder.optionPrice);
                    holder.optionCheckBox.setChecked(true);
                }
            });

            holder.optionCheckBox.setClickable(false);
            holder.optionRadio.setVisibility(View.GONE);
        }
        //라디오버튼 셋팅
        else {
            if(position==0){
                holder.optionRadio.setChecked(true);
            }
            holder.optionRadio.setText(option.menu_option_name);
            holder.optionRadio.setTag(option);
            holder.optionLayout.setOnClickListener(v -> {
                //체크되어있지 않는 라디오클릭에서만 허용
                if (!holder.optionRadio.isChecked()) {
                    holder.optionRadio.setChecked(true);
                    radioClickListener.onClickRadio(position,holder.optionPrice);
                }
            });
            //radio자체는 click X 전체 parent레이아웃 클릭시 체크 되게
            holder.optionRadio.setClickable(false);
            holder.optionCheckBox.setVisibility(View.GONE);
        }
        holder.optionPrice.setText(option.menu_option_price+"원");
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dialog_menu_option_checkbox)
        CheckBox optionCheckBox;
        @BindView(R.id.dialog_menu_option_radio)
        RadioButton optionRadio;
        @BindView(R.id.dialog_menu_option_price)
        TextView optionPrice;
        @BindView(R.id.dialog_menu_option_item_layout)
        RelativeLayout optionLayout;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }


}
