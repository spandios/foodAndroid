package com.example.fooddeuk.cart

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util.StringFormat
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.cart.model.SelectedOption
import com.example.fooddeuk.order.OrderActivity
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.*
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_parent_option_list.view.*

class CartFragment : android.support.v4.app.Fragment() {
    val realm: Realm = Realm.getDefaultInstance()
    private val cartItemList = ArrayList<CartItem>()
    lateinit var cartAdapter: CartAdapter
    var totalPrice = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_cart, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cart_menu_order.setOnClickListener {
            getFinalCartItem()
            StartActivity(OrderActivity::class.java)
        }

        cart_clear.setOnClickListener {
            RealmUtil.removeDataAll(CartItem::class.java)
            recycle_cart!!.adapter = null
        }
    }

    override fun onResume() {
        super.onResume()
        RxBus.subscribe(RxBus.CartResultPrice, this@CartFragment.javaClass, Consumer {
            updateResultPrice()
        })

        RxBus.subscribe(RxBus.CartMinusPrice, this@CartFragment.javaClass, Consumer {
            if (it is String) {
                minusResultPrice(it)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        RxBus.unregister(this@CartFragment.javaClass)
        RxBus.unregister(this@CartFragment.javaClass)
    }


    fun setView() {
        cartItemList.clear()
        RealmUtil.findDataAll(CartItem::class.java)?.let { cartItemList.addAll(realm.copyFromRealm(it)) }
        calTotalPriceAndSetOrderTextView()
        //장바구니 아이템이 있을 경우
        if (cartItemList.size > 0) {
            cart_clear.visible()
            layout_have_cart.visible()
            cart_menu_order.visible()
            alert_empty_cart.gone()
            cartAdapter = CartAdapter(context!!, cartItemList)
            recycle_cart.apply {
                recycle_cart.setting(cartAdapter, overscroll = false, verticalPadding = true)
                isNestedScrollingEnabled = false
            }

        } else {
            cart_clear.gone()
            layout_have_cart.gone()
            cart_menu_order.gone()
            alert_empty_cart.visible()
        }
    }

    private fun calTotalPriceAndSetOrderTextView() {
        totalPrice = 0
        cartItemList.forEach { totalPrice += it.menu.price.getOriginalPrice() }
        cart_menu_order.text = StringFormat(context!!, R.string.cart_order, totalPrice.toCommaWon())
    }

    private fun updateResultPrice() {
        var price = 0
        for (i in 0 until recycle_cart.childCount) {
            price += recycle_cart.getChildAt(i).cart_menu_result_price.text.getOriginalPrice()
        }
        cart_menu_order.text = StringFormat(context!!, R.string.cart_order, price.toCommaWon())
    }

    private fun minusResultPrice(minPrice: String) {
        cart_menu_order.text = StringFormat(context!!, R.string.cart_order, PriceUtil.minusPrice(minPrice, getOriginalPriceFromFinalOrderTextView()))
    }


    private fun getSelectedOption(optionLayout: LinearLayout): RealmList<SelectedOption> {
        val selectedOptionList: RealmList<SelectedOption> = RealmList()

        for (i in 0 until optionLayout.childCount) {
            val selectedOption = SelectedOption()

            with(optionLayout.getChildAt(i).recycle_option){
                (0 until adapter.itemCount)
                        .map { findViewHolderForAdapterPosition(it) }
                        .filter { it.itemView.isSelected }
                        .forEach { selectedOption.selectedOptionList.add(it.itemView.tag as CartOption) }
            }

            if (selectedOption.selectedOptionList.size > 0) {
                selectedOption.categoryName = optionLayout.getChildAt(i).txt_option_category.text.toString()
                selectedOptionList.add(selectedOption)
            }
        }

        return selectedOptionList
    }

    private fun getFinalCartItem() {
        if (cartItemList.size > 0) {
            for (i in 0 until cartAdapter.itemCount) {
                recycle_cart.findViewHolderForAdapterPosition(i)?.let {
                    with(it.itemView) {
                        cartItemList[i].menu_count = cart_menu_quantity.text.toString()
                        cartItemList[i].totalPrice = cart_menu_result_price.text.toString()

                        with(layout_cart_menu_necessary_option) {
                            if (visibility == View.VISIBLE) {
                                cartItemList[i].selNecessaryOptionList = getSelectedOption(this)
                            }
                        }
                        with(layout_cart_menu_unnecessary_option) {
                            if (visibility == View.VISIBLE) {
                                cartItemList[i].selUnNecessaryOptionList = getSelectedOption(this)
                            }
                        }
                    }
                }
                RealmUtil.insertData(cartItemList[i])


//                val cartItemViewHolder = recycle_cart.findViewHolderForAdapterPosition(i)
//
//                if (cartItemViewHolder != null) {
//                    cartItemList[i].menu_count = cartItemViewHolder.itemView.cart_menu_quantity.text.toString()
//                    cartItemList[i].totalPrice = cartItemViewHolder.itemView.cart_menu_result_price.text.toString()
//
//                    cartItemViewHolder.itemView.layout_cart_menu_necessary_option.let {
//                        if (it.visibility == View.VISIBLE) {
//                            cartItemList[i].selNecessaryOptionList = getSelectedOption(it)
//                        }
//                    }
//                    cartItemViewHolder.itemView.layout_cart_menu_unnecessary_option.let {
//                        if (it.visibility == View.VISIBLE) {
//                            cartItemList[i].selUnNecessaryOptionList = getSelectedOption(it)
//                        }
//                    }
//
//                } else {
//                    cartItemList[i].menu_count = 1.toString()
//                    cartItemList[i].totalPrice = cartItemList[i].menu.price
//                    for (optionCategory in cartItemList[i].optionCategoryList) {
//                        if (!optionCategory.multiple) {
//                            cartItemList[i].selNecessaryOptionList.add(SelectedOption(optionCategory.menu_option_category_name, optionCategory.option_content[0]))
//                        }
//                    }
//                }

            }

        }

    }

    private fun getOriginalPriceFromFinalOrderTextView(): String {
        cart_menu_order.text.toString().let {
            return it.substring(0, it.indexOf("원"))
        }
    }


}
