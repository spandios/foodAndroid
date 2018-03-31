package com.example.fooddeuk.custom

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fooddeuk.R
import com.example.fooddeuk.util.toPx


/**
 * Created by heo on 2018. 2. 19..
 */

class CustomFilterDialog internal constructor(internal var builder: Builder) {
    var materialDialog: MaterialDialog

    init {
        //다이아로그 셋팅
        materialDialog = MaterialDialog.Builder(builder.context).customView(builder, false).build()
        //dismiss
        builder.img_filter_exit.setOnClickListener { materialDialog.hide() }

        builder.txt_filter_clear.setOnClickListener{
            builder.clear()
        }
    }


    fun show() {
        materialDialog.show()
    }
    fun hide(){
        materialDialog.hide()
    }

    fun dismiss(){
        materialDialog.dismiss()
    }

    class Builder(internal var context: Context) : LinearLayout(context) {
        lateinit var img_filter_exit: ImageView
        lateinit var txt_filter_clear: TextView
        lateinit var filterParentLayout: LinearLayout
        private var isClearText: Boolean = true
        private var filterSelectColor: Int = 0
        private var filterUnSelectColor: Int = 0
        private var contentTextSize = 16f
        private var titleTextSize = 18f
        private var verticalTextGravity = Gravity.LEFT
        private var contentLeftMargin = 24
        private var contentTopMargin = 24
        private var isFirtSelectColor = true
        private var contentTextTypeface = Typeface.DEFAULT

        lateinit var clearCallback: () -> Unit

        init {
            initView()
        }

        private fun initView() {
            View.inflate(getContext(), R.layout.custom_dialog_filter, this)
            img_filter_exit = findViewById(R.id.img_filter_exit)
            txt_filter_clear = findViewById(R.id.txt_filter_clear)
            filterParentLayout = findViewById(R.id.layout_sort_content)
            filterSelectColor = context.resources.getColor(R.color.tangerine)
            filterUnSelectColor = context.resources.getColor(R.color.text_sub)

        }

        fun build(): CustomFilterDialog {
            return CustomFilterDialog(this)
        }


        fun setFilter(filterContentData: ArrayList<String>, contentClickListener: (position: Int, contentTextView: TextView) -> Unit): Builder {
            filterParentLayout.addView(getParentLayout().apply {
                //title
                (getChildAt(0) as LinearLayout).addView(getBasicTitle(filterContentData[0]))
                //content
                (getChildAt(1) as LinearLayout).addView(contentFilter(filterContentData, contentClickListener))
            })

            return this
        }

        fun setSelectedColor(color : Int){
            this.filterSelectColor=color
        }

        fun setUnSelectedColor(color: Int){
            this.filterUnSelectColor=color
        }

        fun setContentTextSize(size : Float) : Builder {
            contentTextSize =size
            return this
        }

        fun setTitleTextSize(size : Float) : Builder {
            titleTextSize=size
            return this
        }

        fun contentGravity(gravity : Int) : Builder {
            verticalTextGravity=gravity
            if(gravity==Gravity.CENTER){
                contentLeftMargin=0
            }
            return this
        }

        fun contentLeftMargin(size : Int) : Builder {
            contentLeftMargin=size
            return this
        }

        fun contentTopMargin(size : Int) : Builder {
            contentTopMargin=size
            return this
        }

        fun isFirstSelectColor(flag : Boolean): Builder {
            isFirtSelectColor=flag
            return this
        }

        fun contentTypeFace(typeface: Typeface) : Builder {
            contentTextTypeface=typeface
            return this
        }



        private fun backToDefaultText(parentLayout : LinearLayout,clear : Boolean = false){

            for (k in 0 until parentLayout.childCount) {
                with((parentLayout.getChildAt(k) as TextView)){
                    setTextColor(filterUnSelectColor)
                    typeface = Typeface.DEFAULT
                    if(clear){
                        if(k==0){
                            setTextColor(filterSelectColor)
                        }
                        if(k==parentLayout.childCount-1){
                            clearCallback()
                        }
                    }

                }
            }
        }


        fun clear(){
            for(i in 0 until filterParentLayout.childCount){
                val parent = filterParentLayout.getChildAt(i) as LinearLayout
                val contentParentLayout = parent.getChildAt(1) as LinearLayout
                val contentLayout=contentParentLayout.getChildAt(0) as LinearLayout
                backToDefaultText(contentLayout,true)
            }
        }




        fun isClearText(clearText: Boolean): Builder {
            isClearText = clearText
            if (!isClearText) {
                txt_filter_clear.visibility = View.GONE
            }
            return this
        }

        fun SetClearCallback(callback : ()->Unit) : Builder {
            this.clearCallback=callback
            return this
        }




        private fun contentFilter(filterContentData: ArrayList<String>, contentClickListener: (position: Int, contentTextView: TextView) -> Unit): LinearLayout {
            val isVertical = filterContentData[1] == "vertical"
            val contentLayout = LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                orientation = if (isVertical) VERTICAL else HORIZONTAL
            }

            for (i in 2 until filterContentData.size) {
                contentLayout.addView(TextView(context).apply {
                    textSize = contentTextSize
                    if (isVertical) {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                            gravity = verticalTextGravity
                            setMargins(contentLeftMargin.toPx, contentTopMargin.toPx, 0, 0)
                        }
                    } else {
                        for (i in 0 until filterContentData.size - 2) {
                            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                                setMargins(contentLeftMargin.toPx, contentTopMargin.toPx, 0, 0)
                            }

                        }
                    }
                    setTextColor(filterUnSelectColor)

                    //처음텍스트색상
                    if(isFirtSelectColor){
                        if (i == 2) {
                            setTextColor(filterSelectColor)
                        }
                    }


                    text = filterContentData[i]
                    typeface=contentTextTypeface

                    setOnClickListener({
                        backToDefaultText(contentLayout)
                        setTextColor(filterSelectColor)
                        typeface = Typeface.DEFAULT_BOLD

                        contentClickListener(i - 2, this@apply)
                    })
                })
            }

            return contentLayout
        }

        private fun getParentLayout(): LinearLayout {
            return LinearLayout(context).apply {
                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.bottomMargin = (24.toPx)
                setLayoutParams(layoutParams)
                orientation = VERTICAL

                val titleLayout = LinearLayout(context).apply { gravity = Gravity.CENTER }
                val contentLayout = LinearLayout(context)
                addView(titleLayout)
                addView(contentLayout)
            }
        }

        private fun getBasicTitle(filterName: String): TextView {
            return TextView(context).apply {
                text = filterName
                textSize = titleTextSize
                setTypeface(null, Typeface.BOLD)
            }
        }


    }


}


