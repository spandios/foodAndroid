package com.example.heojuyeong.foodandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.model.MenuItem;
import com.example.heojuyeong.foodandroid.util.PriceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private ArrayList<MenuItem.Options.Option> items;
    private Context context;
    private RadioClickListener radioClickListener;
    private SelectCLickListener selectCLickListener;
    private int multiple;

    public OptionAdapter(ArrayList<MenuItem.Options.Option> items, Context context, int multiple, RadioClickListener radioClickListener, SelectCLickListener selectCLickListener) {
        this.items = items;
        this.context = context;
        this.multiple = multiple;
        this.radioClickListener = radioClickListener;
        this.selectCLickListener = selectCLickListener;
    }

    public interface RadioClickListener {
        void onClickRadio(int position);
    }

    public interface SelectCLickListener {
        void onClickCheck(String isPlus, int position);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_menu_list_option_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        OptionAdapter.ViewHolder vh = new OptionAdapter.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MenuItem.Options.Option option = items.get(position);

        //체크박스 셋팅
        if (multiple == 1) {
            holder.optionCheckBox.setText(option.getMenu_option_name());
            holder.optionCheckBox.setTag(option);
            holder.optionLayout.setOnClickListener(v -> {
                //체크되어있으면 체크해제하고 값 minus
                if (holder.optionCheckBox.isChecked()) {
                    holder.optionCheckBox.setChecked(false);
                    selectCLickListener.onClickCheck("minus", holder.getAdapterPosition());
                } else {
                    selectCLickListener.onClickCheck("plus", holder.getAdapterPosition());
                    holder.optionCheckBox.setChecked(true);
                }
            });

            holder.optionCheckBox.setClickable(false);
            holder.optionRadio.setVisibility(View.GONE);
        }
        //라디오버튼 셋팅
        else {
            holder.optionRadio.setText(option.getMenu_option_name());
            holder.optionRadio.setTag(option);
            holder.optionLayout.setOnClickListener(v -> {
                //체크되어있지 않는 라디오클릭에서만 허용
                if (!holder.optionRadio.isChecked()) {
                    radioClickListener.onClickRadio(holder.getAdapterPosition());
                }
            });
            //radio자체는 click X 전체 parent레이아웃 클릭시 체크 되게
            holder.optionRadio.setClickable(false);
            holder.optionCheckBox.setVisibility(View.GONE);
        }
        holder.optionPrice.setText(PriceUtil.optionComma_won(option.getMenu_price()));
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
