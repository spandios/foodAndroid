package com.example.fooddeuk.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.CartActivity;
import com.example.fooddeuk.http.MenuReviewService;
import com.example.fooddeuk.http.OptionService;
import com.example.fooddeuk.http.RestaurantService;
import com.example.fooddeuk.model.cart.CartItem;
import com.example.fooddeuk.model.cart.CartMenu;
import com.example.fooddeuk.model.cart.CartOption;
import com.example.fooddeuk.model.menu.MenuItem;
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
    private ArrayList<MenuItem> items;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnCartCountClickListener onCartCountClickListener;
    private RestaurantItem.Restaurant restaurant;
    private static final int noPicture = 0;
    private static final int havePicture = 1;
    private int expandedPosition = -1;
    private RecyclerView mRecyclerView;




    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder view,float y,RecyclerView recyclerView);
    }

    public interface OnCartCountClickListener {
        void onCartCount(int cartSize);
    }



    static private void reviewDialogShow(Context context, ArrayList<ReviewItem> reviewItem) {
        MaterialDialog reviewDialog = new MaterialDialog.Builder(context).customView(R.layout.dialog_review_listview, true).build();
        View reviewDialogView = reviewDialog.getView();
        RecyclerView reviewListView = (RecyclerView) reviewDialogView.findViewById(R.id.reviewListView);
        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewItem);
        LayoutUtil.RecyclerViewSetting(context, reviewListView);
        reviewListView.setAdapter(reviewAdapter);
        reviewDialog.show();
    }

    private void reviewClick(RecyclerView.ViewHolder view){
        MenuReviewService.getReview(items.get(view.getAdapterPosition()).menu_id).enqueue(new Callback<ArrayList<ReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewItem>> call, Response<ArrayList<ReviewItem>> response) {
                reviewDialogShow(context,response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<ReviewItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

//    private View.OnClickListener reviewCLickListener= v -> reviewClick();


    private View.OnClickListener menuExpandLayoutListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            LinearLayout linearLayout=(LinearLayout)v.findViewById(R.id.menu_detail_layout);

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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



    public MenuListAdapter(Context context, ArrayList<MenuItem> items) {
        this.items = items;
        this.context = context;
        getRestaurant();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == noPicture) {
            View parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_not_have_picture, parent, false);

            // set the view's size, margins, paddings and layout parameters
            ViewHolderNoPicture vh = new ViewHolderNoPicture(parentView);

            vh.itemView.setOnClickListener(menuExpandLayoutListener);
            vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v-> reviewClick(vh));

//            reviewClick(vh.itemView.findViewById(R.id.menu_detail_rating),items.get(vh.getAdapterPosition()).menu_id);

            return vh;
        } else {
            View parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_have_picture, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderWithPicture vh = new ViewHolderWithPicture(parentView);
            vh.itemView.setOnClickListener(menuExpandLayoutListener);

            /*Show Review Dialog*/
            vh.itemView.findViewById(R.id.menu_detail_rating).setOnClickListener(v-> reviewClick(vh));
//            reviewClick(vh.itemView.findViewById(R.id.menu_detail_rating),items.get(vh.getAdapterPosition()).menu_id);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder tmpHolder, int position) {
        final MenuItem menuItem = items.get(position);

        //메뉴 사진 없음
        if (tmpHolder.getItemViewType() == noPicture) {
            ViewHolderNoPicture holder = (ViewHolderNoPicture) tmpHolder;
            holder.bind(menuItem);
            /*------------------------------------------*/
            //펼침
            if (position == expandedPosition) {
                holder.menu_master_layout.setVisibility(View.GONE);
                holder.menu_detail_layout.setVisibility(View.VISIBLE);
            } else {

                holder.menu_master_layout.setVisibility(View.VISIBLE);
                holder.menu_detail_layout.setVisibility(View.GONE);
            }
            holder.itemView.setTag(holder);

            /*------------------------------------------*/
            //주문하기
            holder.menu_detail_order.setOnClickListener(v -> Order(menuItem));

        } else {
            ViewHolderWithPicture holder = (ViewHolderWithPicture) tmpHolder;
            holder.bind(menuItem);
            //사진
            Picasso.with(context).load(menuItem.getMenupicture()).transform(new CropCircleTransformation()).into(holder.menu_master_picture);
            Picasso.with(context).load(menuItem.getMenupicture()).transform(new CropCircleTransformation()).into(holder.detailHotMenuPicture);
//            Picasso.with(context).load(menuItem.getMenupicture()).fit().into(holder.menu_master_picture);
//            Picasso.with(context).load(menuItem.getMenupicture()).fit().into(holder.detailHotMenuPicture);

             /*------------------------------------------*/
            //펼침
            if (position == expandedPosition) {
                holder.menu_master_layout.setVisibility(View.GONE);
                holder.menu_detail_layout.setVisibility(View.VISIBLE);
            } else {

                holder.menu_master_layout.setVisibility(View.VISIBLE);
                holder.menu_detail_layout.setVisibility(View.GONE);
            }
            holder.itemView.setTag(holder);

            /*------------------------------------------*/
            //주문하기
            holder.menu_detail_order.setOnClickListener(v -> Order(menuItem));
        }
    }


    //식당정보
    private void getRestaurant(){
        RestaurantService.getRestaurantById(items.get(0).rest_id).enqueue(new Callback<RestaurantItem>() {
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

    private void Order(MenuItem menuItem){
        OptionService.getOption(menuItem.getMenu_id()).enqueue(new Callback<ArrayList<OptionItem>>() {
            @Override
            public void onResponse(Call<ArrayList<OptionItem>> call, Response<ArrayList<OptionItem>> response) {
                if (response.isSuccessful()) {

                    if (response.body().size() > 0) {
                        optionDialogShow(menuItem, response.body());
                    } else {
                        /**
                         *
                         * TODO 바로 주문
                         *
                         * **/
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OptionItem>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }




    private void createOptionFooterButton(int index, ArrayList<MaterialDialog> optionDialogArray, LinearLayout dialog_menu_option_confirm_layout, TextView dialog_totalPrice, MaterialDialog optionDialog, MenuItem menuItem, String version) {
        try {
            switch (version) {
                case "prev":
                    Button prevOptionButton = new Button(context);
                    prevOptionButton.setText("이전으로");
                    prevOptionButton.setOnClickListener(v -> {
                        TextView totalPrice = (TextView) optionDialogArray.get(index - 1).getView().findViewById(R.id.dialog_totalPrice);
                        totalPrice.setText(dialog_totalPrice.getText().toString());
                        optionDialog.dismiss();
                        optionDialogArray.get(index - 1).show();
                    });
                    dialog_menu_option_confirm_layout.addView(prevOptionButton);
                    break;


                case "cart":
                    Button addCartButton = new Button(context);
                    addCartButton.setText("장바구니 담기");
                    addCartButton.setOnClickListener(v -> {
                        checkCartAndInsertOrOrder(menuItem, optionDialogArray, optionDialog, "cart");
                    });
                    dialog_menu_option_confirm_layout.addView(addCartButton);
                    break;

                case "order":
                    Button directOrderButton = new Button(context);
                    directOrderButton.setText("바로 주문");
                    directOrderButton.setOnClickListener(v -> {
                        checkCartAndInsertOrOrder(menuItem, optionDialogArray, optionDialog, "order");
                    });
                    dialog_menu_option_confirm_layout.addView(directOrderButton);
                    break;


                case "next":
                    Button nextOptionButton = new Button(context);
                    nextOptionButton.setText("다음으로");
                    nextOptionButton.setOnClickListener(v -> {
                        optionDialog.dismiss();
                        TextView totalPrice = (TextView) optionDialogArray.get(index + 1).getView().findViewById(R.id.dialog_totalPrice);
                        totalPrice.setText(dialog_totalPrice.getText().toString());
                        optionDialogArray.get(index + 1).show();
                    });
                    dialog_menu_option_confirm_layout.addView(nextOptionButton);
                    break;

                default:
                    throw new Exception("올바르지 않은 버젼");
            }
        } catch (Exception e) {
            Logger.d(e);
        }
    }

    //메뉴옵션 dialog
    private void optionDialogShow(MenuItem menuItem, final ArrayList<OptionItem> options) {

        //dialog를 ArrayList에 추가
        //optionDialogArray가 final 이기 때문에 이 함수가 끝난 이후에도 정보가 남아있음
        final ArrayList<MaterialDialog> optionDialogArray = new ArrayList<>();

        for (int i = 0; i < options.size(); i++) {

            final int index = i;
            final ArrayList<OptionItem.Option> optionItem = options.get(i).getOption();
            final MaterialDialog optionDialog = new MaterialDialog.Builder(context).customView(R.layout.dailog_menu_option, true).build();
            final View optionDialogView = optionDialog.getView();
            TextView category_name = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_name);
            TextView category_leftCount = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_count);
            TextView category_necessary = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_necessary);
            final TextView dialog_totalPrice = (TextView) optionDialogView.findViewById(R.id.dialog_totalPrice);
            final RecyclerView optionListView = (RecyclerView) optionDialogView.findViewById(R.id.dialog_menu_option_listview);
            LinearLayout dialog_menu_option_confirm_layout = (LinearLayout) optionDialogView.findViewById(R.id.dialog_menu_option_confirm_layout);

            /***
             * 옵션 헤더 부분
             * **/
            //옵션카테고리 이름
            category_name.setText(options.get(i).getMenu_option_category_name());
            //남은 옵션 개수
            category_leftCount.setText("(" + (i + 1) + "/" + options.size() + ")");
            //옵션 필수여부
            category_necessary.setText((options.get(i).getNecessary() == 1) ? "필수" : "필수아님");
            //기본 총 가격
            dialog_totalPrice.setText(PriceUtil.comma_won(menuItem.getPrice()));


            /**옵션 리스트 본문  **/
            //옵션 리사이클러뷰 셋팅
            setOptionRecyclerView(optionListView, optionItem, options.get(i).getMultiple(), dialog_totalPrice);


            /**옵션 하단 부분 **/
            //마지막 옵션 창일 때
            if (options.size() == i + 1) {
                //옵션 2개 이상일 때만 이전버튼 생성
                if (options.size() != 1) {
                    createOptionFooterButton(index, optionDialogArray, dialog_menu_option_confirm_layout, dialog_totalPrice, optionDialog, menuItem, "prev");
                }
                createOptionFooterButton(index, optionDialogArray, dialog_menu_option_confirm_layout, dialog_totalPrice, optionDialog, menuItem, "cart");
                createOptionFooterButton(index, optionDialogArray, dialog_menu_option_confirm_layout, dialog_totalPrice, optionDialog, menuItem, "order");

            }
            //마지막 옵션 아닐 때
            else {
                //맨 처음은 이전버튼을 만들지 않는다
                if (i != 0) {
                    createOptionFooterButton(index, optionDialogArray, dialog_menu_option_confirm_layout, dialog_totalPrice, optionDialog, menuItem, "prev");
                }
                //다음 버튼 생성
                createOptionFooterButton(index, optionDialogArray, dialog_menu_option_confirm_layout, dialog_totalPrice, optionDialog, menuItem, "next");
            }


            //만든 옵션 다이어로그 배열에 저장
            optionDialogArray.add(optionDialog);
        }

        //맨 처음 옵션 설정 창 보여줌
        optionDialogArray.get(0).show();

    }



    private void checkCartAndInsertOrOrder(MenuItem menuItem, ArrayList<MaterialDialog> optionDialogArray, MaterialDialog optionDialog, String version) {
        RealmResults<CartItem> cartItem = RealmUtil.findDataAll(CartItem.class);

        //기존 장바구니가 없으면 바로 추가
        if (cartItem.size() == 0) {
            RestaurantItemRealm newRestaurant = new RestaurantItemRealm(restaurant.rest_id, restaurant.rest_admin_id, restaurant.name, restaurant.address, restaurant.open_time, restaurant.close_time, restaurant.avg_cooking_time, restaurant.latlng[1], restaurant.latlng[0]);
            RealmUtil.insertData(newRestaurant);
            RealmUtil.insertData(createCartMenuItem(menuItem, optionDialogArray));
            if (version.equals("order")) {
                IntentUtil.startActivity(context, CartActivity.class);
            }
            onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));
            optionDialog.dismiss();

        } else if (cartItem.size() > 0) {
            //장바구니에 이미 메뉴가 존재하는 경우
            RealmResults<RestaurantItemRealm> defaultRestaurant = RealmUtil.findDataAll(RestaurantItemRealm.class);
            //기존 장바구니에 있는 식당과 현재 주문할려는 식당이 일치할 경우 장바구니에 메뉴를 추가한다
            if (defaultRestaurant.get(0).rest_id == restaurant.rest_id) {
                RealmUtil.insertData(createCartMenuItem(menuItem, optionDialogArray));
                if (version.equals("order")) {
                    IntentUtil.startActivity(context, CartActivity.class);
                }
                onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));
                optionDialog.dismiss();
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
                            RealmUtil.insertData(createCartMenuItem(menuItem, optionDialogArray));
                            if (version.equals("order")) {
                                IntentUtil.startActivity(context, CartActivity.class);
                            }
                            onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));
                            optionDialog.dismiss();

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

    private void setOptionRecyclerView(RecyclerView optionRecyclerView, ArrayList<OptionItem.Option> optionItem, int multiple, TextView dialog_totalPrice) {

        //여러개 선택 가능한 옵션들의 클릭 콜백함수
        OptionAdapter.SelectCLickListener selectCLickListener = (isPlus, position) -> {
            RecyclerView.ViewHolder viewHolder = optionRecyclerView.getChildViewHolder(optionRecyclerView.getChildAt(position));
            TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
            dialog_totalPrice.setText((isPlus.equals("plus")) ?
                    PriceUtil.plusPrice(PriceUtil.TextViewToString(optionPrice), PriceUtil.TextViewToString(dialog_totalPrice)) :
                    PriceUtil.minusPrice(PriceUtil.TextViewToString(optionPrice), PriceUtil.TextViewToString(dialog_totalPrice)));
        };

        //한개만 선택 가능한 옵션들의 클릭 콜백함수
        OptionAdapter.RadioClickListener radioClickListener = (position) -> {
            //클릭 되어있는 라디오만 해제한뒤
            for (int childCount = optionRecyclerView.getChildCount(), j = 0; j < childCount; j++) {
                RecyclerView.ViewHolder viewHolder = optionRecyclerView.getChildViewHolder(optionRecyclerView.getChildAt(j));
                RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                //체크 되어 있을 경우
                if (radioButton.isChecked()) {
                    //해제
                    radioButton.setChecked(false);
                    TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
                    //최종값에 가격 마이너스 함
                    dialog_totalPrice.setText(PriceUtil.minusPrice(PriceUtil.TextViewToString(dialog_totalPrice), PriceUtil.TextViewToString(optionPrice)));
                }
            }

            //클릭한 포지션의 라디오 체크하고 최종값에 가격 플러스
            RecyclerView.ViewHolder viewHolder = optionRecyclerView.getChildViewHolder(optionRecyclerView.getChildAt(position));
            RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
            TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
            radioButton.setChecked(true);
            dialog_totalPrice.setText(PriceUtil.plusPrice(PriceUtil.TextViewToString(dialog_totalPrice), PriceUtil.TextViewToString(optionPrice)));

        };

        final OptionAdapter optionAdapter = new OptionAdapter(optionItem, context, multiple, radioClickListener, selectCLickListener);
        LayoutUtil.RecyclerViewSetting(context.getApplicationContext(), optionRecyclerView);
        optionRecyclerView.setAdapter(optionAdapter);

    }

    //필수, 선택 옵션 구분해서 값 얻어오기
    private RealmList<CartOption> getMenuOption(ArrayList<MaterialDialog> optionDialogArray) {
        RealmList<CartOption> menuOptionUnNecessary = new RealmList<>();
        RealmList<CartOption> menuOptionNecessary = new RealmList<>();
        RealmList<CartOption> menuOption = new RealmList<>();
        String menuCategoryName;

        for (MaterialDialog optionDialog : optionDialogArray) {
            View optionDialogView = optionDialog.getView();
            TextView menuCategoryNameTextView = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_name);
            menuCategoryName = menuCategoryNameTextView.getText().toString();
            final RecyclerView optionListView = (RecyclerView) optionDialogView.findViewById(R.id.dialog_menu_option_listview);
            for (int childCount = optionListView.getChildCount(), i = 0; i < childCount; i++) {
                RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(i));
                RadioButton optionRadio = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                CheckBox optionCheckBox = (CheckBox) viewHolder.itemView.findViewById(R.id.dialog_menu_option_checkbox);

                if (optionRadio.getVisibility() == View.VISIBLE) {

                    if (optionRadio.isChecked()) {
                        OptionItem.Option optionTempRadio = (OptionItem.Option) optionRadio.getTag();
                        menuOptionNecessary.add(new CartOption(optionTempRadio.getMenu_option_id(), optionTempRadio.getMenu_option_name(), optionTempRadio.getMenu_price(), menuCategoryName));
                    }

                } else {
                    if (optionCheckBox.isChecked()) {
                        OptionItem.Option optionTempCheckbox = (OptionItem.Option) optionCheckBox.getTag();
                        menuOptionUnNecessary.add(new CartOption(optionTempCheckbox.getMenu_option_id(), optionTempCheckbox.getMenu_option_name(), optionTempCheckbox.getMenu_price(), menuCategoryName));
                    }
                }
            }
        }
        //필수값 먼저 삽입한다
        if (menuOptionNecessary.size() >= 1) {
            menuOption.addAll(menuOptionNecessary);
        }
        if (menuOptionUnNecessary.size() >= 1) {
            menuOption.addAll(menuOptionUnNecessary);
        }
        return menuOption;
    }


    //선탠된 메뉴 CartItem RealmObject 생성
    private CartItem createCartMenuItem(MenuItem menuItem, ArrayList<MaterialDialog> optionDialogArray) {
        CartMenu menu = new CartMenu(menuItem.getMenu_id(), menuItem.getName(), menuItem.getPrice(), menuItem.getAvgtime());
        CartItem cartItem;
        if (optionDialogArray.size() >= 1) {
            RealmList<CartOption> menuOption = getMenuOption(optionDialogArray);
            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu, menuOption);
        } else {
            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu);
        }
        return cartItem;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getMenupicture().length() > 0) {
            return havePicture;
        } else {
            return noPicture;
        }
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



    class ViewHolderWithPicture extends RecyclerView.ViewHolder {

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
//        @BindView(R.id.menu_master_basic_price)
//        TextView menu_master_basic_price;

        ViewHolderWithPicture(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        public void bind(MenuItem menuItem){
            String reviewCount="("+menuItem.review_count+")";
            String menuPrice=PriceUtil.comma_won(menuItem.price);
            if(menuItem.rating.length()==1){
                menuItem.rating+=".0";
            }
            menu_master_name.setText(menuItem.getName());
            menu_master_price.setText(menuPrice);
            menu_detail_name.setText(menuItem.getName());
            menu_detail_name.setText(menuItem.getName());
            menu_detail_price.setText(menuPrice);
            menu_detail_rating.setText(menuItem.rating);
            menu_detail_review.setText(reviewCount);
            menu_detail_rating_bar.setRating(Float.parseFloat(menuItem.getRating()));
            menu_detail_rating_bar.setStepSize(0.5f);
            menu_detail_description.setText(menuItem.getDescription());

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

        public void bind(MenuItem menuItem){
            String reviewCount="("+menuItem.review_count+")";
            String menuPrice=PriceUtil.comma_won(menuItem.price);
            if(menuItem.rating.length()==1){
                menuItem.rating+=".0";
            }
            menu_master_name.setText(menuItem.getName());
            menu_master_price.setText(menuPrice);
            menu_detail_name.setText(menuItem.getName());
            menu_detail_name.setText(menuItem.getName());
            menu_detail_price.setText(menuPrice);
            menu_detail_rating.setText(menuItem.rating);
            menu_detail_review.setText(reviewCount);
            menu_detail_rating_bar.setRating(Float.parseFloat(menuItem.rating));
            menu_detail_rating_bar.setStepSize(0.5f);
            menu_detail_description.setText(menuItem.getDescription());
        }
    }


}
