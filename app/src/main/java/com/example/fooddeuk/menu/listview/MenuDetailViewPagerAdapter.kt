package com.example.fooddeuk.menu.listview

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import at.blogc.android.views.ExpandableTextView
import com.example.fooddeuk.R
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.review.MenuReview
import com.example.fooddeuk.util.gone
import com.example.fooddeuk.util.visible
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


/**
 * Created by heo on 2018. 2. 22..
 */

class MenuDetailViewPagerAdapter(var context: Context, val menu: Menu, val menuReivew: ArrayList<MenuReview>, val noPicture: Boolean = false) : PagerAdapter() {
    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var vpCount = 0
    private var pictureSize = 0
    private var reviewSize = 0

    init {
        pictureSize = menu.picture.size
        reviewSize = menuReivew.size
        when (pictureSize) {
            0 -> {
                if (menuReivew.size > 8) {
                    reviewSize = 8
                }
            }
            1 -> {
                if (menuReivew.size > 7) {
                    reviewSize = 7
                }
            }
            2 -> {
                if (menuReivew.size > 6) {
                    reviewSize = 6
                }
            }
            3 -> {
                if (menuReivew.size > 5) {
                    reviewSize = 5
                }
            }
        }

        vpCount = pictureSize + reviewSize
        if (vpCount > 8) {
            vpCount = 8
        }
    }

    override fun getCount(): Int = vpCount

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return when (pictureSize) {
            0 -> {
                createReviewPage(position, container)
            }
            1 -> {
                if (position < 1) {
                    createPicturePage(position, container)
                } else {
                    createReviewPage(position, container, pictureSize)
                }
            }
            2 -> {
                if (position < 2) {
                    createPicturePage(position, container)
                } else {
                    createReviewPage(position, container, pictureSize)
                }
            }
            else -> {
                if (position < 3) {
                    createPicturePage(position, container)
                } else {
                    createReviewPage(position, container, pictureSize)
                }
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    private fun createPicturePage(position: Int, container: ViewGroup): View {
        val imageViewLayout = layoutInflater.inflate(R.layout.item_vp_menu_detail_image, container, false)
        Picasso.with(context).load(menu.picture[position]).fit().into(imageViewLayout.findViewById<ImageView>(R.id.layout_item_menu_detail_imageview))
        imageViewLayout.tag = "image-$position"
        container.addView(imageViewLayout)
        return imageViewLayout
    }

    private fun createReviewPage(position: Int, container: ViewGroup, pictureSize: Int = 0): View {
        val reviewItem = menuReivew[position - pictureSize]
        val reviewLayout = layoutInflater.inflate(R.layout.item_vp_menu_detail_review, container, false)
        val reviewContent = reviewLayout.findViewById<ExpandableTextView>(R.id.expandableTextView).apply {
            text = reviewItem.content
            setInterpolator(OvershootInterpolator())
        }

        reviewLayout.tag = "review-$position"
        reviewLayout.findViewById<ImageView>(R.id.review_image).apply { Picasso.with(context).load(reviewItem.image[0]).fit().into(this) }
        reviewLayout.findViewById<ImageView>(R.id.review_user_image).apply { Picasso.with(context).load(R.drawable.fm).transform(CropCircleTransformation()).fit().into(this) }
        reviewLayout.findViewById<TextView>(R.id.button_toggle).apply {
            setOnClickListener({
                if (reviewContent.isExpanded) {
                    reviewContent.collapse()
                    this.text = "자세히보기"
                } else {
                    reviewContent.expand()
                    this.text = "접기"
                }
            })
            if(reviewItem.content.length>80){
                this.visible()
            }else{
                this.gone()
            }
        }

        container.addView(reviewLayout)
        return reviewLayout
    }
}
