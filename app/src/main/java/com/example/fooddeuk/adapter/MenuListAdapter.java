package com.example.fooddeuk.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.CartActivity;
import com.example.fooddeuk.activity.OrderActivity;
import com.example.fooddeuk.http.MenuReviewService;
import com.example.fooddeuk.model.cart.CartItem;
import com.example.fooddeuk.model.cart.CartMenu;
import com.example.fooddeuk.model.cart.CartOption;
import com.example.fooddeuk.model.menu.MenuContentItem;
import com.example.fooddeuk.model.menu.OptionItem;
import com.example.fooddeuk.model.menu.ReviewItem;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.example.fooddeuk.model.restaurant.RestaurantItemRealm;
import com.example.fooddeuk.util.IntentUtil;
import com.example.fooddeuk.util.LayoutUtil;
import com.example.fooddeuk.util.PriceUtil;
import com.example.fooddeuk.util.RealmUtil;
import com.example.fooddeuk.viewholder.ViewHolderNoPicture;
import com.example.fooddeuk.viewholder.ViewHolderWithPictureDoubleOption;
import com.example.fooddeuk.viewholder.ViewHolderWithPictureNecessary;
import com.example.fooddeuk.viewholder.ViewHolderWithPictureUnNecessary;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by heojuyeong on 2017. 7. 31..
 * MenuList
 */


public class MenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MenuContentItem> items;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnCartCountClickListener onCartCountClickListener;
    private RestaurantItem.Restaurant restaurant;
    private static final int pictureAndNoOption = 0;
    private static final int pictureAndNecessary = 1;
    private static final int pictureAndUnNecessary = 2;
    private static final int pictureAndDoubleOption = 3;
    private static final int noPictureAndNecessary = 4;
    private static final int noPictureAndUnNecessary = 5;
    private static final int noPictureAndDoubleOption= 6;
    private static final int noPictureAndNoOption= 7;
    private int expandedPosition = -1;
    private RecyclerView mRecyclerView;
    private ArrayList<OptionItem.Option> necessaryArrayList = new ArrayList<>();
    private ArrayList<OptionItem.Option> unNecessaryArrayList = new ArrayList<>();
    private BottomSheetDialog[] optionDialogArray = new BottomSheetDialog[2];


    public MenuListAdapter(Context context, ArrayList<MenuContentItem> items, int rest_id, RestaurantItem.Restaurant restaurant) {
        this.items = items;
        this.context = context;
        this.restaurant = restaurant;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder view, float y, RecyclerView recyclerView);
    }

    public interface OnCartCountClickListener {
        void onCartCount(int cartSize);
    }

    private void reviewShow(RecyclerView.ViewHolder view) {
        MenuReviewService.getReview(items.get(view.getAdapterPosition()).menu_id).enqueue(new Callback<ArrayList<ReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewItem>> call, Response<ArrayList<ReviewItem>> response) {
                MaterialDialog reviewDialog = new MaterialDialog.Builder(context).customView(R.layout.dialog_review_listview, true).build();
                View reviewDialogView = reviewDialog.getView();
                RecyclerView reviewListView = reviewDialogView.findViewById(R.id.reviewListView);
                ReviewAdapter reviewAdapter = new ReviewAdapter(context, response.body());
                LayoutUtil.RecyclerViewSetting(context, reviewListView);
                reviewListView.setAdapter(reviewAdapter);
                reviewDialog.show();
            }

            @Override
            public void onFailure(Call<ArrayList<ReviewItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private View.OnClickListener menuExpandLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            LinearLayout linearLayout = v.findViewById(R.id.menu_detail_layout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(layoutParams);
            //레이아웃 접기
            if (expandedPosition >= 0) {
                int prev = expandedPosition;
                notifyItemChanged(prev);
            }
            //레이아웃 펼치기
            expandedPosition = holder.getAdapterPosition();
            notifyItemChanged(expandedPosition);


            //레이아웃 높이 remeasure
            mRecyclerView.requestLayout();
            mRecyclerView.invalidate();
            mRecyclerView.smoothScrollToPosition(holder.getAdapterPosition());


        }
    };


    public void setItemViewClickListener(RecyclerView.ViewHolder vh, String optionFLAG) {
        //EXPAND
        vh.itemView.setOnClickListener(menuExpandLayoutListener);

        //SHOW REVIEW
        vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v -> reviewShow(vh));
        vh.itemView.findViewById(R.id.menu_detail_review_show).setOnClickListener(v -> reviewShow(vh));

        //OPTION
        if (optionFLAG.equals("necessary")) {
            vh.itemView.findViewById(R.id.menu_detail_necessary_option_layout).setOnClickListener(v -> optionDialogArray[0].show());
        } else if (optionFLAG.equals("unnecessary")) {
            vh.itemView.findViewById(R.id.menu_detail_unnecessary_option_layout).setOnClickListener(v -> optionDialogArray[1].show());
        } else {
            vh.itemView.findViewById(R.id.menu_detail_necessary_option_layout).setOnClickListener(v -> optionDialogArray[0].show());
            vh.itemView.findViewById(R.id.menu_detail_unnecessary_option_layout).setOnClickListener(v -> optionDialogArray[1].show());
        }

        //CART
        vh.itemView.findViewById(R.id.menu_detail_cart).setOnClickListener(view -> checkCartAndInsertOrOrder(items.get(vh.getAdapterPosition()), "cart"));

        //ORDER
        vh.itemView.findViewById(R.id.menu_detail_order).setOnClickListener(view -> checkCartAndInsertOrOrder(items.get(vh.getAdapterPosition()), "order"));
    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position).picture.length() > 0) {
            boolean necessary = false;
            boolean unnecessary = false;
            ArrayList<OptionItem> optionCategory = items.get(position).option;
            for (int i = 0; i < optionCategory.size(); i++) {
                if (!optionCategory.get(i).multiple) {
                    necessary = true;
                }
                if (optionCategory.get(i).multiple) {
                    unnecessary = true;
                }
            }

            if (necessary && unnecessary) {
                return pictureAndDoubleOption;
            } else if (necessary && !unnecessary) {
                return pictureAndNecessary;
            } else if (!necessary && unnecessary) {
                return pictureAndUnNecessary;
            }else{
                return pictureAndNoOption;
            }

        } else {
            boolean necessary = false;
            boolean unnecessary = false;
            ArrayList<OptionItem> optionCategory = items.get(position).option;
            for (int i = 0; i < optionCategory.size(); i++) {
                if (!optionCategory.get(i).multiple) {
                    necessary = true;
                }
                if (optionCategory.get(i).multiple) {
                    unnecessary = true;
                }
            }

            if (necessary && unnecessary) {
                return noPictureAndDoubleOption;
            } else if (necessary && !unnecessary) {
                return noPictureAndNecessary;
            } else if (!necessary && unnecessary) {
                return noPictureAndUnNecessary;
            }else{
                return noPictureAndNoOption;
            }
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Picture X
        if (viewType == noPictureAndNoOption) {
            View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_not_have_picture, parent, false);
            ViewHolderNoPicture vh = new ViewHolderNoPicture(parentView);
            vh.itemView.setOnClickListener(menuExpandLayoutListener);
            vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v -> reviewShow(vh));
            vh.itemView.findViewById(R.id.menu_detail_order).setOnClickListener(view -> checkCartAndInsertOrOrder(items.get(vh.getAdapterPosition()), "order"));
            return vh;

        } else if (viewType == pictureAndNecessary) {
            //Picture O
            View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_have_picture_necessary, parent, false);
            ViewHolderWithPictureNecessary vh = new ViewHolderWithPictureNecessary(context, parentView);
            setItemViewClickListener(vh, "necessary");
            return vh;
        } else if (viewType == pictureAndUnNecessary) {
            View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_have_picture_unnecessary, parent, false);
            ViewHolderWithPictureUnNecessary vh = new ViewHolderWithPictureUnNecessary(context, parentView);
            setItemViewClickListener(vh, "unnecessary");
            return vh;
        } else if (viewType == pictureAndDoubleOption) {
            View parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_have_picture, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderWithPictureDoubleOption vh = new ViewHolderWithPictureDoubleOption(context,parentView);
            setItemViewClickListener(vh, "double");
            return vh;
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder tmpHolder, int position) {
        final MenuContentItem menuContentItem = items.get(position);

        if (tmpHolder.getItemViewType() == noPictureAndNoOption) {
            ViewHolderNoPicture holder = (ViewHolderNoPicture) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem, position);

        } else if (tmpHolder.getItemViewType() == pictureAndNecessary) {
            ViewHolderWithPictureNecessary holder = (ViewHolderWithPictureNecessary) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem);
            if (position == expandedPosition) {
                holder.menu_master_layout.setVisibility(View.GONE);
                holder.menu_detail_layout.setVisibility(View.VISIBLE);
                holder.itemView.setClickable(false);
                holder.itemView.setEnabled(false);
                setFirstNecessaryOption(menuContentItem, holder);
                setNecessaryOptionDialog(menuContentItem, holder);
            } else {
                holder.menu_master_layout.setVisibility(View.VISIBLE);
                holder.menu_detail_layout.setVisibility(View.GONE);
            }

        } else if (tmpHolder.getItemViewType() == pictureAndUnNecessary) {

            ViewHolderWithPictureUnNecessary holder = (ViewHolderWithPictureUnNecessary) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem);
            if (position == expandedPosition) {
                holder.menu_master_layout.setVisibility(View.GONE);
                holder.menu_detail_layout.setVisibility(View.VISIBLE);
                holder.itemView.setClickable(false);
                holder.itemView.setEnabled(false);
                setUnNecessaryOptionDialog(menuContentItem.option, holder);
            } else {
                holder.menu_master_layout.setVisibility(View.VISIBLE);
                holder.menu_detail_layout.setVisibility(View.GONE);
            }

        } else if (tmpHolder.getItemViewType() == pictureAndDoubleOption) {

            ViewHolderWithPictureDoubleOption holder = (ViewHolderWithPictureDoubleOption) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem, position);
            if (position == expandedPosition) {
                holder.menu_master_layout.setVisibility(View.GONE);
                holder.menu_detail_layout.setVisibility(View.VISIBLE);
                holder.itemView.setClickable(false);
                holder.itemView.setEnabled(false);
                setFirstNecessaryOption(menuContentItem, holder);
                setDoubleOptionDialog(menuContentItem.option,holder);
            } else {
                holder.menu_master_layout.setVisibility(View.VISIBLE);
                holder.menu_detail_layout.setVisibility(View.GONE);
            }


        }
    }

    void addSelectOptionToArray() {

        RecyclerView optionCategoryList = optionDialogArray[0].findViewById(R.id.dialog_menu_option_listview);
        for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
            View categoryView = optionCategoryList.getChildAt(i);
            LinearLayout optionContentList = categoryView.findViewById(R.id.dialog_test);
            for (int j = 0; j < optionContentList.getChildCount(); j++) {
                View optionView = optionContentList.getChildAt(j);
                if (optionView.findViewById(R.id.option_radio) != null) {
                    RadioButton radioOption = optionView.findViewById(R.id.option_radio);
                    if (radioOption.isChecked()) {
                        OptionItem.Option optionContent = (OptionItem.Option) radioOption.getTag();
                        necessaryArrayList.add(optionContent);
                    }
                }
            }
        }


        optionCategoryList = optionDialogArray[1].findViewById(R.id.dialog_menu_option_listview);
        for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
            View categoryView = optionCategoryList.getChildAt(i);
            LinearLayout optionContentList = categoryView.findViewById(R.id.dialog_test);
            for (int j = 0; j < optionContentList.getChildCount(); j++) {
                View optionView = optionContentList.getChildAt(j);
                if (optionView.findViewById(R.id.option_checkbox) != null) {
                    CheckBox checkOption = optionView.findViewById(R.id.option_checkbox);
                    if (checkOption.isChecked()) {
                        OptionItem.Option optionContent = (OptionItem.Option) checkOption.getTag();
                        unNecessaryArrayList.add(optionContent);
                    }
                }
            }
        }
    }

    void setDoubleOptionDialog(ArrayList<OptionItem> optionCategory, ViewHolderWithPictureDoubleOption viewHolder) {
        BottomSheetDialog necessaryOptionDialog = new BottomSheetDialog(context);
        BottomSheetDialog unNecessaryOptionDialog = new BottomSheetDialog(context);
        View necessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
        necessaryOptionDialog.setContentView(necessaryOptionDialogView);
        View unNecessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
        unNecessaryOptionDialog.setContentView(unNecessaryOptionDialogView);


        Button necessaryOptionConfirmButton = necessaryOptionDialogView.findViewById(R.id.optionConfirmButton);
        Button unNecessaryOptionConfirmButton = unNecessaryOptionDialogView.findViewById(R.id.optionConfirmButton);
        setOptionRecyclerView(necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), viewHolder.menu_detail_order, necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, true);
        setOptionRecyclerView(unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), viewHolder.menu_detail_order, unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, false);
        optionDialogArray[0] = necessaryOptionDialog;
        optionDialogArray[1] = unNecessaryOptionDialog;

        View.OnClickListener clickListener = v -> {
            necessaryArrayList.clear();
            unNecessaryArrayList.clear();

            addSelectOptionToArray();
            if (v == necessaryOptionConfirmButton) {
                viewHolder.menu_detail_option_necessary_content.setText(getSelectOption(necessaryArrayList));
                TextView dialogTotalPrice = optionDialogArray[0].findViewById(R.id.dialog_totalPrice);
                ((TextView) optionDialogArray[0].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                ((TextView) optionDialogArray[1].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                viewHolder.menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");
                optionDialogArray[0].dismiss();
            } else {
                viewHolder.menu_detail_option_unnecessary_content.setText(getSelectOption(unNecessaryArrayList));
                TextView dialogTotalPrice = optionDialogArray[1].findViewById(R.id.dialog_totalPrice);
                ((TextView) optionDialogArray[1].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                ((TextView) optionDialogArray[0].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                viewHolder.menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");
                optionDialogArray[1].dismiss();
            }
        };
        necessaryOptionConfirmButton.setOnClickListener(clickListener);
        unNecessaryOptionConfirmButton.setOnClickListener(clickListener);

    }

    void setNecessaryOptionDialog(MenuContentItem menuContentItem, ViewHolderWithPictureNecessary viewHolder) {
        ArrayList<OptionItem> optionCategory = menuContentItem.option;
        BottomSheetDialog necessaryOptionDialog = new BottomSheetDialog(context);
        View necessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
        necessaryOptionDialog.setContentView(necessaryOptionDialogView);

        Button necessaryOptionConfirmButton = necessaryOptionDialogView.findViewById(R.id.optionConfirmButton);

        View.OnClickListener clickListener = v -> {

            necessaryArrayList.clear();
            TextView dialogTotalPrice;
            RecyclerView optionCategoryList;
            dialogTotalPrice = necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice);
            optionCategoryList = necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview);


            for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
                View categoryView = optionCategoryList.getChildAt(i);

                //CategoryName
                LinearLayout optionContentList = categoryView.findViewById(R.id.dialog_test);

                for (int j = 0; j < optionContentList.getChildCount(); j++) {

                    View optionView = optionContentList.getChildAt(j);
                    if (optionView.findViewById(R.id.option_radio) != null) {
                        RadioButton radioOption = optionView.findViewById(R.id.option_radio);
                        if (radioOption.isChecked()) {
                            OptionItem.Option optionContent = (OptionItem.Option) radioOption.getTag();
                            necessaryArrayList.add(optionContent);
                        }
                    }
                }
            }
            viewHolder.menu_detail_option_necessary_content.setText(getSelectOption(necessaryArrayList));
            ((TextView) optionDialogArray[0].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
            viewHolder.menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");
            optionDialogArray[0].dismiss();

        };
        necessaryOptionConfirmButton.setOnClickListener(clickListener);
        setOptionRecyclerView(necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), viewHolder.menu_detail_order, necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, true);
        optionDialogArray[0] = necessaryOptionDialog;
        optionDialogArray[1] = null;

    }

    /**
     * 필수 항목의 가장 첫번째 옵션(기본옵션) 가져와 뷰에 뿌려주고 저장
     * param1 : Menu
     **/

    void setFirstNecessaryOption(MenuContentItem menuContentItem, RecyclerView.ViewHolder viewHolder) {
        necessaryArrayList.clear();
        ArrayList<OptionItem> optionCategoryArray = menuContentItem.option;

        StringBuffer necessaryCategoryText = new StringBuffer("");
        StringBuffer necessaryOptionText = new StringBuffer("");
        TextView title=viewHolder.itemView.findViewById(R.id.menu_detail_option_necessary_title);
        TextView content=viewHolder.itemView.findViewById(R.id.menu_detail_option_necessary_content);
        if (menuContentItem.option.size() == 1) {
            OptionItem.Option necessaryBasicOption = optionCategoryArray.get(0).necessary.get(0);
            necessaryArrayList.add(necessaryBasicOption);
            Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");
            title.setText(optionCategoryArray.get(0).menu_option_category_name);
            content.setText(necessaryBasicOption.menu_option_name);

        } else if (menuContentItem.option.size() > 1) {
            for (int i = 0; i < optionCategoryArray.size(); i++) {
                OptionItem categoryOption = optionCategoryArray.get(i);
                if(categoryOption.necessary.size()>0){
                    OptionItem.Option necessaryBasicOption=categoryOption.necessary.get(0);
                    if(i==0){
                        necessaryCategoryText.append(categoryOption.menu_option_category_name);
                        necessaryOptionText.append(necessaryBasicOption.menu_option_name);
                    }else if(i != optionCategoryArray.size() - 1){
                        necessaryCategoryText.append(categoryOption.menu_option_category_name + ",");
                        necessaryOptionText.append(necessaryBasicOption.menu_option_name + ",");
                    }else {
                        necessaryCategoryText.append(categoryOption.menu_option_category_name);
                        necessaryOptionText.append(necessaryBasicOption.menu_option_name);
                    }
                    necessaryArrayList.add(necessaryBasicOption);
                    Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");
                }


            }

            title.setText(necessaryCategoryText);
            content.setText(necessaryOptionText);
        }

    }

    void setUnNecessaryOptionDialog(ArrayList<OptionItem> optionCategory, ViewHolderWithPictureUnNecessary holder) {
        BottomSheetDialog unNecessaryOptionDialog = new BottomSheetDialog(context);
        View unNecessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
        unNecessaryOptionDialog.setContentView(unNecessaryOptionDialogView);

        //확인버튼
        Button unNecessaryOptionConfirmButton = unNecessaryOptionDialogView.findViewById(R.id.optionConfirmButton);
        View.OnClickListener clickListener = v -> {
            unNecessaryArrayList.clear();
            TextView dialogTotalPrice = unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice);
            RecyclerView optionCategoryList = unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview);


            for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
                View categoryView = optionCategoryList.getChildAt(i);
                LinearLayout optionContentList = categoryView.findViewById(R.id.dialog_test);
                for (int j = 0; j < optionContentList.getChildCount(); j++) {
                    View optionView = optionContentList.getChildAt(j);
                    if (optionView.findViewById(R.id.option_checkbox) != null) {
                        CheckBox checkOption = optionView.findViewById(R.id.option_checkbox);
                        if (checkOption.isChecked()) {
                            OptionItem.Option optionContent = (OptionItem.Option) checkOption.getTag();
                            unNecessaryArrayList.add(optionContent);
                        }
                    }
                }
            }

            holder.menu_detail_option_unnecessary_content.setText(getSelectOption(unNecessaryArrayList));
            optionDialogArray[1].dismiss();
            ((TextView) optionDialogArray[1].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
            holder.menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");
        };

        unNecessaryOptionConfirmButton.setOnClickListener(clickListener);
        setOptionRecyclerView(unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), holder.menu_detail_order, unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, false);
        optionDialogArray[0] = null;
        optionDialogArray[1] = unNecessaryOptionDialog;

    }

    //장바구니에 아이템이 있는지 없는지 체크
    private void checkCartAndInsertOrOrder(MenuContentItem menuContentItem, String version) {
        RealmResults<CartItem> cartItem = RealmUtil.findDataAll(CartItem.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("isDirect",true);

        //기존 장바구니가 없으면 바로 추가
        if (cartItem.size() == 0) {
            RestaurantItemRealm newRestaurant = new RestaurantItemRealm(restaurant.rest_id, restaurant.rest_admin_id, restaurant.name, restaurant.address, restaurant.open_time, restaurant.close_time, restaurant.avg_cooking_time, restaurant.latlng[1], restaurant.latlng[0]);
            RealmUtil.insertData(newRestaurant);
            RealmUtil.insertData2(createCartMenuItem(menuContentItem));
            if (version.equals("cart")) {
                IntentUtil.startActivity(context, CartActivity.class);
            }else{
                //Direct Order
                IntentUtil.startActivity(context, OrderActivity.class,bundle);
            }



        } else if (cartItem.size() > 0) {
            //장바구니에 이미 메뉴가 존재하는 경우
            RealmResults<RestaurantItemRealm> defaultRestaurant = RealmUtil.findDataAll(RestaurantItemRealm.class);
            //기존 장바구니에 있는 식당과 현재 주문할려는 식당이 일치할 경우 장바구니에 메뉴를 추가한다
            if (defaultRestaurant.get(0).rest_id == restaurant.rest_id) {
                RealmUtil.insertData2(createCartMenuItem(menuContentItem));
                if (version.equals("cart")) {
                    IntentUtil.startActivity(context, CartActivity.class);
                }else{
                    //Direct Order
                    IntentUtil.startActivity(context, CartActivity.class);
                }
//                onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));

            }
            //일치하지 않을 경우 기존 장바구니를 삭제하고 새로운 식당의 메뉴를 추가할것인지 묻는다
            else {
                new MaterialDialog.Builder(context)
                        .content(context.getString(R.string.cart_delete_check))
                        .positiveText("예")
                        .negativeText("아니요")
                        .onPositive((dialog, which) -> {
                            RealmUtil.removeDataAll(CartItem.class);
                            RealmUtil.removeDataAll(RestaurantItemRealm.class);
                            RestaurantItemRealm newRestaurant = new RestaurantItemRealm(restaurant.rest_id, restaurant.rest_admin_id, restaurant.name, restaurant.address, restaurant.open_time, restaurant.close_time, restaurant.avg_cooking_time, restaurant.latlng[1], restaurant.latlng[0]);
                            RealmUtil.insertData(newRestaurant);
//                            RealmUtil.insertData(createCartMenuItem(menuContentItem, optionDialogArray));
                            if (version.equals("cart")) {
                                IntentUtil.startActivity(context, CartActivity.class);
                            }else{
                                IntentUtil.startActivity(context, OrderActivity.class,bundle);
                            }
//                            onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));


                        })
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        }
    }

    //필수, 선택 옵션 구분한뒤 localdb 변환
    private RealmList<CartOption> getMenuOption() {
        RealmList<CartOption> menuOption = new RealmList<>();
        //필수값 먼저 삽입한다
        if (necessaryArrayList.size() >= 1) {
            for (int i = 0; i < necessaryArrayList.size(); i++) {
                OptionItem.Option optionItem = necessaryArrayList.get(i);
                CartOption option = new CartOption(optionItem.menu_option_id, optionItem.menu_option_name, optionItem.menu_option_price);
                menuOption.add(option);
            }
        }
        if (unNecessaryArrayList.size() >= 1) {
            Logger.d("eee");
            for (int i = 0; i < unNecessaryArrayList.size(); i++) {
                OptionItem.Option optionItem = unNecessaryArrayList.get(i);

                CartOption option = new CartOption(optionItem.menu_option_id, optionItem.menu_option_name, optionItem.menu_option_price);
                menuOption.add(option);
            }
        }


        return menuOption;
    }


    //선탠된 메뉴 CartItem RealmObject 생성
    private CartItem createCartMenuItem(MenuContentItem menuContentItem) {
        CartMenu menu = new CartMenu(menuContentItem.getMenu_id(), menuContentItem.getName(), menuContentItem.price, menuContentItem.getAvgtime());
        CartItem cartItem;

        if (necessaryArrayList.size() > 0 || unNecessaryArrayList.size() > 0) {
            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu, getMenuOption());
        } else {
            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu);
        }

        return cartItem;
    }





    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCartCountClickListener(OnCartCountClickListener onCartAddClickListenr) {
        this.onCartCountClickListener = onCartAddClickListenr;
    }

    void setOptionRecyclerView(RecyclerView optionCategoryList, Button menu_detail_order, TextView dialog_totalPrice, ArrayList<OptionItem> optionCategory, boolean necessary) {
        LayoutUtil.RecyclerViewSetting(context, optionCategoryList);
        dialog_totalPrice.setText(menu_detail_order.getText().subSequence(0, menu_detail_order.getText().length() - 6));


        OptionCategoryAdapter.SelectCLickListener selectCLickListener = (isPlus, price) -> {
            dialog_totalPrice.setText((isPlus.equals("plus")) ?
                    PriceUtil.plusPrice(price, PriceUtil.TextViewToString(dialog_totalPrice)) :
                    PriceUtil.minusPrice(price, PriceUtil.TextViewToString(dialog_totalPrice)));
        };

        OptionCategoryAdapter.RadioPriceListener radioPriceListener = (plusPrice, minusPrice) -> {
            if (minusPrice == null) {
                dialog_totalPrice.setText(PriceUtil.plusPrice(PriceUtil.TextViewToString(dialog_totalPrice), PriceUtil.TextViewToString(plusPrice)));
                return;
            }
            int price = PriceUtil.getOriginalPrice(plusPrice) - PriceUtil.getOriginalPrice(minusPrice) + PriceUtil.getOriginalPrice(dialog_totalPrice.getText().toString());
            dialog_totalPrice.setText(PriceUtil.comma_won(price));
        };

        //선택
        if (!necessary) {
            ArrayList<OptionItem> unNecessaryOptionList = new ArrayList<>();
            for (int i = 0; i < optionCategory.size(); i++) {
                if (optionCategory.get(i).multiple) {
                    unNecessaryOptionList.add(optionCategory.get(i));
                }
            }
            optionCategoryList.setAdapter(new OptionCategoryAdapter(unNecessaryOptionList, context, selectCLickListener, radioPriceListener));
        }
        //필수
        else {
            ArrayList<OptionItem> necessaryOptionList = new ArrayList<>();
            for (int i = 0; i < optionCategory.size(); i++) {
                if (!optionCategory.get(i).multiple) {
                    necessaryOptionList.add(optionCategory.get(i));
                }
            }
            optionCategoryList.setAdapter(new OptionCategoryAdapter(necessaryOptionList, context, selectCLickListener, radioPriceListener));
        }
    }

    String getSelectOption(ArrayList<OptionItem.Option> optionArrayList) {
        if (optionArrayList.size() == 1) {
            return optionArrayList.get(0).menu_option_name;
        } else if (optionArrayList.size() > 1) {
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < optionArrayList.size(); i++) {
                if (i == optionArrayList.size() - 1) {
                    s.append(optionArrayList.get(i).menu_option_name);
                    break;
                }
                s.append(optionArrayList.get(i).menu_option_name + ",");

            }
            return s.toString();
        }
        return null;
    }





}



