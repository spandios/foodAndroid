package com.example.fooddeuk.cart

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.cart.model.SelectedOption
import com.example.fooddeuk.order.OrderActivity
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.*
import io.reactivex.functions.Consumer
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_cart.view.*

class CartFragment : android.support.v4.app.Fragment() {
    val realm: Realm = Realm.getDefaultInstance()
    private val cartItemList = ArrayList<CartItem>()
    lateinit var adapter: CartAdapter
    var totalPrice = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setView()
        getFirstResultPrice()

        //publish by CartViewHolder


    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).fragmentPager.notifyDataSetChanged()
        RxBus.subscribe(RxBus.CartResultPrice, this@CartFragment.javaClass, Consumer {
            updateResultPrice()
        })
        RxBus.subscribe(RxBus.CartFragmentSizeZero, this@CartFragment.javaClass, Consumer {
            cart_clear.visibility=View.GONE
            layout_cart.visibility=View.GONE
            layout_cart_no_item.visibility=View.VISIBLE
        })
    }

    override fun onPause() {
        super.onPause()
        RxBus.unregister(this@CartFragment.javaClass)
        RxBus.unregister(this@CartFragment.javaClass)

    }


    private fun getFirstResultPrice() {
        cartItemList.forEach {
            totalPrice += it.menu.price.getOriginalPrice()
        }
        cart_menu_order.text = Util.stringFormat(context!!, R.string.cart_order, totalPrice.toCommaWon())
    }

    private fun updateResultPrice() {
        var price = 0
        for(i in 0 until recycle_cart.childCount){
            price+=recycle_cart.getChildAt(i).cart_menu_result_price.text.getOriginalPrice()
        }
        cart_menu_order.text = Util.stringFormat(context!!, R.string.cart_order, price.toCommaWon())
    }
    
    private fun getSelectedOption(parentLayout: LinearLayout): RealmList<SelectedOption>? {
        if (parentLayout.childCount != 0) {
            val selectedOptionList: RealmList<SelectedOption> = RealmList()

            for (i in 0 until parentLayout.childCount) {
                val selectedOption = SelectedOption()

                val necessaryList = parentLayout.getChildAt(i).findViewById<RecyclerView>(R.id.recycle_option)

                (0 until necessaryList.adapter.itemCount)
                        .map { necessaryList.findViewHolderForAdapterPosition(it) }
                        .filter { it.itemView.isSelected }
                        .forEach {
                            selectedOption.selectedOptionList.add(it.itemView.tag as CartOption)
                        }

                if (selectedOption.selectedOptionList.size > 0) {
                    val categoryName = parentLayout.getChildAt(i).findViewById<TextView>(R.id.txt_option_category)
                    selectedOption.categoryName = categoryName.text.toString()
                    selectedOptionList.add(selectedOption)
                }
            }


            return selectedOptionList
        }

        return null
    }

    private fun getFinalCartItem() {
        if (cartItemList.size > 0) {
            for (i in 0 until adapter.itemCount) {

                val cartItemViewHolder = recycle_cart.findViewHolderForAdapterPosition(i)
                if(cartItemViewHolder!=null){
                    cartItemList[i].menu_count = cartItemViewHolder.itemView.cart_menu_quantity.text.toString()
                    cartItemList[i].totalPrice = cartItemViewHolder.itemView.cart_menu_result_price.text.toString()

                    cartItemViewHolder.itemView.layout_cart_menu_necessary_option.let {
                        if (it.visibility == View.VISIBLE) {
                            cartItemList[i].selNecessaryOptionList = getSelectedOption(it)
                        }
                    }
                    cartItemViewHolder.itemView.layout_cart_menu_unnecessary_option.let {
                        if (it.visibility == View.VISIBLE) {
                            cartItemList[i].selUnNecessaryOptionList = getSelectedOption(it)
                        }
                    }

                }else{
                    cartItemList[i].menu_count=1.toString()
                    cartItemList[i].totalPrice=cartItemList[i].menu.price
                    for(optionCategory in cartItemList[i].optionCategoryList){
                        if(!optionCategory.multiple){
                            cartItemList[i].selNecessaryOptionList.add(SelectedOption(optionCategory.menu_option_category_name,optionCategory.option_content[0]))
                        }
                    }

                }
                RealmUtil.insertData(cartItemList[i])
            }
        } else {
            Toast.makeText(context!!, "아이템이 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

     fun setView() {
        //모든 장바구니 삭제

        val cartItem = RealmUtil.findDataAll(CartItem::class.java)
        cartItemList.addAll(realm.copyFromRealm(cartItem))

        if (cartItemList.size > 0) {
            layout_cart.visibility=View.VISIBLE
            cart_clear.visibility=View.VISIBLE
            layout_cart_no_item.visibility=View.GONE
            adapter = CartAdapter(context!!, cartItemList)
            recycle_cart.setting(adapter,overscroll = true, verticalPadding = true)

        }else{
            cart_clear.visibility=View.GONE
            layout_cart.visibility=View.GONE
            layout_cart_no_item.visibility=View.VISIBLE
        }

        cart_clear.setOnClickListener({
            RealmUtil.removeDataAll(CartItem::class.java)
            recycle_cart!!.adapter = null
        })

        cart_menu_order.setOnClickListener({
            if (cartItemList.size != 0) {
                /** 장바구니 각각의 아이템  수량,가격 , 옵션 삽입 후 주문 activity 호출*/
                getFinalCartItem()
                StartActivity(OrderActivity::class.java)
            } else {
                Toast.makeText(context!!, "아이템이 없습니다", Toast.LENGTH_SHORT).show()
            }
        })

    }



}
