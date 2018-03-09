package com.example.fooddeuk.option

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.util.LayoutUtil
import io.realm.RealmList
import kotlinx.android.synthetic.main.item_parent_option_list.view.*

/**
 * Created by heo on 2018. 3. 2..
 */

class CustomOptionList(context: Context, categoryTitle: String, optionList: RealmList<CartOption>, necessary: Boolean=true, isOrder : Boolean=false) : LinearLayout(context){


    init {
        LayoutInflater.from(context).inflate(R.layout.item_parent_option_list, this, true)
        txt_option_category.text=categoryTitle
        if(isOrder){
            txt_option_category.visibility= View.GONE
        }
        LayoutUtil.RecyclerViewSetting(context,recycle_option)
        recycle_option.isNestedScrollingEnabled=false
        recycle_option.setHasFixedSize(true)
        //OptionAdapter
        recycle_option.adapter=OptionAdapter(ArrayList<CartOption>().apply { addAll(optionList) },necessary,isOrder)
    }









}