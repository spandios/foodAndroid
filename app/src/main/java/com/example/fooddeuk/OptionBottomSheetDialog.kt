package com.example.fooddeuk

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.example.fooddeuk.menu.option.OptionCategoryAdapter
import com.example.fooddeuk.model.cart.CartOption
import com.example.fooddeuk.model.cart.CartOptionCategory
import com.example.fooddeuk.model.menu.Option
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.PriceUtil
import io.realm.RealmList

/**
 * Created by IfamilySC on 2018-03-01.
 */

class OptionBottomSheetDialog(context: Context,var optionCategoryList: ArrayList<CartOptionCategory>,val cart_menu_result_price : TextView, var confirmCallback: (optionList : RealmList<CartOption>, totalPrice : String)->Unit) : BottomSheetDialog(context) {

    val parentView: LinearLayout = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null) as LinearLayout
    val optionListView : RecyclerView =parentView.findViewById(R.id.dialog_menu_option_listview)
    val dialog_totalPrice : TextView = parentView.findViewById(R.id.dialog_totalPrice)
    val optionConfirmButton : Button = parentView.findViewById(R.id.optionConfirmButton)

    var optionList : RealmList<CartOption> = RealmList()

    init {

        setContentView(parentView)

    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)

        optionConfirmButton.setOnClickListener {
            for (i in 0 until optionListView.childCount) {
                val categoryView = optionListView.getChildAt(i)

                //CategoryName
                val optionContentList = categoryView.findViewById<LinearLayout>(R.id.option_content_list)

                for (j in 0 until optionContentList.childCount) {
                    val optionView = optionContentList.getChildAt(j)
                    if (optionView.findViewById<View>(R.id.option_radio) != null) {
                        val radioOption = optionView.findViewById<RadioButton>(R.id.option_radio)
                        if (radioOption.isChecked) {
                            //선택된옵션 리스트에 추가
                            optionList.add(CartOption(radioOption.tag as Option))
                        }
                    }
                }
            }
            confirmCallback(optionList,dialog_totalPrice.text.toString())
        }

        setOptionRecyclerView()


    }

    private fun setOptionRecyclerView() {
        LayoutUtil.RecyclerViewSetting(context, optionListView)
        dialog_totalPrice.text = cart_menu_result_price.text


        val selectCLickListener = { isPlus : String, price : String ->
            dialog_totalPrice.text = if (isPlus == "plus")
                PriceUtil.plusPrice(price, PriceUtil.TextViewToString(dialog_totalPrice))
            else
                PriceUtil.minusPrice(price, PriceUtil.TextViewToString(dialog_totalPrice))
        }

        val radioPriceListener = { plusPrice : String, minusPrice : String ->
            val price = PriceUtil.getOriginalPrice(plusPrice) - PriceUtil.getOriginalPrice(minusPrice) + PriceUtil.getOriginalPrice(dialog_totalPrice.text.toString())
            dialog_totalPrice.text = PriceUtil.comma_won(price)
        }

        optionListView.adapter = OptionCategoryAdapter(context, optionCategoryList,selectCLickListener, radioPriceListener)


    }

}
