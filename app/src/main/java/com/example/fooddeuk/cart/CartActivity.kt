package com.example.fooddeuk.cart

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.OrderActivity
import com.example.fooddeuk.adapter.CartAdapter
import com.example.fooddeuk.model.cart.CartItem
import com.example.fooddeuk.model.menu.Option
import com.example.fooddeuk.util.IntentUtil
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.RealmUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.item_cart.view.*


class CartActivity : AppCompatActivity() {
    val realm: Realm = Realm.getDefaultInstance()
    private val cartItemList = ArrayList<CartItem>()
    private val optionDialogArray = arrayOfNulls<BottomSheetDialog>(2)
    private val necessaryArrayList = java.util.ArrayList<Option>()
    private val unNecessaryArrayList = java.util.ArrayList<Option>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setToolBar()
        setView()
        setCartAdapter()

    }


    private fun setToolBar() {
        setSupportActionBar(cartToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cartToolbar.setNavigationIcon(R.drawable.backbutton)
        cartToolbar.setNavigationOnClickListener { finish() }
    }

    private fun setCartAdapter() {
        val cartItem = realm.where(CartItem::class.java).findAll()
        cartItemList.addAll(realm.copyFromRealm(cartItem))
        val adapter = CartAdapter(this, cartItemList)

        adapter.SetOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, model: CartItem) {
            }
        })
        LayoutUtil.RecyclerViewSetting(this.applicationContext, cartListView)
        cartListView!!.adapter = adapter

    }

    private fun setView() {
        //모든 장바구니 삭제
        cartClear.setOnClickListener({
            RealmUtil.removeDataAll(CartItem::class.java)
            cartListView!!.adapter = null
            finish()
        })

        cart_menu_order.setOnClickListener({
            if (cartItemList.size != 0) {
                /** 장바구니 각각의 아이템  수량과 가격 데이터 삽입 후 주문 activity 호출*/
                var childCount = cartListView.childCount
                var i = 0
                while (i < childCount) {
                    var cartItemViewHolder = cartListView.findViewHolderForAdapterPosition(i)
                    val count: TextView = cartItemViewHolder.itemView.cart_menu_quantity
                    val totalPrice = cartItemViewHolder.itemView.cart_menu_result_price
                    cartItemList[i].menu_count = count.text.toString()
                    cartItemList[i].totalPrice = totalPrice.text.toString()
                    RealmUtil.insertData(cartItemList[i])
                    i++
                }

                IntentUtil.startActivity(this, OrderActivity::class.java)
            } else {
                Toast.makeText(this, "아이템이 없습니다", Toast.LENGTH_SHORT).show()
            }


        })


    }

//    private fun addSelectOptionToArray() {
//
//        var optionCategoryList = optionDialogArray[0]?.findViewById<RecyclerView>(R.id.dialog_menu_option_listview)
//        for (i in 0 until optionCategoryList!!.childCount) {
//            val categoryView = optionCategoryList.getChildAt(i)
//            val optionContentList = categoryView.findViewById<LinearLayout>(R.id.dialog_test)
//            for (j in 0 until optionContentList.childCount) {
//                val optionView = optionContentList.getChildAt(j)
//                if (optionView.findViewById<View>(R.id.option_radio) != null) {
//                    val radioOption = optionView.findViewById<RadioButton>(R.id.option_radio)
//                    if (radioOption.isChecked) {
//                        val optionContent = radioOption.tag as Option
//                        necessaryArrayList.add(optionContent)
//                    }
//                }
//            }
//        }
//
//
//        optionCategoryList = optionDialogArray[1]?.findViewById(R.id.dialog_menu_option_listview)
//        for (i in 0 until optionCategoryList!!.childCount) {
//            val categoryView = optionCategoryList.getChildAt(i)
//            val optionContentList = categoryView.findViewById<LinearLayout>(R.id.dialog_test)
//            for (j in 0 until optionContentList.childCount) {
//                val optionView = optionContentList.getChildAt(j)
//                if (optionView.findViewById<View>(R.id.option_checkbox) != null) {
//                    val checkOption = optionView.findViewById<CheckBox>(R.id.option_checkbox)
//                    if (checkOption.isChecked) {
//                        val optionContent = checkOption.tag as Option
//                        unNecessaryArrayList.add(optionContent)
//                    }
//                }
//            }
//        }
//    }
//
//    fun setDoubleOptionDialog(optionCategory: java.util.ArrayList<OptionCategory>, viewHolder: DoubleOptionViewHolder) {
//        val necessaryOptionDialog = BottomSheetDialog(context)
//        val unNecessaryOptionDialog = BottomSheetDialog(context)
//        val necessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null)
//        necessaryOptionDialog.setContentView(necessaryOptionDialogView)
//        val unNecessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null)
//        unNecessaryOptionDialog.setContentView(unNecessaryOptionDialogView)
//
//
//        val necessaryOptionConfirmButton = necessaryOptionDialogView.findViewById<Button>(R.id.optionConfirmButton)
//        val unNecessaryOptionConfirmButton = unNecessaryOptionDialogView.findViewById<Button>(R.id.optionConfirmButton)
//        setOptionRecyclerView(necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), viewHolder.menuOrder, necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, true)
//        setOptionRecyclerView(unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), viewHolder.menuOrder, unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, false)
//        optionDialogArray[0] = necessaryOptionDialog
//        optionDialogArray[1] = unNecessaryOptionDialog
//
//        val clickListener= View.OnClickListener { v ->
//            necessaryArrayList.clear()
//            unNecessaryArrayList.clear()
//
//            addSelectOptionToArray()
//            if (v === necessaryOptionConfirmButton) {
//                viewHolder.menuOptionNecessaryContent.text = getSelectOption(necessaryArrayList)
//                val dialogTotalPrice = optionDialogArray[0]?.findViewById<TextView>(R.id.dialog_totalPrice)
//                (optionDialogArray[0]?.findViewById<View>(R.id.dialog_totalPrice) as TextView).text = dialogTotalPrice!!.text
//                (optionDialogArray[1]?.findViewById<View>(R.id.dialog_totalPrice) as TextView).text = dialogTotalPrice.text
//                viewHolder.menuOrder.text = dialogTotalPrice.text.toString() + " 바로 주문"
//                optionDialogArray[0]?.dismiss()
//            } else {
//                viewHolder.menuOptionUnnecessaryContent.text = getSelectOption(unNecessaryArrayList)
//                val dialogTotalPrice = optionDialogArray[1]?.findViewById<TextView>(R.id.dialog_totalPrice)
//                (optionDialogArray[1]?.findViewById<View>(R.id.dialog_totalPrice) as TextView).text = dialogTotalPrice!!.text
//                (optionDialogArray[0]?.findViewById<View>(R.id.dialog_totalPrice) as TextView).text = dialogTotalPrice.text
//                viewHolder.menuOrder.text = dialogTotalPrice.text.toString() + " 바로 주문"
//                optionDialogArray[1]?.dismiss()
//            }
//        }
//        necessaryOptionConfirmButton.setOnClickListener(clickListener)
//        unNecessaryOptionConfirmButton.setOnClickListener(clickListener)
//
//    }
//
//    fun setNecessaryOptionDialog(menu: Menu, viewHolder: PictureNecessaryViewHolder) {
//        val optionCategory = menu.option
//        val necessaryOptionDialog = BottomSheetDialog(context)
//        val necessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null)
//        necessaryOptionDialog.setContentView(necessaryOptionDialogView)
//
//        val necessaryOptionConfirmButton = necessaryOptionDialogView.findViewById<Button>(R.id.optionConfirmButton)
//        val clickListener =object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                necessaryArrayList.clear()
//                val dialogTotalPrice: TextView
//                val optionCategoryList: RecyclerView
//                dialogTotalPrice = necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice)
//                optionCategoryList = necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview)
//
//
//                for (i in 0 until optionCategoryList.childCount) {
//                    val categoryView = optionCategoryList.getChildAt(i)
//
//                    //CategoryName
//                    val optionContentList = categoryView.findViewById<LinearLayout>(R.id.dialog_test)
//
//                    for (j in 0 until optionContentList.childCount) {
//
//                        val optionView = optionContentList.getChildAt(j)
//                        if (optionView.findViewById<View>(R.id.option_radio) != null) {
//                            val radioOption = optionView.findViewById<RadioButton>(R.id.option_radio)
//                            if (radioOption.isChecked) {
//                                val optionContent = radioOption.tag as Option
//                                necessaryArrayList.add(optionContent)
//                            }
//                        }
//                    }
//                }
//                viewHolder.menu_detail_option_necessary_content.text = getSelectOption(necessaryArrayList)
//                (optionDialogArray[0]?.findViewById<View>(R.id.dialog_totalPrice) as TextView).text = dialogTotalPrice.text
//                viewHolder.menu_detail_order.text = dialogTotalPrice.text.toString() + " 바로 주문"
//                optionDialogArray[0]?.dismiss()
//            }
//        }
//        necessaryOptionConfirmButton.setOnClickListener(clickListener)
//        setOptionRecyclerView(necessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), viewHolder.menu_detail_order, necessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, true)
//        optionDialogArray[0] = necessaryOptionDialog
//        optionDialogArray[1] = null
//
//    }
//
//    /**
//     * 필수 항목의 가장 첫번째 옵션(기본옵션) 가져와 뷰에 뿌려주고 저장
//     * param1 : MenuCategory
//     */
//
//    fun setFirstNecessaryOption(menu: Menu, viewHolder: RecyclerView.ViewHolder) {
//        necessaryArrayList.clear()
//        val optionCategoryArray = menu.option
//
//        val necessaryCategoryText = StringBuffer("")
//        val necessaryOptionText = StringBuffer("")
//        val title = viewHolder.itemView.findViewById<TextView>(R.id.menu_detail_option_necessary_title)
//        val content = viewHolder.itemView.findViewById<TextView>(R.id.menu_detail_option_necessary_content)
//        if (menu.option.size == 1) {
//            val necessaryBasicOption = optionCategoryArray[0].necessary[0]
//            necessaryArrayList.add(necessaryBasicOption)
//            Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가")
//            title.text = optionCategoryArray[0].menu_option_category_name
//            content.text = necessaryBasicOption.menu_option_name
//
//        } else if (menu.option.size > 1) {
//            for (i in optionCategoryArray.indices) {
//                val categoryOption = optionCategoryArray[i]
//                if (categoryOption.necessary.size > 0) {
//                    val necessaryBasicOption = categoryOption.necessary[0]
//                    if (i == 0) {
//                        necessaryCategoryText.append(categoryOption.menu_option_category_name)
//                        necessaryOptionText.append(necessaryBasicOption.menu_option_name)
//                    } else if (i != optionCategoryArray.size - 1) {
//                        necessaryCategoryText.append(categoryOption.menu_option_category_name + ",")
//                        necessaryOptionText.append(necessaryBasicOption.menu_option_name + ",")
//                    } else {
//                        necessaryCategoryText.append(categoryOption.menu_option_category_name)
//                        necessaryOptionText.append(necessaryBasicOption.menu_option_name)
//                    }
//                    necessaryArrayList.add(necessaryBasicOption)
//                    Logger.d("기본 필수옵션 " + necessaryBasicOption.menu_option_name + " 추가")
//                }
//
//
//            }
//
//            title.text = necessaryCategoryText
//            content.text = necessaryOptionText
//        }
//
//    }
//
//    fun setUnNecessaryOptionDialog(optionCategory: java.util.ArrayList<OptionCategory>, holder: PictureUnNecessaryViewHolder) {
//        val unNecessaryOptionDialog = BottomSheetDialog(context)
//        val unNecessaryOptionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu_option, null)
//        unNecessaryOptionDialog.setContentView(unNecessaryOptionDialogView)
//
//        //확인버튼
//        val unNecessaryOptionConfirmButton = unNecessaryOptionDialogView.findViewById<Button>(R.id.optionConfirmButton)
//        val clickListener = { v : View->
//            unNecessaryArrayList.clear()
//            val dialogTotalPrice = unNecessaryOptionDialogView.findViewById<TextView>(R.id.dialog_totalPrice)
//            val optionCategoryList = unNecessaryOptionDialogView.findViewById<RecyclerView>(R.id.dialog_menu_option_listview)
//
//
//            for (i in 0 until optionCategoryList.childCount) {
//                val categoryView = optionCategoryList.getChildAt(i)
//                val optionContentList = categoryView.findViewById<LinearLayout>(R.id.dialog_test)
//                for (j in 0 until optionContentList.childCount) {
//                    val optionView = optionContentList.getChildAt(j)
//                    if (optionView.findViewById<View>(R.id.option_checkbox) != null) {
//                        val checkOption = optionView.findViewById<CheckBox>(R.id.option_checkbox)
//                        if (checkOption.isChecked) {
//                            val optionContent = checkOption.tag as Option
//                            unNecessaryArrayList.add(optionContent)
//                        }
//                    }
//                }
//            }
//
//            holder.menu_detail_option_unnecessary_content.text = getSelectOption(unNecessaryArrayList)
//            optionDialogArray[1]?.dismiss()
//            (optionDialogArray[1]?.findViewById<View>(R.id.dialog_totalPrice) as TextView).text = dialogTotalPrice.text
//            holder.menu_detail_order.text = dialogTotalPrice.text.toString() + " 바로 주문"
//        }
//
//        unNecessaryOptionConfirmButton.setOnClickListener(clickListener)
//        setOptionRecyclerView(unNecessaryOptionDialogView.findViewById(R.id.dialog_menu_option_listview), holder.menu_detail_order, unNecessaryOptionDialogView.findViewById(R.id.dialog_totalPrice), optionCategory, false)
//        optionDialogArray[0] = null
//        optionDialogArray[1] = unNecessaryOptionDialog
//
//    }

//    internal fun setOptionRecyclerView(optionCategoryList: RecyclerView, menu_detail_order: Button, dialog_totalPrice: TextView, optionCategory: ArrayList<OptionCategory>, necessary: Boolean) {
//        LayoutUtil.RecyclerViewSetting(context, optionCategoryList)
//        dialog_totalPrice.text = menu_detail_order.text.subSequence(0, menu_detail_order.text.length - 6)
//
//
//        val selectCLickListener = { isPlus : String, price : String ->
//            dialog_totalPrice.text = if (isPlus == "plus")
//                PriceUtil.plusPrice(price, PriceUtil.TextViewToString(dialog_totalPrice))
//            else
//                PriceUtil.minusPrice(price, PriceUtil.TextViewToString(dialog_totalPrice))
//        }
//
//        val radioPriceListener = { plusPrice : String, minusPrice : String ->
//
//            val price = PriceUtil.getOriginalPrice(plusPrice) - PriceUtil.getOriginalPrice(minusPrice) + PriceUtil.getOriginalPrice(dialog_totalPrice.text.toString())
//            dialog_totalPrice.text = PriceUtil.comma_won(price)
//        }
//
//        //선택
//        if (!necessary) {
//            val unNecessaryOptionList = ArrayList<OptionCategory>()
//            for (i in optionCategory.indices) {
//                if (optionCategory[i].multiple) {
//                    unNecessaryOptionList.add(optionCategory[i])
//                }
//            }
//            optionCategoryList.adapter = OptionCategoryAdapter(unNecessaryOptionList, context, selectCLickListener, radioPriceListener)
//        } else {
//            val necessaryOptionList = ArrayList<OptionCategory>()
//            for (i in optionCategory.indices) {
//                if (!optionCategory[i].multiple) {
//                    necessaryOptionList.add(optionCategory[i])
//                }
//            }
//            optionCategoryList.adapter = OptionCategoryAdapter(necessaryOptionList, context, selectCLickListener, radioPriceListener)
//        }//필수
//    }
//
//    internal fun getSelectOption(optionArrayList: ArrayList<Option>): String? {
//        if (optionArrayList.size == 1) {
//            return optionArrayList[0].menu_option_name
//        } else if (optionArrayList.size > 1) {
//            val s = StringBuffer()
//            for (i in optionArrayList.indices) {
//                if (i == optionArrayList.size - 1) {
//                    s.append(optionArrayList[i].menu_option_name)
//                    break
//                }
//                s.append(optionArrayList[i].menu_option_name + ",")
//
//            }
//            return s.toString()
//        }
//        return null
//    }

    //필수, 선택 옵션 구분한뒤 localdb 변환
    //필수값 먼저 삽입한다
//    val menuOption: RealmList<Option>
//        get() {
//            val menuOption = ArrayList<Option>()
//            val optionRealmList = RealmList<Option>()
//            if (necessaryArrayList.size >= 1) {
//                menuOption.addAll(necessaryArrayList)
//            }
//            if (unNecessaryArrayList.size >= 1) {
//                menuOption.addAll(unNecessaryArrayList)
//            }
//            optionRealmList.addAll(menuOption)
//
//            return optionRealmList
//        }

    //        //OPTION
    //        if (optionFLAG.equals("necessary")) {
    //            vh.itemView.findViewById(R.id.menu_detail_necessary_option_layout).setOnClickListener(v -> optionDialogArray[0].show());
    //        } else if (optionFLAG.equals("unnecessary")) {
    //            vh.itemView.findViewById(R.id.menu_detail_unnecessary_option_layout).setOnClickListener(v -> optionDialogArray[1].show());
    //        } else {
    //            vh.itemView.findViewById(R.id.menu_detail_necessary_option_layout).setOnClickListener(v -> optionDialogArray[0].show());
    //            vh.itemView.findViewById(R.id.menu_detail_unnecessary_option_layout).setOnClickListener(v -> optionDialogArray[1].show());
    //        }
    }
