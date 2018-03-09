package com.example.fooddeuk.order.model

import com.example.fooddeuk.cart.model.SelectedOption
import com.example.fooddeuk.option.Option

/**
 * Created by heo on 2018. 3. 3..
 */
class PostSelectedOption(selectedOption: SelectedOption){
    var categoryName: String = selectedOption.categoryName
    var selectedOptionList : ArrayList<Option> = ArrayList()
    init {
        selectedOption.selectedOptionList.forEach {
            selectedOptionList.add(Option(it.menu_option_id,it.menu_option_name,it.menu_option_price))
        }
    }
}