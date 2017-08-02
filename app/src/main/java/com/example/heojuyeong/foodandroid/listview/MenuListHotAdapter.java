package com.example.heojuyeong.foodandroid.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.heojuyeong.foodandroid.R;
import com.example.heojuyeong.foodandroid.item.MenuItem;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuListHotAdapter extends RecyclerView.Adapter<MenuListHotAdapter.ViewHolder>{
    private ArrayList<MenuItem> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;


    public interface  OnItemClickListener{
         void onItemClick(View view, int position);
    }


   public MenuListHotAdapter(Context context,ArrayList<MenuItem> items,OnItemClickListener onItemClickListener){
        this.items=items;
        this.context=context;
       this.mOnItemClickListener=onItemClickListener;
   }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_listview_hot_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        final ViewHolder vh = new ViewHolder(v);

        return vh;
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            MenuItem menuItem=items.get(position);
            Picasso.with(context).load(menuItem.getMenupicture()).fit().into(holder.hotMenuPicture);
            holder.hotMenuName.setText(menuItem.getName());
            holder.hotMenuRating.setText(menuItem.getRating());
            holder.hotMenuPrice.setText(menuItem.getPrice());

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                        holder.detailHotMenu.setVisibility(View.VISIBLE);
//                        holder.masterHotMenu.setVisibility(View.GONE);
//                        clickFlag=true;
//
//                }
//            });
//
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());


                }
            });



    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView hotMenuPicture;
        private TextView hotMenuName;
        private TextView hotMenuRating;
        private TextView hotMenuPrice;
        private LinearLayout masterHotMenu;
        private LinearLayout detailHotMenu;
//        private TextView hotMenuAvgTime;


        public ViewHolder(View itemView){
            super(itemView);
            hotMenuPicture=(ImageView)itemView.findViewById(R.id.hotMenuPicture);
            hotMenuName=(TextView)itemView.findViewById(R.id.hotMenuName);
            hotMenuRating=(TextView)itemView.findViewById(R.id.hotMenuRating);
            hotMenuPrice=(TextView)itemView.findViewById(R.id.hotMenuPrice);
            masterHotMenu=(LinearLayout)itemView.findViewById(R.id.masterHotMenu);
            detailHotMenu=(LinearLayout)itemView.findViewById(R.id.detailHotMenu);
        }




    }


}
