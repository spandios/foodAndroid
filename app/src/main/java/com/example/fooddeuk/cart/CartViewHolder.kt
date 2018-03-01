package com.example.fooddeuk.cart

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.OptionBottomSheetDialog
import com.example.fooddeuk.model.cart.CartItem
import com.example.fooddeuk.model.cart.CartOption
import com.example.fooddeuk.model.cart.CartOptionCategory
import com.example.fooddeuk.util.PriceUtil
import com.jakewharton.rxbinding2.view.RxView
import com.orhanobut.logger.Logger
import io.realm.RealmList
import kotlinx.android.synthetic.main.item_cart.view.*

class CartViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var cart_menu_quantity=1
    private val optionDialogArray = arrayOfNulls<OptionBottomSheetDialog>(2)
    private var necessaryOptionCategoryList = ArrayList<CartOptionCategory>()
    private var unNecessaryOptionCategoryList = ArrayList<CartOptionCategory>()
    private var optionList: RealmList<CartOption> = RealmList()

    init {
        itemView.setBackgroundColor(Color.WHITE)
    }


    //옵션 텍스트 뷰 추가
    //옵션카테고리:옵션이름 , 옵션 총 가격 계산
//            for(i in cartItemoption.indices){
//
//                optionPrice+= PriceUtil.getOriginalPrice(cartItemoption[i].menu_option_price)
//                val optionCateAndName=TextView(mContext)
//                optionCateAndName.text=cartItemoption[i].menu_option_name
//                cart_menu_option_layout.addView(optionCateAndName)
//            }


    fun bind(cartItem: CartItem) {
        var optionPrice = 0
        with(itemView) {
            cart_menu_name.text = cartItem.menu.name
            cart_menu_price.text = cartItem.menu.price + "원"
            layout_cart_menu_necessary_option.setOnClickListener { optionDialogArray[0]?.show() }
            //수량
            cart_menu_quantity.text = cart_menu_quantity.toString()

            //총 옵션 가격
//            cart_menu_option_price.text= PriceUtil.comma_won(optionPrice)

            //메뉴가격 + 옵션가격 = 총 가격
            cart_menu_result_price.text = PriceUtil.plusPrice(PriceUtil.TextViewToString(cart_menu_price), optionPrice)

            //옵션이 있다면
            if (cartItem.optionCategoryList.isNotEmpty()) {
                layout_parent_cart_option.visibility = View.VISIBLE

                checkOptionAndInsert(cartItem.optionCategoryList)

                //necessary dialog
                if (necessaryOptionCategoryList.isNotEmpty()) {
                    layout_cart_menu_necessary_option.visibility = View.VISIBLE

                    //category name
                    setBasicNecessaryOption(necessaryOptionCategoryList)
                    //dialog
                    setNecessaryOptionDialog(necessaryOptionCategoryList)

                }
                //unnecessary dialog
                if(unNecessaryOptionCategoryList.isNotEmpty()){
                    layout_cart_menu_unnecessary_option.visibility = View.VISIBLE
                    setNecessaryOptionDialog(necessaryOptionCategoryList)
                }
            }



            //수량 -
            RxView.clicks(cart_menu_minus)
                    .map { Integer.parseInt(cart_menu_quantity.text.toString()) }
                    .filter { value -> value > 1 }
                    .map { value -> value - 1 }
                    .subscribe { value ->
                        cart_menu_quantity.text = value.toString()
//                        cart_menu_price.text = PriceUtil.minusPrice(PriceUtil.TextViewToString(cart_menu_price),cartItemmenuCategory.menu_price)
                        cart_menu_result_price.text = PriceUtil.minusPrice(PriceUtil.TextViewToString(cart_menu_result_price), cartItem.menu.price)

                    }

            //수량+
            RxView.clicks(cart_menu_plus)
                    .map { Integer.parseInt(cart_menu_quantity.text.toString()) }
                    .filter { value -> value < 100 }
                    .map { value -> value + 1 }
                    .subscribe { value ->
                        cart_menu_quantity.text = value.toString()
//                        cart_menu_price.text = PriceUtil.plusPrice(PriceUtil.TextViewToString(cart_menu_price),cartItemmenuCategory.menu_price)
                        cart_menu_result_price.text = PriceUtil.plusPrice(PriceUtil.TextViewToString(cart_menu_result_price), cartItem.menu.price)
                    }
        }

    }

    //필수,선택 옵션 구분해서 list에 추가
    private fun checkOptionAndInsert(optionCategoryList: RealmList<CartOptionCategory>) {
        for (optionList in optionCategoryList) {
            //필수
            if (!optionList.multiple) {
                necessaryOptionCategoryList.add(optionList)
            }
            //선택
            else {
                unNecessaryOptionCategoryList.add(optionList)
            }
        }
    }

    //필수옵션은 제일 처음 옵션을 보여줌과 동시에 리스트에도 기본적으로 추가 한다.
    private fun setBasicNecessaryOption(optionCategoryList: ArrayList<CartOptionCategory>){
        if(optionCategoryList.size==1){
            itemView.cart_option_necessary_title.text=optionCategoryList[0].menu_option_category_name
            itemView.txt_cart_option_necessary_content.text=optionCategoryList[0].necessary[0].menu_option_name
            optionList.add(optionCategoryList[0].necessary[0])
            return
        }

        val necessaryCategoryText = StringBuffer("")
        val necessaryContentText = StringBuffer("")
        Logger.d(optionCategoryList.size)
        for (i in optionCategoryList.indices) {
            val cartOptionCategory = optionCategoryList[i]
            if (cartOptionCategory.necessary.size > 0) {
                val necessaryBasicOption = cartOptionCategory.necessary[0]
                when {
                    i != optionCategoryList.size - 1 -> {
                        necessaryCategoryText.append(cartOptionCategory.menu_option_category_name + ",")
                        necessaryContentText.append(necessaryBasicOption.menu_option_name + ",")
                    }
                    else -> {
                        necessaryCategoryText.append(cartOptionCategory.menu_option_category_name)
                        necessaryContentText.append(necessaryBasicOption.menu_option_name)
                    }
                }

                optionList.add(necessaryBasicOption)
                Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가")
            }
        }

        itemView.cart_option_necessary_title.text=necessaryCategoryText
        itemView.txt_cart_option_necessary_content.text=necessaryContentText

    }

    private fun makeCommaText(){

    }

    private fun setNecessaryOptionDialog(optionCategory: ArrayList<CartOptionCategory>) {
        val necessaryOptionDialog = OptionBottomSheetDialog(context, optionCategory,itemView.cart_menu_result_price, { optionList, totalPrice ->
            this@CartViewHolder.optionList.clear()
            this@CartViewHolder.optionList.addAll(optionList)
            itemView.txt_cart_option_necessary_content.text = getSelectOption(optionList)
            optionDialogArray[0]?.let {
                it.dialog_totalPrice.text = totalPrice
                it.dismiss()
            }
        })
        optionDialogArray[0] = necessaryOptionDialog
        optionDialogArray[1] = null

    }

    private fun getSelectOption(optionArrayList: RealmList<CartOption>): String? {
        if (optionArrayList.size == 1) {
            return optionArrayList[0].menu_option_name
        } else if (optionArrayList.size > 1) {
            val s = StringBuffer()
            for (i in optionArrayList.indices) {
                if (i == optionArrayList.size - 1) {
                    s.append(optionArrayList[i].menu_option_name)
                    break
                }
                s.append(optionArrayList[i].menu_option_name + ",")

            }
            return s.toString()
        }
        return null
    }

}