package com.example.fooddeuk.order_history

import com.example.fooddeuk.cart.model.SelectedOption

/**
 * Created by heo on 2018. 3. 9..
 */

data class OrderMenuItem(var menu_id : String,var name : String, var price : String, var menu_count : String, var avgtime : String, var totalPrice : String, var selUnNecessaryOptionListPost : ArrayList<SelectedOption>,var selNecessaryOptionListPost : ArrayList<SelectedOption>)
