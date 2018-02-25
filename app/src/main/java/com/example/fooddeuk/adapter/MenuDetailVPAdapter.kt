package com.example.fooddeuk.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.fooddeuk.R
import com.example.fooddeuk.listview.review.ReviewAdapter
import com.example.fooddeuk.model.menu.Menu
import com.example.fooddeuk.model.review.MenuReview
import com.example.fooddeuk.util.LayoutUtil
import com.squareup.picasso.Picasso


/**
 * Created by heo on 2018. 2. 22..
 */

class MenuDetailVPAdapter(var context: Context,  val menu: Menu,val menuReivew : ArrayList<MenuReview>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var vpCount = 0

    init {
        vpCount=menu.picture.size + 1
    }

    override fun getCount(): Int = vpCount

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position < vpCount - 1) {

            val imageViewLayout = layoutInflater.inflate(R.layout.item_vp_menu_detail_image, container, false)
            Picasso.with(context).load(menu.picture[position]).fit().into(imageViewLayout.findViewById<ImageView>(R.id.layout_item_menu_detail_imageview))
            container.addView(imageViewLayout)
            return imageViewLayout
        } else {
            val reviewLayout = layoutInflater.inflate(R.layout.item_vp_menu_detail_review, container, false)
            val reviewRecyclerView = reviewLayout.findViewById<RecyclerView>(R.id.recycle_item_menu_detail_review)
            val moreDetailReviewLayout = reviewLayout.findViewById<LinearLayout>(R.id.layout_more_review)
            LayoutUtil.RecyclerViewSetting(context, reviewRecyclerView)
            reviewRecyclerView.adapter = ReviewAdapter(context, menuReivew, true,moreDetailReviewLayout)

            container.addView(reviewLayout)
            return reviewLayout
        }
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}