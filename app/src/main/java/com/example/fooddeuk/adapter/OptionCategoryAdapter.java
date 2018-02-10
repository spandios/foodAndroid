package com.example.fooddeuk.adapter;

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
import com.example.fooddeuk.model.menu.Option;
import com.example.fooddeuk.model.menu.OptionCategory;
import com.example.fooddeuk.util.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heo on 2018. 1. 5..
 */

public class OptionCategoryAdapter extends RecyclerView.Adapter<OptionCategoryAdapter.ViewHolder> {
    private ArrayList<OptionCategory> optionCategory;
    private Context context;
    private RadioPriceListener radioPricelistener;
    private SelectCLickListener selectCLickListener;
    private List<RadioButton> radioButtons=new ArrayList<>();



    public OptionCategoryAdapter(ArrayList<OptionCategory> optionCategory, Context context, OptionCategoryAdapter.SelectCLickListener selectCLickListener, RadioPriceListener radioPriceListener) {
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

//        radioClickListener = (position,plusPrice) -> {
//
//            //클릭 되어있는 라디오만 해제한뒤
//            for (int childCount = vh.dialog_test.getChildCount(), j = 0; j < childCount; j++) {
//                RadioButton radioButton = (RadioButton) vh.dialog_test.getChildAt(j);
//                if(childCount==0) {
//                    radioPricelistener.getRadioPrice(plusPrice,null);
//                    return;
//                }
//
//                if(j==position){
//                    continue;
//                }
//
//                //체크 되어 있을 경우
//                if (radioButton.isChecked()) {
//                    //해제
//                    radioButton.setChecked(false);
//                    TextView minPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
//                    radioPricelistener.getRadioPrice(plusPrice,minPrice);
//                    //최종값에 가격 마이너스 함
////                    dialog_totalPrice.setText(PriceUtil.minusPrice(PriceUtil.TextViewToString(dialog_totalPrice), PriceUtil.TextViewToString(optionPrice)));
//                }
//
//            }
//
//        };
        return vh;
    }
//    RelativeLayout optionContentLayout=new RelativeLayout(context);
//    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(0, LayoutUtil.convertDpToPixelInt(14,context),0,0);
//                    optionContentLayout.setLayoutParams(layoutParams);
//
//
//    RadioButton radioButton=new RadioButton(context);
//    RelativeLayout.LayoutParams layoutParams1=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams1.setMargins(LayoutUtil.convertDpToPixelInt(15,context),LayoutUtil.convertDpToPixelInt(15,context),0,0);
//
//                    radioButton.setLayoutParams(layoutParams1);
//                    radioButton.setText(optionItem.necessary.get(i).menu_option_name);
//                    radioButton.setTextSize(20);
//                    radioButton.setTag(optionItem.necessary.get(i));
//
//    RelativeLayout.LayoutParams layoutParams2=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams2.setMargins(0,LayoutUtil.convertDpToPixelInt(20,context),LayoutUtil.convertDpToPixelInt(15,context),0);
//                    layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//    TextView optionPrice=new TextView(context);
//                    optionPrice.setLayoutParams(layoutParams2);
//                    optionPrice.setTextSize(20);
//                    optionPrice.setText(optionItem.necessary.get(i).menu_option_price);
//
//                    optionContentLayout.addView(radioButton);
//                    optionContentLayout.addView(optionPrice);
//                    optionContentLayout.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Logger.d("ee");
//        }
//    });


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OptionCategory optionCategory = this.optionCategory.get(position);
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
                      Option unNecessaryOption= optionCategory.unnecessary.get(i);
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
        @BindView(R.id.dialog_test)
        LinearLayout dialog_test;



        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
