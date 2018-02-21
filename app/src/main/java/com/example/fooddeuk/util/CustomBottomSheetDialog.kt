package com.example.fooddeuk.util

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.View

/**
 * Created by heo on 2018. 2. 20..
 */

class CustomBottomSheetDialog(context: Context) : BottomSheetDialog(context) {
    lateinit var bottomSheetView: View

    override fun setContentView(view: View) {
        super.setContentView(view)
    }

    override fun setContentView(layoutResId: Int) {
        super.setContentView(layoutResId)
    }

}
