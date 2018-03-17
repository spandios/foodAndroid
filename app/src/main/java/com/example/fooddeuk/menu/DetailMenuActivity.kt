package com.example.fooddeuk.menu

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.custom.ImageVPAdapter
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.review.ReviewAdapter
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.setting
import com.example.fooddeuk.util.toJustWon
import com.example.fooddeuk.util.toPx
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_menu.*


class DetailMenuActivity : AppCompatActivity() {
    private lateinit var menu: Menu
    private var toolbarHeight = 0
    private var nameHeight = 0
    private var isDangol = false
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mGestureListener: View.OnTouchListener

    private val cart by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_cart)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }
    private val heart by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_heart)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }
    private val backArrow by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_back_black)?.apply { setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_menu)
        mGestureDetector = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onShowPress(e: MotionEvent?) {

            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {

            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//                parentlinear.translationY=(-1*distanceY)
                Logger.d(e1?.y)
                Logger.d(e2?.y)
                return true
            }
        })

        mGestureListener = View.OnTouchListener { v, event ->
            if (mGestureDetector.onTouchEvent(event)) {
                return@OnTouchListener false
            }
            if (event.action == MotionEvent.ACTION_UP) {
                Logger.d(event.y)
            }
            false
        }

        scroll_menu_detail.setOnTouchListener(mGestureListener)

        RxBus.intentSubscribe(RxBus.DetailMenuActivityData, this.javaClass, Consumer {
            if (it is Menu) {
                menu = it
                settingMenuLayout()
            }
        })

    }

    private fun settingMenuLayout() {
        menu_detail_back.setOnClickListener { finish() }
        vp_menu_detail_image.adapter = ImageVPAdapter(this, menu.picture)
        vp_menu_detail_image_indicator.setViewPager(vp_menu_detail_image)

        menu_detail_distance.text = menu.distance
        menu_detail_name.text = menu.name
        menu_detail_name_in_list.text = menu.name
        menu_detail_price.text = menu.price.toJustWon()
        menu_detail_description.text = menu.description
        img_detail_rating.rating = menu.rating.toFloat()
        menu_detail_reviewcount.text = Util.stringFormat(this, R.string.rest_reviewcnt, menu.reviewCnt.toString())

//        menu_detail_rating.text= Util.stringFormat(this,R.string.rest_rating_and_review_cnt,menu.rating.toString(),menu.reviewCnt.toString())

        HTTP.single(HTTP.httpService.getReview(menu.menu_id)).subscribe({
            menu_detail_review.setting(ReviewAdapter(this, it.result, true), false, false, true)

        }, {
            it.printStackTrace()
        })

        val mGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //리스너삭제
                scroll_menu_detail.viewTreeObserver.removeOnGlobalLayoutListener(this)
                toolbarHeight = vp_menu_detail_image.height - header.height
                nameHeight = menu_detail_name_in_list.measuredHeight + 24.toPx + toolbarHeight


            }
        }


        scroll_menu_detail.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

        menu_detail_back.setImageDrawable(backArrow)
        menu_detail_heart.setImageDrawable(heart)
        menu_detail_cart.setImageDrawable(cart)

        scroll_menu_detail.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            var toolbarIconAlpha = ((Math.min(1f, scrollY.toFloat() / toolbarHeight)) * 255).toInt()

            if (nameHeight > scrollY) {
                menu_detail_name.visibility = View.GONE
            } else {
                menu_detail_name.visibility = View.VISIBLE
            }

            when (toolbarIconAlpha) {
                in 0..130 -> {
                    setIconChangeColor(R.color.white)
                }
                in 130..160 -> {

                    setIconChangeColor(R.color.white_1)
                }
                in 161..190 -> {
                    setIconChangeColor(R.color.white_2)
                }
                in 190..255 -> {
                    setIconChangeColor(R.color.black)
                }
            }

            header.background.alpha = toolbarIconAlpha
        }
    }

//    private fun isDangol() {
//        user?.let {
//            it.rest_id.forEach {
//                if (it == menu.men) {
//                    isDangol = true
//                    rest_detail_heart?.setColorFilter(ContextCompat.getColor(this@DetailRestaurantActivity, R.color.heart), PorterDuff.Mode.SRC_ATOP)
//                    return
//                }
//            }
//        }
//    }

    private fun setIconChangeColor(colorResource: Int) {
        backArrow?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
        cart?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
        if (!isDangol) {
            heart?.setColorFilter(ContextCompat.getColor(this, colorResource), PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this.javaClass)
    }
}
