package com.example.fooddeuk.option

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.addPlus
import kotlinx.android.synthetic.main.item_option.view.*

/**
 * Created by heo on 2018. 3. 2..
 */
class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun necessaryBindOption(option: CartOption, position: Int, optionToggleCallback: (position: Int)->Unit) {
        with(itemView) {
            txt_option_content.text = option.menu_option_name
            txt_option_price.text = option.menu_option_price.addPlus()
            tag = option
            if (position == 0) {
                isSelected = !isSelected
                img_option_radio.isSelected = isSelected
                txt_option_content.isSelected=isSelected
            }
            img_option_radio.visibility = View.VISIBLE
            setOnClickListener {
                optionToggleCallback(position)
                RxBus.publish(RxBus.SelectedOptionPrice, true)
            }
        }
    }

    fun unnecessaryBindOption(option: CartOption){
        with(itemView) {
            txt_option_content.text = option.menu_option_name
            txt_option_price.text = option.menu_option_price.addPlus()
            tag = option
            img_option_checkbox.visibility = View.VISIBLE
            setOnClickListener {
                isSelected = !isSelected
                (it.img_option_checkbox).isSelected = isSelected
                txt_option_content.isSelected=isSelected
                RxBus.publish(RxBus.SelectedOptionPrice, false)
            }
        }
    }
}