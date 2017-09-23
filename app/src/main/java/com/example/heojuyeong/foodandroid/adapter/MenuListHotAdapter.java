package com.example.heojuyeong.foodandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.model.CurrentLocationRestaurantItem;
import com.example.heojuyeong.foodandroid.model.MenuItem;
import com.example.heojuyeong.foodandroid.model.cart.CartItem;
import com.example.heojuyeong.foodandroid.model.cart.CartMenu;
import com.example.heojuyeong.foodandroid.model.cart.CartOption;
import com.example.heojuyeong.foodandroid.model.cart.CartRestaurant;
import com.example.heojuyeong.foodandroid.util.LayoutUtil;
import com.example.heojuyeong.foodandroid.util.PriceUtil;
import com.example.heojuyeong.foodandroid.util.RealmUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by heojuyeong on 2017. 7. 31..
 * MenuList
 */


public class MenuListHotAdapter extends RecyclerView.Adapter<MenuListHotAdapter.ViewHolder> {
    private ArrayList<MenuItem> items;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnCartCountClickListener onCartCountClickListener;
    private MaterialDialog reviewDialog;
    final private CurrentLocationRestaurantItem.Restaurant restaurant;
    private Realm realm;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCartCountClickListener(OnCartCountClickListener onCartAddClickListenr) {
        this.onCartCountClickListener = onCartAddClickListenr;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnCartCountClickListener {
        void onCartCount(int cartSize);
    }


    public MenuListHotAdapter(Context context, ArrayList<MenuItem> items, CurrentLocationRestaurantItem.Restaurant restaurant) {
        this.items = items;
        this.context = context;
        this.restaurant = restaurant;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_listview_hot_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MenuItem menuItem = items.get(position);

        Picasso.with(context).load(menuItem.getMenupicture()).fit().into(holder.hotMenuPicture);
        Picasso.with(context).load(menuItem.getMenupicture()).transform(new CropCircleTransformation()).into(holder.detailHotMenuPicture);
        holder.hotMenuName.setText(menuItem.getName());
        holder.hotMenuPrice.setText(String.valueOf(menuItem.getPrice()));
        holder.detailHotMenuName.setText(menuItem.getName());
        holder.detailHotMenuPrice.setText(String.valueOf(menuItem.getPrice()));
        holder.detailHotMenuRating.setText(menuItem.getRating());
        holder.detailHotMenuRating.setOnClickListener(v -> {
            if (menuItem.getReviews().size() >= 1)
                reviewDialogShow(menuItem);
        });
        holder.detailHotMenuReview.setText("리뷰[" + menuItem.getReviews().size() + "]");
        holder.detailHotMenuReview.setOnClickListener(v -> {
            if (menuItem.getReviews().size() >= 1)
                reviewDialogShow(menuItem);
        });
        holder.detailHotMenuDescription.setText(menuItem.getDescription());
        //주문하기
        holder.detailHotMenuOrder.setOnClickListener(v -> {
            //옵션 선택사항이 있을 경우
            if (menuItem.getOptions().size() > 1) {
                optionDialogShow(menuItem);
            } else {
                /**
                 *
                 * TODO 바로 주문
                 *
                 * **/

            }
        });


        //각 아이템 클릭 리스너
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(v, holder.getAdapterPosition()));

    }

    //메뉴옵션 dialog
    private void optionDialogShow(final MenuItem menuItem) {

        ArrayList<MenuItem.Options> options = menuItem.getOptions();

        //dialog를 ArrayList에 추가
        final ArrayList<MaterialDialog> optionDialogArray = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            final int index = i;
            final ArrayList<MenuItem.Options.Option> optionItem = options.get(i).getOption();

            final MaterialDialog optionDialog = new MaterialDialog.Builder(context).customView(R.layout.dailog_menu_option, true).build();

            final View optionDialogView = optionDialog.getView();

            TextView category_name = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_name);
            TextView category_leftCount = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_count);
            TextView category_necessary = (TextView) optionDialogView.findViewById(R.id.dialog_option_category_necessary);
            final TextView dialog_totalPrice = (TextView) optionDialogView.findViewById(R.id.dialog_totalPrice);

            final RecyclerView optionListView = (RecyclerView) optionDialogView.findViewById(R.id.dialog_menu_option_listview);

            LinearLayout dialog_menu_option_confirm_layout = (LinearLayout) optionDialogView.findViewById(R.id.dialog_menu_option_confirm_layout);


            /**
             * option header
             * **/
            category_name.setText(options.get(i).getMenu_category_name());
            category_leftCount.setText("(" + (i + 1) + "/" + options.size() + ")");
            category_necessary.setText((options.get(i).getNecessary() == 1) ? "필수" : "필수아님");
            dialog_totalPrice.setText(PriceUtil.comma_won(menuItem.getPrice()));


            OptionAdapter.SelectCLickListener selectCLickListener = (isPlus, position) -> {
                RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(position));
                TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);

                dialog_totalPrice.setText((isPlus.equals("plus")) ?
                        PriceUtil.plusPrice(PriceUtil.getString(optionPrice), PriceUtil.getString(dialog_totalPrice)) :
                        PriceUtil.minusPrice(PriceUtil.getString(optionPrice), PriceUtil.getString(dialog_totalPrice)));
            };

            /**option content**/
            //클릭 안 되어있는 라디오를 클릭했을 때만 이벤트일어남
            OptionAdapter.RadioClickListener radioClickListener = (position) -> {


                //클릭 되어있는 라디오만 해제한뒤
                for (int childCount = optionListView.getChildCount(), j = 0; j < childCount; j++) {
                    RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(j));
                    RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                    if (radioButton.isChecked()) {
                        radioButton.setChecked(false);
                        TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
                        dialog_totalPrice.setText(PriceUtil.minusPrice(PriceUtil.getString(dialog_totalPrice), PriceUtil.getString(optionPrice)));
                    }
                }

                //클릭한 포지션의 라디오 체크
                RecyclerView.ViewHolder viewHolder = optionListView.getChildViewHolder(optionListView.getChildAt(position));
                RadioButton radioButton = (RadioButton) viewHolder.itemView.findViewById(R.id.dialog_menu_option_radio);
                TextView optionPrice = (TextView) viewHolder.itemView.findViewById(R.id.dialog_menu_option_price);
                radioButton.setChecked(true);
                dialog_totalPrice.setText(PriceUtil.plusPrice(PriceUtil.getString(dialog_totalPrice), PriceUtil.getString(optionPrice)));


            };


            final OptionAdapter optionAdapter = new OptionAdapter(optionItem, context, options.get(i).getMultiple(), radioClickListener, selectCLickListener);
            LayoutUtil.RecyclerViewSetting(context.getApplicationContext(), optionListView);
            optionListView.setAdapter(optionAdapter);





            //맨 처음인덱스가 아니고 옵션2개이상 일때
//            if (options.size()>1) {
//                Button prevOptionButton = new Button(context);
//                prevOptionButton.setText("이전으로");
//                prevOptionButton.setOnClickListener(v -> {
//                    TextView totalPrice = (TextView) optionDialogArray.get(index - 1).getView().findViewById(R.id.dialog_totalPrice);
//                    totalPrice.setText(dialog_totalPrice.getText().toString());
//                    optionDialog.dismiss();
//                    optionDialogArray.get(index - 1).show();
//                });
//
//                dialog_menu_option_confirm_layout.addView(prevOptionButton);
////            }

            /**option footer**/
            //옵션 선택 카테고리의 인덱스가 마지막이면
            if (options.size() == i + 1) {

                //옵션 2개 이상일 때만
                if(options.size()!=1){
                    Button prevOptionButton = new Button(context);
                    prevOptionButton.setText("이전으로");
                    prevOptionButton.setOnClickListener(v -> {
                        TextView totalPrice = (TextView) optionDialogArray.get(index - 1).getView().findViewById(R.id.dialog_totalPrice);
                        totalPrice.setText(dialog_totalPrice.getText().toString());
                        optionDialog.dismiss();
                        optionDialogArray.get(index - 1).show();
                    });

                    dialog_menu_option_confirm_layout.addView(prevOptionButton);
                }


                Button addCartButton = new Button(context);
                addCartButton.setText("장바구니 담기");
                addCartButton.setOnClickListener(v -> {
                    // 선택한 메뉴 정보를 로컬db에저장
                    RealmUtil.insertData(createCartMenuItem(menuItem, optionDialogArray));
                    //장바구니 총 갯수 전달
                    onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));
                    optionDialog.dismiss();

                });


                Button directOrderButton = new Button(context);
                directOrderButton.setText("바로 주문");
                directOrderButton.setOnClickListener(v -> {
//                        OrderItem orderItem=initOrder(optionDialogArray,menuItem);
                    optionDialog.dismiss();
                });


                dialog_menu_option_confirm_layout.addView(addCartButton);
                dialog_menu_option_confirm_layout.addView(directOrderButton);

                //선택항목이 2개이상 일 때만 이전으로 버튼 생성



            }
            //옵션 카테고리 마지막 x
            else {

                //맨 처음 시작이 아니라면
                if (i != 0) {
                    Button prevOptionButton = new Button(context);
                    prevOptionButton.setText("이전으로");
                    prevOptionButton.setOnClickListener(v -> {
                        optionDialog.dismiss();
                        TextView totalPrice = (TextView) optionDialogArray.get(index - 1).getView().findViewById(R.id.dialog_totalPrice);
                        totalPrice.setText(dialog_totalPrice.getText().toString());
                        optionDialogArray.get(index - 1).show();

                    });

                    dialog_menu_option_confirm_layout.addView(prevOptionButton);
                }


                Button nextOptionButton = new Button(context);
                nextOptionButton.setText("다음으로");
                nextOptionButton.setOnClickListener(v -> {
                    optionDialog.dismiss();
                    TextView totalPrice = (TextView) optionDialogArray.get(index + 1).getView().findViewById(R.id.dialog_totalPrice);
                    totalPrice.setText(dialog_totalPrice.getText().toString());
                    optionDialogArray.get(index + 1).show();
                });

                dialog_menu_option_confirm_layout.addView(nextOptionButton);
            }

            optionDialogArray.add(optionDialog);

        }

        optionDialogArray.get(0).show();

    }


    //리뷰dialogshow 함수
    private void reviewDialogShow(MenuItem menuItem) {

        reviewDialog = new MaterialDialog.Builder(context).customView(R.layout.dialog_review_listview, true).build();
        View reviewDialogView = reviewDialog.getView();
        RecyclerView reviewListView = (RecyclerView) reviewDialogView.findViewById(R.id.reviewListView);
        ReviewAdapter reviewAdapter = new ReviewAdapter(context, menuItem.getReviews());
        LayoutUtil.RecyclerViewSetting(context,reviewListView);
        reviewListView.setAdapter(reviewAdapter);
        reviewDialog.show();
    }

    //선탠된 메뉴 RealmObject 생성
    private CartItem createCartMenuItem(MenuItem menuItem, ArrayList<MaterialDialog> optionDialogArray) {
        CartMenu menu = new CartMenu(menuItem.getMenu_id(), menuItem.getName(), menuItem.getPrice(), menuItem.getDescription(), menuItem.getAvgtime());
        CartRestaurant cartRestaurant = new CartRestaurant(restaurant.getRest_id(), restaurant.getName());
        CartItem cartItem;
        if (optionDialogArray.size() >= 1) {
            RealmList<CartOption> menuOption = getMenuOption(optionDialogArray);
            cartItem = new CartItem(getNextKey(), menu, cartRestaurant, menuOption);

        } else {
            cartItem = new CartItem(getNextKey(), menu, cartRestaurant);

        }
        return cartItem;
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
                        MenuItem.Options.Option optionTempRadio = (MenuItem.Options.Option) optionRadio.getTag();
                        menuOptionNecessary.add(new CartOption(optionTempRadio.getMenu_option_id(), optionTempRadio.getMenu_option_name(), optionTempRadio.getMenu_price(), menuCategoryName));
                    }

                } else {
                    if (optionCheckBox.isChecked()) {
                        MenuItem.Options.Option optionTempCheckbox = (MenuItem.Options.Option) optionCheckBox.getTag();
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


    //Auto Increment id
    private int getNextKey() {
        try {
            Number id = realm.where(CartItem.class).max("id");
            if (id != null) {
                return id.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hotMenuPicture)
        ImageView hotMenuPicture;
        @BindView(R.id.hotMenuName)
        TextView hotMenuName;
        @BindView(R.id.hotMenuPrice)
        TextView hotMenuPrice;
        @BindView(R.id.detailHotMenuPicture)
        ImageView detailHotMenuPicture;
        @BindView(R.id.detailHotMenuName)
        TextView detailHotMenuName;
        @BindView(R.id.detailHotMenuPrice)
        TextView detailHotMenuPrice;
        @BindView(R.id.detailHotMenuDescritption)
        TextView detailHotMenuDescription;
        @BindView(R.id.detailHotMenuRating)
        TextView detailHotMenuRating;
        @BindView(R.id.detailHotMenuReview)
        TextView detailHotMenuReview;
        @BindView(R.id.detailHotMenuCart)
        Button detailHotMenuCart;
        @BindView(R.id.detailHotMenuOrder)
        Button detailHotMenuOrder;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }


}
