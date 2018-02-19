package com.example.fooddeuk.util

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
        var isClearText: Boolean = true
        var filterSelectColor: Int = 0
        var filterUnSelectColor: Int = 0
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




        fun isSetClearText(clearText: Boolean): Builder {
            isClearText = clearText
            if (!isClearText) {
                txt_filter_clear.visibility = View.GONE
            }
            return this
        }

        fun SetClearCallback(callback : ()->Unit) : Builder{
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
                    textSize = 16f
                    if (isVertical) {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                            gravity = Gravity.LEFT
                            setMargins(24.toPx, 24.toPx, 0, 0)
                        }
                    } else {
                        for (i in 0 until filterContentData.size - 2) {
                            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                                setMargins(24.toPx, 24.toPx, 0, 0)
                            }

                        }
                    }
                    setTextColor(filterUnSelectColor)

                    if (i == 2) {
                        setTextColor(filterSelectColor)
                    }

                    text = filterContentData[i]

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
                textSize = 22f
                setTypeface(null, Typeface.BOLD)
            }
        }


    }


}


