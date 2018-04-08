package com.example.fooddeuk.cart

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.option.CustomOptionList
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.PriceUtil
import com.example.fooddeuk.util.getOriginalPrice
import com.example.fooddeuk.util.toCommaWon
import com.example.fooddeuk.util.toJustWon
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.functions.Consumer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cart.*

class CartViewHolder(val context: Context, override var containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private var cart_menu_quantity_val = 1


    init {
        itemView.setBackgroundColor(Color.WHITE)
    }


    private fun getSelectedOptionPrice(parentLayout: LinearLayout): Int {
        var price = 0
        if (parentLayout.childCount != 0) {
            for (i in 0 until parentLayout.childCount) {
                val necessaryList = parentLayout.getChildAt(i).findViewById<RecyclerView>(R.id.recycle_option)

                for (j in 0 until necessaryList.adapter.itemCount) {
                    val item = necessaryList.findViewHolderForAdapterPosition(j)
                    if (item.itemView.isSelected) {
                        price += (item.itemView.tag as CartOption).menu_option_price.toString().getOriginalPrice()
                    }
                }
            }

        }

        return price
    }


    fun bind(cartItem: CartItem) {
        cart_menu_name.text = cartItem.menu.name
        cart_menu_price.text = cartItem.menu.price + "원"
        cart_menu_quantity.text = cart_menu_quantity_val.toString()

        if (cartItem.menu.picture != null) {
            Picasso.with(context).load(cartItem.menu.picture).fit().into(cart_menu_picture)
        }

        RxBus.subscribe(RxBus.SelectedOptionPrice, this.javaClass, Consumer {
            //셀렉된 옵션 계산
            cart_menu_result_price.text =
                    (cart_menu_price.text.toString().getOriginalPrice()
                            + getSelectedOptionPrice(layout_cart_menu_necessary_option)
                            + getSelectedOptionPrice(layout_cart_menu_unnecessary_option)
                            ).toCommaWon()

            RxBus.publish(RxBus.CartResultPrice, true)
        })

        //옵션 리사이클러뷰 셋팅
        for (categoryList in cartItem.optionCategoryList) {

            val optionRecyclerView = CustomOptionList(context,
                    categoryList.menu_option_category_name, //MenuCategory Name
                    categoryList.option_content, //RealmList -> ArrayList CartOption
                    !categoryList.multiple //necessary
            )
            //필수 옵션 레이아웃에 추가
            if (!categoryList.multiple) {
                if (layout_cart_menu_necessary_option.visibility != View.VISIBLE) {
                    layout_cart_menu_necessary_option.visibility = View.VISIBLE
                }
                layout_cart_menu_necessary_option.addView(optionRecyclerView)
            } else {
                if (layout_cart_menu_unnecessary_option.visibility != View.VISIBLE) {
                    layout_cart_menu_unnecessary_option.visibility = View.VISIBLE
                }
                layout_cart_menu_unnecessary_option.addView(optionRecyclerView)
            }
        }

        //기본총 가격
        cart_menu_result_price.text = cartItem.menu.price.toJustWon()

        //수량 -
        RxView.clicks(cart_menu_minus)
                .map { Integer.parseInt(cart_menu_quantity.text.toString()) }
                .filter { value -> value > 1 }
                .map { value -> value - 1 }
                .subscribe { value ->
                    cart_menu_quantity.text = value.toString()
                    cart_menu_result_price.text = PriceUtil.minusPrice(cart_menu_result_price, cartItem.menu.price)
                    RxBus.publish(RxBus.CartResultPrice, true)
                }

        //수량+
        RxView.clicks(cart_menu_plus)
                .map { Integer.parseInt(cart_menu_quantity.text.toString()) }
                .filter { value -> value < 100 }
                .map { value -> value + 1 }
                .subscribe { value ->
                    cart_menu_quantity.text = value.toString()
                    cart_menu_result_price.text = PriceUtil.plusPrice(cart_menu_result_price, cartItem.menu.price)
                    RxBus.publish(RxBus.CartResultPrice, true)
                }
    }


    //필수옵션은 제일 처음 옵션을 보여줌과 동시에 리스트에도 기본적으로 추가 한다.
//    private fun setBasicNecessaryOption(optionCategoryList: ArrayList<CartOptionCategory>){
//        if(optionCategoryList.size==1){
//            itemView.cart_option_necessary_title.text=optionCategoryList[0].menu_option_category_name
//            itemView.txt_cart_option_necessary_content.text=optionCategoryList[0].necessary[0].menu_option_name
//            optionList.add(optionCategoryList[0].necessary[0])
//            return
//        }
//
//        val necessaryCategoryText = StringBuffer("")
//        val necessaryContentText = StringBuffer("")
//        Logger.d(optionCategoryList.size)
//        for (i in optionCategoryList.indices) {
//            val cartOptionCategory = optionCategoryList[i]
//            if (cartOptionCategory.necessary.size > 0) {
//                val necessaryBasicOption = cartOptionCategory.necessary[0]
//                when {
//                    i != optionCategoryList.size - 1 -> {
//                        necessaryCategoryText.append(cartOptionCategory.menu_option_category_name + ",")
//                        necessaryContentText.append(necessaryBasicOption.menu_option_name + ",")
//                    }
//                    else -> {
//                        necessaryCategoryText.append(cartOptionCategory.menu_option_category_name)
//                        necessaryContentText.append(necessaryBasicOption.menu_option_name)
//                    }
//                }
//
//                optionList.add(necessaryBasicOption)
//                Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가")
//            }
//        }
//
//        itemView.cart_option_necessary_title.text=necessaryCategoryText
//        itemView.txt_cart_option_necessary_content.text=necessaryContentText
//
//    }

//    private fun setNecessaryOptionDialog(optionCategory: ArrayList<CartOptionCategory>) {
//        val necessaryOptionDialog = OptionBottomSheetDialog(context, optionCategory, itemView.cart_menu_result_price, { optionList, totalPrice ->
//            this@CartViewHolder.optionList.clear()
//            this@CartViewHolder.optionList.addAll(optionList)
//            itemView.txt_cart_option_necessary_content.text = getSelectOption(optionList)
//            optionDialogArray[0]?.let {
//                it.dialog_totalPrice.text = totalPrice
//                it.dismiss()
//            }
//        })
//        optionDialogArray[0] = necessaryOptionDialog
//        optionDialogArray[1] = null
//
//    }

    

}