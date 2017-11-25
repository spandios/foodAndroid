package com.example.fooddeuk.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddeuk.R;
import com.example.fooddeuk.adapter.MenuListAdapter;
import com.example.fooddeuk.fragment.HomeFragment;
import com.example.fooddeuk.fragment.RestMenuCategoryFragment;
import com.example.fooddeuk.model.restaurant.RestaurantItem;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class DetailRestaurantActivity extends AppCompatActivity implements MenuListAdapter.OnItemClickListener,MenuListAdapter.OnCartCountClickListener, View.OnClickListener {

    RestaurantItem.Restaurant restaurant;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;




//    @BindView(R.id.rest_detail_back_btn)
//    Button rest_detail_back_btn;


    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rest_detail_heart)
    ImageView rest_detail_heart;

    @BindView(R.id.rest_detail_image_viewpager)
    ViewPager rest_detail_image_viewpager;

    @BindView(R.id.rest_detail_image_viewpager_indicator)
    CircleIndicator rest_detail_image_viewpager_indicator;

    @BindView(R.id.rest_main_tab)
    FrameLayout rest_main_tab;
//    @BindView(R.id.rest_detail_image)
//    ImageView rest_detail_image;
    //이름
//    @BindView(R.id.rest_detail_name)
//    TextView rest_detail_name;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;


    //평점
    @BindView(R.id.rest_detail_rating)
    TextView rest_detail_rating;


    //별
//    @BindView(R.id.rest_detail_rating_star_layout)
//    LinearLayout rest_detail_rating_star_layout;

    @BindView(R.id.rest_detail_main_tab)
    TabLayout tabLayout;

    //TabLayout
//    @BindView(R.id.rest_detail_main_tab)
//    TabLayout rest_detail_main_tab;

    //Menu ViewPager
//    @BindView(R.id.rest_detail_main_tab_viewpager)
//    MainTabViewPager rest_detail_main_tab_viewpager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        ButterKnife.bind(this);

        //Parcel로 선택된 식당 정보 가져옴
        /**from CurLocationRestaurantFragment**/
        /**from CurLocationRestaurantFragment**/


        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
//        starDpMap=LayoutUtil.DpToLayoutParams(getApplicationContext(), 12,11);
        String[] temp=new String[2];
        temp[0]="https://fooddeuk.s3.ap-northeast-2.amazonaws.com/1501404292757_images.png";
        temp[1]="https://s3.ap-northeast-2.amazonaws.com/fooddeuk/1500703066568_%E1%84%8B%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%85%E1%85%B5%E1%86%AB.jpeg";
        RestDetailImageViewpager restDetailImageViewpager=new RestDetailImageViewpager(this,temp);
        rest_detail_image_viewpager.setAdapter(restDetailImageViewpager);
        rest_detail_image_viewpager_indicator.setViewPager(rest_detail_image_viewpager);

        viewSetting();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rest_main_tab, RestMenuCategoryFragment.newInstance(restaurant.rest_id));
        fragmentTransaction.commit();



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
// get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = RestMenuCategoryFragment.newInstance(restaurant.rest_id);
                        break;
                    case 1:
                        fragment = new HomeFragment();
                        break;
                    case 2:
                        fragment = RestMenuCategoryFragment.newInstance(restaurant.rest_id);
                        break;
                }

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.rest_main_tab, fragment);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



    }


    //Implement CartAddClick -> 메뉴 추가 한 뒤 장바구니 개수 새로고침
    @Override
    public void onCartCount(int cartSize) {
//        rest_detail_cart_count.setText(String.valueOf(cartSize));
    }

    //TODO 더 안정화
    //Implement ItemClick -> 해당 position의 상세 레이아웃 펼쳐짐
    @Override
    public void onItemClick(RecyclerView.ViewHolder view, float y,RecyclerView recyclerView) {
        Logger.d(view.itemView.getY());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rest_detail_back_btn:
//                finish();
//                break;

//

//            case R.id.detailTopCartButton:
//                IntentUtil.startActivity(this, CartActivity.class);
//                break;
//            case R.id.rest_detail_cart_btn:
//                IntentUtil.startActivity(this, CartActivity.class);
//                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        rest_detail_cart_count.setText(String.valueOf(RealmUtil.getDataSize(CartItem.class)));

    }


    private void viewSetting() {
        //MenuList

//        rest_detail_name.setText(restaurant.getName());
        setSupportActionBar(toolbar);
        final Drawable backArrow = getResources().getDrawable(R.drawable.ic_back_black);
        final Drawable heart = getResources().getDrawable(R.drawable.ic_heart);

        backArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        heart.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(backArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rest_detail_heart.setImageDrawable(heart);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //collapse
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                backArrow.setColorFilter(getResources().getColor(R.color.charcoal_grey), PorterDuff.Mode.SRC_ATOP);
                heart.setColorFilter(getResources().getColor(R.color.charcoal_grey), PorterDuff.Mode.SRC_ATOP);



            } else if (verticalOffset == 0) {
                backArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                heart.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            } else {
                // Somewhere in between
            }
//                int toolBarHeight = toolbar.getMeasuredHeight();
//                int appBarHeight = appBarLayout.getMeasuredHeight();
//                Float f = ((((float) appBarHeight - toolBarHeight) + verticalOffset) / ( (float) appBarHeight - toolBarHeight)) * 255;
////                toolbar.getBackground().setAlpha(255 - Math.round(f));
//                toolbar.setAlpha(255 - Math.round(f));
        });

//        Picasso.with(this).load(restaurant.rest_picture).fit().into(rest_detail_image);
        rest_detail_rating.setText(Double.toString(restaurant.getRating()));
        collapsingToolbarLayout.setTitle(restaurant.name);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        int bottomPx = 180; // margin in dips
        float d = getResources().getDisplayMetrics().density;
        int margin = (int)(bottomPx * d);
        collapsingToolbarLayout.setExpandedTitleMarginBottom(margin);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);


//        rest_detail_back_btn.setOnClickListener(this);




    }


//    private void setStarView(float rating, LinearLayout rest_detail_rating_star_layout) {
//        for (int j = 0; j < 5; j++) {
//            ImageView star = new ImageView(getApplicationContext());
//            star.setLayoutParams(new ViewGroup.LayoutParams(starDpMap.get("width"), starDpMap.get("height")));
//            if (rating == 0) {
//                star.setImageResource(R.drawable.ic_star);
//                rest_detail_rating_star_layout.addView(star);
//
//            } else if (rating >= 1.0) {
//                rating -= 1.0;
//                star.setImageResource(R.drawable.ic_star_ranked);
//                rest_detail_rating_star_layout.addView(star);
//
//            } else if (rating == 0.5) {
//                rating -= 0.5;
//                star.setImageResource(R.drawable.ic_star_ranked_half);
//                rest_detail_rating_star_layout.addView(star);
//            }
//        }
//    }


//    public class fragmentPagerAdapter extends FragmentPagerAdapter {
//
//        public fragmentPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
////        @Override
////        public int getItemPosition(Object object) {
////            return POSITION_NONE;
////        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                   return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
//                case 1:
//                    return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
//                case 2:
//                    return RestMenuCategoryFragment.newInstance(restaurant.rest_id);
//
//            }
//            return null;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "메뉴";
//                case 1:
//                    return "메뉴판";
//                case 2:
//                    return "정보";
//
//                default:return null;
//            }
//        }
//    }

    public class RestDetailImageViewpager extends PagerAdapter {
        Context context;
        String images[];
        LayoutInflater layoutInflater;


        public RestDetailImageViewpager(Context context, String images[]) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.item_rest_detail_image_viewpager, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.rest_detail_image);

            Picasso.with(context).load(images[position]).fit().into(imageView);
            container.addView(itemView);

            //listening to image click
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }




}
