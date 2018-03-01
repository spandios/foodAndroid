package com.example.fooddeuk.menu.option;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.model.cart.CartOption;
import com.example.fooddeuk.model.cart.CartOptionCategory;
import com.example.fooddeuk.model.menu.Option;
import com.example.fooddeuk.util.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heo on 2018. 1. 5..
 */

public class OptionCategoryAdapter extends RecyclerView.Adapter<OptionCategoryAdapter.ViewHolder> {
    private ArrayList<CartOptionCategory> optionCategory;
    private Context context;
    private RadioPriceListener radioPricelistener;
    private SelectCLickListener selectCLickListener;
    private List<RadioButton> radioButtons=new ArrayList<>();



    public OptionCategoryAdapter(Context context, ArrayList<CartOptionCategory> optionCategory, OptionCategoryAdapter.SelectCLickListener selectCLickListener, RadioPriceListener radioPriceListener) {
        this.optionCategory = optionCategory;
        this.context = context;
        this.radioPricelistener=radioPriceListener;
        this.selectCLickListener=selectCLickListener;

    }

    public interface  RadioPriceListener{
        void getRadioPrice(String plusPrice,String minusPrice);
    }

    public interface SelectCLickListener {
        void onClickCheck(String isPlus, String price);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_menu_option_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        OptionCategoryAdapter.ViewHolder vh = new OptionCategoryAdapter.ViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CartOptionCategory optionCategory = this.optionCategory.get(position);
        holder.dialog_option_category_name.setText(optionCategory.menu_option_category_name);

                //필수  -> RADIO
              if(!optionCategory.multiple){
                for(int i = 0; i< optionCategory.necessary.size(); i++){
                    LinearLayout.LayoutParams radioButtonLayOutParam=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,8);
                    radioButtonLayOutParam.setMargins(LayoutUtil.convertDpToPixelInt(12,context),LayoutUtil.convertDpToPixelInt(20,context),0,0);

                    RadioButton optionRadio=new RadioButton(context);
                    if(i==0){
                        optionRadio.setChecked(true);
                    }
                    optionRadio.setLayoutParams(radioButtonLayOutParam);
                    optionRadio.setTextSize(20);
                    optionRadio.setTag(optionCategory.necessary.get(i));
                    optionRadio.setId(R.id.option_radio);
                    optionRadio.setText(optionCategory.necessary.get(i).menu_option_name);
                    radioButtons.add(optionRadio);
                    LinearLayout linearLayout=new LinearLayout(context);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.addView(optionRadio);
                    LinearLayout.LayoutParams layoutParams2=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2);
                    layoutParams2.setMargins(0,LayoutUtil.convertDpToPixelInt(20,context),LayoutUtil.convertDpToPixelInt(12,context),0);
                    TextView optionPrice=new TextView(context);
                    optionPrice.setGravity(Gravity.RIGHT);
                    optionPrice.setTextSize(20);
                    optionPrice.setText("+"+ optionCategory.necessary.get(i).menu_option_price+"원");
                    optionPrice.setLayoutParams(layoutParams2);
                    linearLayout.addView(optionPrice);
                    holder.dialog_test.addView(linearLayout);
                }

                //Mutilple true == 선택옵션
            }else{
                  for(int i = 0; i< optionCategory.unnecessary.size(); i++){
                      CartOption unNecessaryOption= optionCategory.unnecessary.get(i);
                      LinearLayout.LayoutParams radioButtonLayOutParam=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,8);
                      radioButtonLayOutParam.setMargins(LayoutUtil.convertDpToPixelInt(12,context),LayoutUtil.convertDpToPixelInt(20,context),0,0);
                      CheckBox checkBox=new CheckBox(context);
                      checkBox.setLayoutParams(radioButtonLayOutParam);
                      checkBox.setTextSize(20);
                      checkBox.setTag(unNecessaryOption);
                      checkBox.setId(R.id.option_checkbox);
                      checkBox.setText(unNecessaryOption.menu_option_name);
                      checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                          @Override
                          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                              if(b){
                                  selectCLickListener.onClickCheck("plus",((Option) compoundButton.getTag()).menu_option_price);
                              }else{
                                  selectCLickListener.onClickCheck("minus",((Option) compoundButton.getTag()).menu_option_price);
                              }
                          }
                      });
                      LinearLayout linearLayout=new LinearLayout(context);
                      linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                      linearLayout.addView(checkBox);
                      LinearLayout.LayoutParams layoutParams2=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2);
                      layoutParams2.setMargins(0,LayoutUtil.convertDpToPixelInt(20,context),LayoutUtil.convertDpToPixelInt(12,context),0);
                      TextView optionPrice=new TextView(context);
                      optionPrice.setGravity(Gravity.RIGHT);
                      optionPrice.setTextSize(20);
                      optionPrice.setText("+"+ unNecessaryOption.menu_option_price+"원");
                      optionPrice.setLayoutParams(layoutParams2);

                      linearLayout.addView(optionPrice);
                      holder.dialog_test.addView(linearLayout);
                  }

            }
        //선택옵션


        for (RadioButton button : radioButtons){
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) processRadioButtonClick(buttonView);
                }
            });

        }


    }
    private void processRadioButtonClick(CompoundButton buttonView){
        String plusPrice=new String();
        String minPrice=new String();
        for (RadioButton button : radioButtons){
            if (button != buttonView ) {
                minPrice=((Option)button.getTag()).menu_option_price;
                button.setChecked(false);
            }
            if(button==buttonView){
                plusPrice=((Option)button.getTag()).menu_option_price;
            }
        }

        radioPricelistener.getRadioPrice(plusPrice,minPrice);

    }
    @Override
    public int getItemCount() {
        return optionCategory.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dialog_option_category_name)
        TextView dialog_option_category_name;
        @BindView(R.id.option_content_list)
        LinearLayout dialog_test;



        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
