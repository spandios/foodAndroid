package com.example.fooddeuk.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.CartActivity;
import com.example.fooddeuk.http.MenuReviewService;
import com.example.fooddeuk.http.RestaurantService;
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
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
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
    private static final int noPicture = 0;
    private static final int pictureAndNecessary = 1;
    private static final int pictureAndUnNecessary = 2;
    private static final int pictureAndDoubleOption = 3;
    private int expandedPosition = -1;
    private RecyclerView mRecyclerView;
    private int rest_id;
    private CartMenu menu = new CartMenu();
    private ArrayList<CartOption> option = new ArrayList<>();
    private ArrayList<OptionItem.Option> optionArrayList = new ArrayList<>();
    private ArrayList<OptionItem.Option> necessaryArrayList=new ArrayList<>();
    private ArrayList<OptionItem.Option> unNecessaryArrayList=new ArrayList<>();
    private BottomSheetDialog[] optionDialogArray = new BottomSheetDialog[2];


    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder view, float y, RecyclerView recyclerView);
    }

    public interface OnCartCountClickListener {
        void onCartCount(int cartSize);
    }


    //Review
    static private void reviewDialogShow(Context context, ArrayList<ReviewItem> reviewItem) {
        MaterialDialog reviewDialog = new MaterialDialog.Builder(context).customView(R.layout.dialog_review_listview, true).build();
        View reviewDialogView = reviewDialog.getView();
        RecyclerView reviewListView = (RecyclerView) reviewDialogView.findViewById(R.id.reviewListView);
        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewItem);
        LayoutUtil.RecyclerViewSetting(context, reviewListView);
        reviewListView.setAdapter(reviewAdapter);
        reviewDialog.show();
    }

    private void reviewClick(RecyclerView.ViewHolder view) {
        MenuReviewService.getReview(items.get(view.getAdapterPosition()).menu_id).enqueue(new Callback<ArrayList<ReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewItem>> call, Response<ArrayList<ReviewItem>> response) {
                reviewDialogShow(context, response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ReviewItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

//    private View.OnClickListener reviewCLickListener= v -> reviewClick();


    private View.OnClickListener menuExpandLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.menu_detail_layout);


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


//            onItemClickListener.onItemClick(holder,LayoutUtil.convertPixelsToDp(mRecyclerView.getChildAt(holder.getAdapterPosition()).findViewById(R.id.menu_detail_layout).getTop(),context));


//            Logger.d(originalPos[1]);
//            mRecyclerView.getChildAt(holder.getAdapterPosition()).findViewById(R.id.menu_detail_layout).getLocationOnScreen(originalPos);

//            onItemClickListener.onItemClick(holder,mRecyclerView.getChildAt(holder.getAdapterPosition()).findViewById(R.id.menu_detail_layout).getY(),mRecyclerView);

        }
    };


    public MenuListAdapter(Context context, ArrayList<MenuContentItem> items, int rest_id) {
        this.items = items;
        this.context = context;
        this.rest_id = rest_id;
        getRestaurant();


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Picture X
        if (viewType == noPicture) {
            View parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_not_have_picture, parent, false);

            // set the view's size, margins, paddings and layout parameters
            ViewHolderNoPicture vh = new ViewHolderNoPicture(parentView);

            vh.itemView.setOnClickListener(menuExpandLayoutListener);

            vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v -> reviewClick(vh));


//            reviewClick(vh.itemView.findViewById(R.id.menu_detail_rating),items.get(vh.getAdapterPosition()).menu_id);

            return vh;

        } else if (viewType == pictureAndNecessary) {
            //Picture O
            View parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_have_picture_necessary, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderWithPictureNecessary vh = new ViewHolderWithPictureNecessary(parentView);
            vh.itemView.setOnClickListener(menuExpandLayoutListener);
            /*Show Review Dialog*/
            vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v -> reviewClick(vh));
            vh.itemView.findViewById(R.id.menu_detail_review_show).setOnClickListener(v -> reviewClick(vh));
//            reviewClick(vh.itemView.findViewById(R.id.menu_detail_rating),items.get(vh.getAdapterPosition()).menu_id);

            return vh;
        } else if (viewType == pictureAndUnNecessary) {
            View parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_have_picture_unnecessary, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderWithPictureUnNecessary vh = new ViewHolderWithPictureUnNecessary(parentView);
            vh.itemView.setOnClickListener(menuExpandLayoutListener);

            /*Show Review Dialog*/
            vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v -> reviewClick(vh));
            vh.itemView.findViewById(R.id.menu_detail_review_show).setOnClickListener(v -> reviewClick(vh));
//            reviewClick(vh.itemView.findViewById(R.id.menu_detail_rating),items.get(vh.getAdapterPosition()).menu_id);
            return vh;
        } else if (viewType == pictureAndDoubleOption) {
            View parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_have_picture, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderWithPictureDoubleOption vh = new ViewHolderWithPictureDoubleOption(parentView);
            vh.itemView.setOnClickListener(menuExpandLayoutListener);

            /*Show Review Dialog*/
            vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v -> reviewClick(vh));
            vh.itemView.findViewById(R.id.menu_detail_review_show).setOnClickListener(v -> reviewClick(vh));
//            reviewClick(vh.itemView.findViewById(R.id.menu_detail_rating),items.get(vh.getAdapterPosition()).menu_id);
            return vh;
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder tmpHolder, int position) {
        final MenuContentItem menuContentItem = items.get(position);

        if (tmpHolder.getItemViewType() == noPicture) {
            ViewHolderNoPicture holder = (ViewHolderNoPicture) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem, position);


        } else if (tmpHolder.getItemViewType() == pictureAndNecessary) {
            ViewHolderWithPictureNecessary holder = (ViewHolderWithPictureNecessary) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem, position);

        } else if (tmpHolder.getItemViewType() == pictureAndUnNecessary) {

            ViewHolderWithPictureUnNecessary holder = (ViewHolderWithPictureUnNecessary) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem, position);

        } else if (tmpHolder.getItemViewType() == pictureAndDoubleOption) {

            ViewHolderWithPictureDoubleOption holder = (ViewHolderWithPictureDoubleOption) tmpHolder;
            holder.itemView.setTag(holder);
            holder.bind(menuContentItem, position);


        }
    }


    //식당정보
    private void getRestaurant() {
        RestaurantService.getRestaurantById(rest_id).enqueue(new Callback<RestaurantItem>() {
            @Override
            public void onResponse(Call<RestaurantItem> call, Response<RestaurantItem> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        restaurant = response.body().getRestaurants().get(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<RestaurantItem> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    //메뉴옵션 dialog



    //장바구니에 아이템이 있는지 없는지 체크

    private void checkCartAndInsertOrOrder(MenuContentItem menuContentItem, String version) {

        RealmResults<CartItem> cartItem = RealmUtil.findDataAll(CartItem.class);


        //기존 장바구니가 없으면 바로 추가
        if (cartItem.size() == 0) {
            RestaurantItemRealm newRestaurant = new RestaurantItemRealm(restaurant.rest_id, restaurant.rest_admin_id, restaurant.name, restaurant.address, restaurant.open_time, restaurant.close_time, restaurant.avg_cooking_time, restaurant.latlng[1], restaurant.latlng[0]);
            RealmUtil.insertData(newRestaurant);
            RealmUtil.insertData2(createCartMenuItem(menuContentItem));
            if (version.equals("order")) {
                IntentUtil.startActivity(context, CartActivity.class);
            }
//            onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));


        } else if (cartItem.size() > 0) {
            //장바구니에 이미 메뉴가 존재하는 경우
            RealmResults<RestaurantItemRealm> defaultRestaurant = RealmUtil.findDataAll(RestaurantItemRealm.class);
            //기존 장바구니에 있는 식당과 현재 주문할려는 식당이 일치할 경우 장바구니에 메뉴를 추가한다
            if (defaultRestaurant.get(0).rest_id == restaurant.rest_id) {
                RealmUtil.insertData2(createCartMenuItem(menuContentItem));
                if (version.equals("order")) {
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
                            if (version.equals("order")) {
                                IntentUtil.startActivity(context, CartActivity.class);
                            }
//                            onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));


                        })
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        }
//        onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));
//        optionDialog.dismiss();
    }

//    private void setOptionRecyclerView(RecyclerView optionRecyclerView, ArrayList<OptionItem.Option> optionItem, boolean multiple, TextView dialog_totalPrice) {
//
//        //여러개 선택 가능한 옵션들의 클릭 콜백함수
//        OptionContentAdapter.SelectCLickListener selectCLickListener = (isPlus, position) -> {
//            RecyclerView.ViewHolder viewHolder = optionRecyclerView.getChildViewHolder(optionRecyclerView.getChildAt(position));
//            TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
//            dialog_totalPrice.setText((isPlus.equals("plus")) ?
//                    PriceUtil.plusPrice(PriceUtil.TextViewToString(optionPrice), PriceUtil.TextViewToString(dialog_totalPrice)) :
//                    PriceUtil.minusPrice(PriceUtil.TextViewToString(optionPrice), PriceUtil.TextViewToString(dialog_totalPrice)));
//        };
//
//        //한개만 선택 가능한 옵션들의 클릭 콜백함수
//        OptionContentAdapter.RadioClickListener radioClickListener = (position) -> {
//            //클릭 되어있는 라디오만 해제한뒤
//            for (int childCount = optionRecyclerView.getChildCount(), j = 0; j < childCount; j++) {
//                RecyclerView.ViewHolder viewHolder = optionRecyclerView.getChildViewHolder(optionRecyclerView.getChildAt(j));
//                RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
//                //체크 되어 있을 경우
//                if (radioButton.isChecked()) {
//                    //해제
//                    radioButton.setChecked(false);
//                    TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
//                    //최종값에 가격 마이너스 함
//                    dialog_totalPrice.setText(PriceUtil.minusPrice(PriceUtil.TextViewToString(dialog_totalPrice), PriceUtil.TextViewToString(optionPrice)));
//                }
//            }
//
//            //클릭한 포지션의 라디오 체크하고 최종값에 가격 플러스
//            RecyclerView.ViewHolder viewHolder = optionRecyclerView.getChildViewHolder(optionRecyclerView.getChildAt(position));
//            RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
//            TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
//            radioButton.setChecked(true);
//            dialog_totalPrice.setText(PriceUtil.plusPrice(PriceUtil.TextViewToString(dialog_totalPrice), PriceUtil.TextViewToString(optionPrice)));
//
//        };
//
//        final OptionContentAdapter optionContentAdapter = new OptionContentAdapter(optionItem, context, multiple, radioClickListener, selectCLickListener);
//        LayoutUtil.RecyclerViewSetting(context.getApplicationContext(), optionRecyclerView);
//        optionRecyclerView.setAdapter(optionContentAdapter);
//
//    }

    //필수, 선택 옵션 구분한뒤 localdb 변환
    private RealmList<CartOption> getMenuOption() {

        RealmList<CartOption> menuOption = new RealmList<>();


        //필수값 먼저 삽입한다
        if (necessaryArrayList.size() >= 1) {
            for(int i=0; i<necessaryArrayList.size();i++){
                OptionItem.Option optionItem=necessaryArrayList.get(i);
                CartOption option=new CartOption(optionItem.menu_option_id,optionItem.menu_option_name,optionItem.menu_option_price);
                menuOption.add(option);
            }
        }
        if (unNecessaryArrayList.size() >= 1) {
            for(int i=0; i<unNecessaryArrayList.size();i++){
                OptionItem.Option optionItem=unNecessaryArrayList.get(i);

                CartOption option=new CartOption(optionItem.menu_option_id,optionItem.menu_option_name,optionItem.menu_option_price);
                menuOption.add(option);
            }
        }


        return menuOption;
    }


    //선탠된 메뉴 CartItem RealmObject 생성
    private CartItem createCartMenuItem(MenuContentItem menuContentItem) {
        CartMenu menu = new CartMenu(menuContentItem.getMenu_id(), menuContentItem.getName(), menuContentItem.price, menuContentItem.getAvgtime());
        CartItem cartItem;

        if(necessaryArrayList.size()>0||unNecessaryArrayList.size()>0){
            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu, getMenuOption());
        }else{
            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu);
        }

        return cartItem;
    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position).picture.length() > 0) {
            boolean necessary = false;
            boolean unnecessary = false;
            ArrayList<OptionItem> optionCategory = items.get(position).option;
            for (int i = 0; i < optionCategory.size(); i++) {
                if (optionCategory.get(i).necessary.size() > 0) {
                    necessary = true;
                }
                if (optionCategory.get(i).unnecessary.size() > 0) {
                    unnecessary = true;
                }
            }

            if (necessary && unnecessary) {
                return pictureAndDoubleOption;
            } else if (necessary && !unnecessary) {
                return pictureAndNecessary;
            } else if (!necessary && unnecessary) {
                return pictureAndUnNecessary;
            }

        } else {
            return noPicture;
        }

        return 0;
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


        OptionContentAdapter.SelectCLickListener selectCLickListener = (isPlus, textView) -> {
            dialog_totalPrice.setText((isPlus.equals("plus")) ?
                    PriceUtil.plusPrice(PriceUtil.TextViewToString(textView), PriceUtil.TextViewToString(dialog_totalPrice)) :
                    PriceUtil.minusPrice(PriceUtil.TextViewToString(textView), PriceUtil.TextViewToString(dialog_totalPrice)));
        };

        OptionCategoryAdapter.RadioPriceListener radioPriceListener = (plusPrice, minusPrice) -> {
            Logger.d(plusPrice);
            Logger.d(minusPrice);
            if (minusPrice == null) {
                dialog_totalPrice.setText(PriceUtil.plusPrice(PriceUtil.TextViewToString(dialog_totalPrice), PriceUtil.TextViewToString(plusPrice)));
                return;
            }


            int price = PriceUtil.getOriginalPrice(plusPrice) - PriceUtil.getOriginalPrice(minusPrice);
            int price2 = PriceUtil.getOriginalPrice(dialog_totalPrice.getText().toString()) + price;
            dialog_totalPrice.setText(PriceUtil.comma_won(price2));
        };

        if (!necessary) {
            ArrayList<OptionItem> unNecessaryOptionList = new ArrayList<>();
            for (int i = 0; i < optionCategory.size(); i++) {
                if (optionCategory.get(i).unnecessary.size() > 0) {
                    unNecessaryOptionList.add(optionCategory.get(i));
                }
            }
            optionCategoryList.setAdapter(new OptionCategoryAdapter(unNecessaryOptionList, context, selectCLickListener, radioPriceListener));
        } else {
            ArrayList<OptionItem> necessaryOptionList = new ArrayList<>();
            for (int i = 0; i < optionCategory.size(); i++) {
                if (optionCategory.get(i).necessary.size() > 0) {
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


    class ViewHolderWithPictureUnNecessary extends RecyclerView.ViewHolder {
        //Layout
        @BindView(R.id.menu_master_layout)
        ConstraintLayout menu_master_layout;
        @BindView(R.id.menu_detail_layout)
        LinearLayout menu_detail_layout;

        //Master
        @BindView(R.id.menu_master_picture)
        ImageView menu_master_picture;
        @BindView(R.id.menu_master_name)
        TextView menu_master_name;
        @BindView(R.id.menu_master_price)
        TextView menu_master_price;

        //Detail
        @BindView(R.id.menu_detail_picture)
        ImageView detailHotMenuPicture;
        @BindView(R.id.menu_detail_name)
        TextView menu_detail_name;
        @BindView(R.id.menu_detail_price)
        TextView menu_detail_price;
        @BindView(R.id.menu_detail_description)
        TextView menu_detail_description;
        @BindView(R.id.menu_detail_rating)
        TextView menu_detail_rating;
        @BindView(R.id.menu_detail_rating_bar)
        RatingBar menu_detail_rating_bar;
        @BindView(R.id.menu_detail_review)
        TextView menu_detail_review;
        @BindView(R.id.menu_detail_cart)
        Button menu_detail_cart;
        @BindView(R.id.menu_detail_order)
        Button menu_detail_order;


        @BindView(R.id.menu_detail_unnecessary_option_layout)
        RelativeLayout menu_detail_unnecessary_option_layout;

        @BindView(R.id.menu_detail_option_unnecessary_content)
        TextView menu_detail_option_unnecessary_content;


        @BindView(R.id.menu_detail_total_price)
        TextView menu_detail_total_price;


//        @BindView(R.id.menu_master_basic_price)
//        TextView menu_master_basic_price;

        ViewHolderWithPictureUnNecessary(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void setUnNecessaryOptionDialog(ArrayList<OptionItem> optionCategory) {
            BottomSheetDialog unNecessaryOptionDialog = new BottomSheetDialog(context);
            View unNecessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
            unNecessaryOptionDialog.setContentView(unNecessaryOptionDialogView);

            Button unNecessaryOptionConfirmButton = (Button) unNecessaryOptionDialogView.findViewById(R.id.optionConfirmButton);

            View.OnClickListener clickListener = v -> {
                unNecessaryArrayList.clear();
                TextView dialogTotalPrice;
                RecyclerView optionCategoryList;
                dialogTotalPrice = (TextView) unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice);
                optionCategoryList = (RecyclerView) unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview);


//                for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
//                    View categoryView = optionCategoryList.getChildAt(i);
//                    RecyclerView optionList = (RecyclerView) categoryView.findViewById(R.id.dialog_menu_option_content_listview);
//                    for (int j = 0; j < optionList.getChildCount(); j++) {
//                        View optionView = optionList.getChildAt(j);
//                        RadioButton radioOption = (RadioButton) optionView.findViewById(R.id.dialog_menu_option_radio);
//                        CheckBox checkOption = (CheckBox) optionView.findViewById(R.id.dialog_menu_option_checkbox);
//
//                        if (radioOption.getVisibility() == View.VISIBLE && radioOption.isChecked()) {
//                            OptionItem.Option optionContent = (OptionItem.Option) radioOption.getTag();
//                            unNecessaryArrayList.add(optionContent);
//
//                        } else if (checkOption.getVisibility() == View.VISIBLE && checkOption.isChecked()) {
//                            OptionItem.Option optionContent = (OptionItem.Option) checkOption.getTag();
//                            unNecessaryArrayList.add(optionContent);
//                        }
//
//                    }
//
//                }


                menu_detail_option_unnecessary_content.setText(getSelectOption(unNecessaryArrayList));
                optionDialogArray[1].dismiss();
                ((TextView) optionDialogArray[1].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");

            };

            unNecessaryOptionConfirmButton.setOnClickListener(clickListener);
            setOptionRecyclerView((RecyclerView) unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), menu_detail_order, (TextView) unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, false);
            optionDialogArray[0] = null;
            optionDialogArray[1] = unNecessaryOptionDialog;

        }

        public void bind(MenuContentItem menuContentItem, int position) {
            String reviewCount = "(" + menuContentItem.reviewCnt + ")";
            String menuPrice = menuContentItem.price+"원";
            if (menuContentItem.rating.length() == 1) {
                menuContentItem.rating += ".0";
            }

            Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(menu_master_picture);
            Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(detailHotMenuPicture);
            menu_master_name.setText(menuContentItem.name);
            menu_master_price.setText(menuPrice);
            menu_detail_name.setText(menuContentItem.name);
            menu_detail_name.setText(menuContentItem.name);
            menu_detail_price.setText(menuPrice);
            menu_detail_rating.setText(menuContentItem.rating);
            menu_detail_review.setText(reviewCount);
            menu_detail_rating_bar.setRating(Float.parseFloat(menuContentItem.rating));
            menu_detail_rating_bar.setStepSize(0.5f);
            menu_detail_order.setText(menuPrice + " 바로주문");

            if (menuContentItem.description.length() > 1) {
                menu_detail_description.setText(menuContentItem.description);
            } else {
                menu_detail_description.setVisibility(View.GONE);
            }

            menu_detail_unnecessary_option_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionDialogArray[1].show();
                }
            });

            if (position == expandedPosition) {
                menu_master_layout.setVisibility(View.GONE);
                menu_detail_layout.setVisibility(View.VISIBLE);
                itemView.setClickable(false);
                itemView.setEnabled(false);
                setUnNecessaryOptionDialog(menuContentItem.option);


            } else {
                menu_master_layout.setVisibility(View.VISIBLE);
                menu_detail_layout.setVisibility(View.GONE);
            }

        }
    }


    class ViewHolderWithPictureNecessary extends RecyclerView.ViewHolder {

        //Layout
        @BindView(R.id.menu_master_layout)
        ConstraintLayout menu_master_layout;
        @BindView(R.id.menu_detail_layout)
        LinearLayout menu_detail_layout;

        //Master
        @BindView(R.id.menu_master_picture)
        ImageView menu_master_picture;
        @BindView(R.id.menu_master_name)
        TextView menu_master_name;
        @BindView(R.id.menu_master_price)
        TextView menu_master_price;

        //Detail
        @BindView(R.id.menu_detail_picture)
        ImageView detailHotMenuPicture;
        @BindView(R.id.menu_detail_name)
        TextView menu_detail_name;
        @BindView(R.id.menu_detail_price)
        TextView menu_detail_price;
        @BindView(R.id.menu_detail_description)
        TextView menu_detail_description;
        @BindView(R.id.menu_detail_rating)
        TextView menu_detail_rating;
        @BindView(R.id.menu_detail_rating_bar)
        RatingBar menu_detail_rating_bar;
        @BindView(R.id.menu_detail_review)
        TextView menu_detail_review;
        @BindView(R.id.menu_detail_cart)
        Button menu_detail_cart;
        @BindView(R.id.menu_detail_order)
        Button menu_detail_order;


        @BindView(R.id.menu_detail_necessary_option_layout)
        RelativeLayout menu_detail_necessary_option_layout;

        @BindView(R.id.menu_detail_option_necessary_title)
        TextView menu_detail_option_necessary_title;

        @BindView(R.id.menu_detail_option_necessary_content)
        TextView menu_detail_option_necessary_content;


        @BindView(R.id.menu_detail_total_price)
        TextView menu_detail_total_price;


//        @BindView(R.id.menu_master_basic_price)
//        TextView menu_master_basic_price;

        ViewHolderWithPictureNecessary(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        public void bind(MenuContentItem menuContentItem, int position) {
            Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(menu_master_picture);
            Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(detailHotMenuPicture);
            String reviewCount = "(" + menuContentItem.reviewCnt + ")";
            String menuPrice = menuContentItem.price+"원";
            if (menuContentItem.rating.length() == 1) {
                menuContentItem.rating += ".0";
            }
            menu_master_name.setText(menuContentItem.name);
            menu_master_price.setText(menuPrice);
            menu_detail_name.setText(menuContentItem.name);
            menu_detail_name.setText(menuContentItem.name);
            menu_detail_price.setText(menuPrice);
            menu_detail_rating.setText(menuContentItem.rating);
            menu_detail_review.setText(reviewCount);
            menu_detail_rating_bar.setRating(Float.parseFloat(menuContentItem.rating));
            menu_detail_rating_bar.setStepSize(0.5f);
            menu_detail_order.setText(menuPrice + " 바로 주문");
            menu_detail_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkCartAndInsertOrOrder(menuContentItem,"order");
                }
            });

            if (menuContentItem.description.length() > 1) {
                menu_detail_description.setText(menuContentItem.description);
            } else {
                menu_detail_description.setVisibility(View.GONE);
            }
            menu_detail_necessary_option_layout.setOnClickListener(v -> optionDialogArray[0].show());

            if (position == expandedPosition) {
                menu_master_layout.setVisibility(View.GONE);
                menu_detail_layout.setVisibility(View.VISIBLE);
                itemView.setClickable(false);
                itemView.setEnabled(false);
                setFirstNecessaryOption(menuContentItem);
                setNecessaryOptionDialog(menuContentItem);

            } else {

                menu_master_layout.setVisibility(View.VISIBLE);
                menu_detail_layout.setVisibility(View.GONE);

            }


        }


        void setNecessaryOptionDialog(MenuContentItem menuContentItem) {
            ArrayList<OptionItem> optionCategory=menuContentItem.option;
            BottomSheetDialog necessaryOptionDialog = new BottomSheetDialog(context);
            View necessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
            necessaryOptionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            necessaryOptionDialog.setContentView(necessaryOptionDialogView);

            Button necessaryOptionConfirmButton = necessaryOptionDialogView.findViewById(R.id.optionConfirmButton);

            View.OnClickListener clickListener = v -> {
                necessaryArrayList.clear();
                TextView dialogTotalPrice;
                RecyclerView optionCategoryList;
                dialogTotalPrice =  necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice);
                optionCategoryList =  necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview);


                for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
                    View categoryView = optionCategoryList.getChildAt(i);

                    //CategoryName
                    LinearLayout optionContentList = categoryView.findViewById(R.id.dialog_test);

                    for (int j = 0; j < optionContentList.getChildCount(); j++) {

                        View optionView = optionContentList.getChildAt(j);
                        if(optionView.findViewById(R.id.option_radio)!=null){
                            RadioButton radioOption =  optionView.findViewById(R.id.option_radio);
                            if (radioOption.isChecked()) {
                                OptionItem.Option optionContent = (OptionItem.Option) radioOption.getTag();
                                necessaryArrayList.add(optionContent);
                            }
                        }else{
                            CheckBox checkOption = optionView.findViewById(R.id.option_checkbox);
                            if (checkOption.isChecked()) {
                                OptionItem.Option optionContent = (OptionItem.Option) checkOption.getTag();
                                necessaryArrayList.add(optionContent);
                            }
                        }



//                        if (radioOption.getVisibility() == View.VISIBLE && radioOption.isChecked()) {
//
//                            OptionItem.Option optionContent = (OptionItem.Option) radioOption.getTag();
//                            necessaryArrayList.add(optionContent);
//
//                        } else if (checkOption.getVisibility() == View.VISIBLE && checkOption.isChecked()) {
//
//                            OptionItem.Option optionContent = (OptionItem.Option) checkOption.getTag();
//                            necessaryArrayList.add(optionContent);
//                        }

                    }
                }
                menu_detail_option_necessary_content.setText(getSelectOption(necessaryArrayList));
                optionDialogArray[0].dismiss();
                ((TextView) optionDialogArray[0].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");



            };
            necessaryOptionConfirmButton.setOnClickListener(clickListener);
            setOptionRecyclerView((RecyclerView) necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), menu_detail_order, (TextView) necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, true);
            optionDialogArray[0] = necessaryOptionDialog;
            optionDialogArray[1] = null;

        }

        /**
         * 필수 항목의 가장 첫번째 옵션(기본옵션) 가져와 뷰에 뿌려주고 저장
         * param1 : Menu
         **/

        public void setFirstNecessaryOption(MenuContentItem menuContentItem) {
            necessaryArrayList.clear();
            ArrayList<OptionItem> optionCategoryArray = menuContentItem.option;

            StringBuffer necessaryCategoryText = new StringBuffer("");
            StringBuffer necessaryOptionText = new StringBuffer("");

            if (menuContentItem.option.size() == 1) {
                OptionItem.Option necessaryBasicOption = optionCategoryArray.get(0).necessary.get(0);
                necessaryArrayList.add(necessaryBasicOption);
                Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");
                menu_detail_option_necessary_title.setText(optionCategoryArray.get(0).menu_option_category_name);
                menu_detail_option_necessary_content.setText(necessaryBasicOption.menu_option_name);

            } else if (menuContentItem.option.size() > 1) {
                for (int i = 0; i < optionCategoryArray.size(); i++) {
                    OptionItem categoryOption = optionCategoryArray.get(i);
                    OptionItem.Option necessaryBasicOption = optionCategoryArray.get(i).necessary.get(0);

                    if (i != optionCategoryArray.size() - 1) {
                        necessaryCategoryText.append(categoryOption.menu_option_category_name + ",");
                        necessaryOptionText.append(necessaryBasicOption.menu_option_name + ",");
                    } else {
                        necessaryCategoryText.append(categoryOption.menu_option_category_name);
                        necessaryOptionText.append(necessaryBasicOption.menu_option_name);
                    }
                    necessaryArrayList.add(necessaryBasicOption);
                    Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");
                }

                menu_detail_option_necessary_title.setText(necessaryCategoryText);
                menu_detail_option_necessary_content.setText(necessaryOptionText);
            }

        }


    }


    class ViewHolderWithPictureDoubleOption extends RecyclerView.ViewHolder {

        //Layout
        @BindView(R.id.menu_master_layout)
        ConstraintLayout menu_master_layout;
        @BindView(R.id.menu_detail_layout)
        LinearLayout menu_detail_layout;

        //Master
        @BindView(R.id.menu_master_picture)
        ImageView menu_master_picture;
        @BindView(R.id.menu_master_name)
        TextView menu_master_name;
        @BindView(R.id.menu_master_price)
        TextView menu_master_price;

        //Detail
        @BindView(R.id.menu_detail_picture)
        ImageView detailHotMenuPicture;
        @BindView(R.id.menu_detail_name)
        TextView menu_detail_name;
        @BindView(R.id.menu_detail_price)
        TextView menu_detail_price;
        @BindView(R.id.menu_detail_description)
        TextView menu_detail_description;
        @BindView(R.id.menu_detail_rating)
        TextView menu_detail_rating;
        @BindView(R.id.menu_detail_rating_bar)
        RatingBar menu_detail_rating_bar;
        @BindView(R.id.menu_detail_review)
        TextView menu_detail_review;
        @BindView(R.id.menu_detail_cart)
        Button menu_detail_cart;
        @BindView(R.id.menu_detail_order)
        Button menu_detail_order;


        @BindView(R.id.menu_detail_necessary_option_layout)
        RelativeLayout menu_detail_necessary_option_layout;

        @BindView(R.id.menu_detail_option_necessary_title)
        TextView menu_detail_option_necessary_title;

        @BindView(R.id.menu_detail_option_necessary_content)
        TextView menu_detail_option_necessary_content;

        @BindView(R.id.menu_detail_option_unnecessary_content)
        TextView menu_detail_option_unnecessary_content;

        @BindView(R.id.menu_detail_unnecessary_option_layout)
        RelativeLayout menu_detail_unnecessary_option_layout;


        @BindView(R.id.menu_detail_total_price)
        TextView menu_detail_total_price;


//        @BindView(R.id.menu_master_basic_price)
//        TextView menu_master_basic_price;

        ViewHolderWithPictureDoubleOption(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        public void bind(MenuContentItem menuContentItem, int position) {

            String reviewCount = "(" + menuContentItem.reviewCnt + ")";
            String menuPrice = menuContentItem.price+"원";
            if (menuContentItem.rating.length() == 1) {
                menuContentItem.rating += ".0";
            }
            Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(menu_master_picture);
            Picasso.with(context).load(menuContentItem.picture).transform(new CropCircleTransformation()).into(detailHotMenuPicture);
            menu_master_name.setText(menuContentItem.name);
            menu_master_price.setText(menuPrice);
            menu_detail_name.setText(menuContentItem.name);
            menu_detail_name.setText(menuContentItem.name);
            menu_detail_price.setText(menuPrice);
            menu_detail_rating.setText(menuContentItem.rating);
            menu_detail_review.setText(reviewCount);
            menu_detail_rating_bar.setRating(Float.parseFloat(menuContentItem.rating));
            menu_detail_rating_bar.setStepSize(0.5f);


            if (menuContentItem.description.length() > 1) {
                menu_detail_description.setText(menuContentItem.description);
            } else {
                menu_detail_description.setVisibility(View.GONE);
            }

            menu_detail_order.setText(menuPrice + " 바로 주문");
            menu_detail_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkCartAndInsertOrOrder(menuContentItem,"order");
                }
            });

            View.OnClickListener optionClickListener = v -> {
                if (v == menu_detail_necessary_option_layout) {
                    optionDialogArray[0].show();
                } else {
                    optionDialogArray[1].show();
                }
            };
            menu_detail_necessary_option_layout.setOnClickListener(optionClickListener);
            menu_detail_unnecessary_option_layout.setOnClickListener(optionClickListener);


            if (position == expandedPosition) {
                setFirstNecessaryOption(menuContentItem);
                setDoubleOptionDialog(menuContentItem.option);
                menu_master_layout.setVisibility(View.GONE);
                menu_detail_layout.setVisibility(View.VISIBLE);
                itemView.setClickable(false);
                itemView.setEnabled(false);

            } else {

                menu_master_layout.setVisibility(View.VISIBLE);
                menu_detail_layout.setVisibility(View.GONE);

            }


        }

//        void addSelectOptionToArray(){
//            LinearLayout linearLayouts=(LinearLayout)optionDialogArray[0].findViewById(R.id.dialog_test);
//            for(int i=0; i<linearLayouts.getChildCount(); i++){
//                Button button=(Button)linearLayouts.getChildAt(i);
//                Logger.d(button.getText());
//            }
//            RecyclerView optionCategoryList=(RecyclerView) optionDialogArray[0].findViewById(R.id.dialog_menu_option_listview);
//            for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
//                View categoryView = optionCategoryList.getChildAt(i);
//
//                RecyclerView optionList = (RecyclerView) categoryView.findViewById(R.id.dialog_menu_option_content_listview);
//                Logger.d(optionList.getChildCount());
//                for (int j = 0; j < optionList.getChildCount(); j++) {
//                    View optionView = optionList.getChildAt(j);
//                    RadioButton radioOption = (RadioButton) optionView.findViewById(R.id.dialog_menu_option_radio);
//                    CheckBox checkOption = (CheckBox) optionView.findViewById(R.id.dialog_menu_option_checkbox);
//
//                    if ( radioOption.isChecked()) {
//                        OptionItem.Option optionContent = (OptionItem.Option) radioOption.getTag();
//                        Logger.d(optionContent.menu_option_name);
//                        necessaryArrayList.add(optionContent);
//
//                    } else if ( checkOption.isChecked()) {
//
//                        OptionItem.Option optionContent = (OptionItem.Option) checkOption.getTag();
//                        necessaryArrayList.add(optionContent);
//
//                    }
//
//                }
//            }
//
//            optionCategoryList=(RecyclerView) optionDialogArray[1].findViewById(R.id.dialog_menu_option_listview);
//            Logger.d(optionCategoryList.getChildCount());
//            for (int i = 0; i < optionCategoryList.getChildCount(); i++) {
//                View categoryView = optionCategoryList.getChildAt(i);
//                RecyclerView optionList = (RecyclerView) categoryView.findViewById(R.id.dialog_menu_option_content_listview);
//
//                for (int j = 0; j < optionList.getChildCount(); j++) {
//
//                    View optionView = optionList.getChildAt(j);
//                    RadioButton radioOption = (RadioButton) optionView.findViewById(R.id.dialog_menu_option_radio);
//                    CheckBox checkOption = (CheckBox) optionView.findViewById(R.id.dialog_menu_option_checkbox);
//
//                    if ( radioOption.isChecked()) {
//                        OptionItem.Option optionContent = (OptionItem.Option) radioOption.getTag();
//
//                        unNecessaryArrayList.add(optionContent);
//
//                    } else if ( checkOption.isChecked()) {
//                        OptionItem.Option optionContent = (OptionItem.Option) checkOption.getTag();
//
//                        unNecessaryArrayList.add(optionContent);
//
//                    }
//
//                }
//            }
//
//
//
//        }

        void setDoubleOptionDialog(ArrayList<OptionItem> optionCategory) {
            BottomSheetDialog necessaryOptionDialog = new BottomSheetDialog(context);
            BottomSheetDialog unNecessaryOptionDialog = new BottomSheetDialog(context);
            View necessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
            necessaryOptionDialog.setContentView(necessaryOptionDialogView);
            View unNecessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null);
            unNecessaryOptionDialog.setContentView(unNecessaryOptionDialogView);


            Button necessaryOptionConfirmButton = (Button) necessaryOptionDialogView.findViewById(R.id.optionConfirmButton);
            Button unNecessaryOptionConfirmButton = (Button) unNecessaryOptionDialogView.findViewById(R.id.optionConfirmButton);
            setOptionRecyclerView((RecyclerView) necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), menu_detail_order, (TextView) necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, true);
            setOptionRecyclerView((RecyclerView) unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), menu_detail_order, (TextView) unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, false);
            optionDialogArray[0] = necessaryOptionDialog;
            optionDialogArray[1] = unNecessaryOptionDialog;

            View.OnClickListener clickListener = v -> {
                necessaryArrayList.clear();
                unNecessaryArrayList.clear();

//                addSelectOptionToArray();
                if (v == necessaryOptionConfirmButton) {
                    menu_detail_option_necessary_content.setText(getSelectOption(necessaryArrayList));
                    TextView dialogTotalPrice = (TextView) optionDialogArray[0].findViewById(R.id.dialog_totalPrice);
                    ((TextView) optionDialogArray[0].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                    ((TextView) optionDialogArray[1].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                    menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");
                    optionDialogArray[0].dismiss();
                } else {
                    menu_detail_option_unnecessary_content.setText(getSelectOption(unNecessaryArrayList));
                    TextView dialogTotalPrice = (TextView) optionDialogArray[1].findViewById(R.id.dialog_totalPrice);
                    ((TextView) optionDialogArray[1].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                    ((TextView) optionDialogArray[0].findViewById(R.id.dialog_totalPrice)).setText(dialogTotalPrice.getText());
                    menu_detail_order.setText(dialogTotalPrice.getText() + " 바로 주문");
                    optionDialogArray[1].dismiss();
                }


            };
            necessaryOptionConfirmButton.setOnClickListener(clickListener);
            unNecessaryOptionConfirmButton.setOnClickListener(clickListener);

        }


        /**
         * 필수 항목의 가장 첫번째 옵션 가져와 뷰에 뿌려주고 local db에 추가하기
         * param1 : Menu
         **/

        void setFirstNecessaryOption(MenuContentItem menuContentItem) {
            necessaryArrayList.clear();
            ArrayList<OptionItem> optionCategory = new ArrayList<>();
            for (int i = 0; i < menuContentItem.option.size(); i++) {
                if (menuContentItem.option.get(i).necessary.size() > 0) {
                    optionCategory.add(menuContentItem.option.get(i));
                }
            }


            StringBuffer necessaryCategoryText = new StringBuffer("");
            StringBuffer necessaryOptionText = new StringBuffer("");

            if (menuContentItem.option.size() == 1) {
                OptionItem.Option necessaryBasicOption = optionCategory.get(0).necessary.get(0);
                necessaryArrayList.add(necessaryBasicOption);
                Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");
                menu_detail_option_necessary_title.setText(optionCategory.get(0).menu_option_category_name);
                menu_detail_option_necessary_content.setText(necessaryBasicOption.menu_option_name);

            } else if (menuContentItem.option.size() > 1) {
                for (int i = 0; i < optionCategory.size(); i++) {

                    OptionItem.Option necessaryBasicOption = optionCategory.get(i).necessary.get(0);
                    necessaryArrayList.add(necessaryBasicOption);
                    Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가");

                    //Category
                    OptionItem categoryOption = optionCategory.get(i);


                    if (i != optionCategory.size() - 1) {
                        necessaryCategoryText.append(categoryOption.menu_option_category_name + ",");
                        necessaryOptionText.append(categoryOption.necessary.get(0).menu_option_name + ",");
                    } else {
                        necessaryCategoryText.append(categoryOption.menu_option_category_name);
                        necessaryOptionText.append(categoryOption.necessary.get(0).menu_option_name);
                    }
                }

                menu_detail_option_necessary_title.setText(necessaryCategoryText);
                menu_detail_option_necessary_content.setText(necessaryOptionText);
            }

        }


    }


    class ViewHolderNoPicture extends RecyclerView.ViewHolder {
        //Layout
        @BindView(R.id.menu_master_layout)
        ConstraintLayout menu_master_layout;
        @BindView(R.id.menu_detail_layout)
        LinearLayout menu_detail_layout;


        //Master
        @BindView(R.id.menu_master_name)
        TextView menu_master_name;
        @BindView(R.id.menu_master_price)
        TextView menu_master_price;

        //Detail
        @BindView(R.id.menu_detail_name)
        TextView menu_detail_name;
        @BindView(R.id.menu_detail_price)
        TextView menu_detail_price;
        @BindView(R.id.menu_detail_description)
        TextView menu_detail_description;
        @BindView(R.id.menu_detail_rating)
        TextView menu_detail_rating;
        @BindView(R.id.menu_detail_rating_bar)
        RatingBar menu_detail_rating_bar;
        @BindView(R.id.menu_detail_review)
        TextView menu_detail_review;
        @BindView(R.id.menu_detail_cart)
        Button menu_detail_cart;
        @BindView(R.id.menu_detail_order)
        Button menu_detail_order;


        ViewHolderNoPicture(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MenuContentItem menuContentItem, int position) {
            String reviewCount = "(" + menuContentItem.reviewCnt + ")";
            String menuPrice = menuContentItem.price+"원";
            if (menuContentItem.rating.length() == 1) {
                menuContentItem.rating += ".0";
            }
            menu_master_name.setText(menuContentItem.getName());
            menu_master_price.setText(menuPrice);
            menu_detail_name.setText(menuContentItem.getName());
            menu_detail_name.setText(menuContentItem.getName());
            menu_detail_price.setText(menuPrice);
            menu_detail_rating.setText(menuContentItem.rating);
            menu_detail_review.setText(reviewCount);
            menu_detail_rating_bar.setRating(Float.parseFloat(menuContentItem.rating));
            menu_detail_rating_bar.setStepSize(0.5f);
            menu_detail_description.setText(menuContentItem.getDescription());


            if (position == expandedPosition) {
                menu_master_layout.setVisibility(View.GONE);
                menu_detail_layout.setVisibility(View.VISIBLE);
                itemView.setClickable(false);
            } else {
                menu_master_layout.setVisibility(View.VISIBLE);
                menu_detail_layout.setVisibility(View.GONE);
            }
        }


    }
}



