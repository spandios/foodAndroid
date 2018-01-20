package com.example.fooddeuk.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.model.cart.CartItem
import com.example.fooddeuk.util.PriceUtil
import com.example.fooddeuk.util.RealmUtil
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.item_cart.view.*
import java.util.*


/**
 * A custom adapter to use with the RecyclerView widget.
 */
class CartAdapter(private val mContext: Context, private var modelList: ArrayList<CartItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //기본 menu 수량은 1
    var cart_menu_quantity=1

    private var mItemClickListener: OnItemClickListener? = null
//    fun updateList(modelList: ArrayList<CartItem>) {
//        this.modelList = modelList
//        notifyDataSetChanged()
//    }

    //장바구니 아이템 삭제
    private fun removeList(position:Int){
        RealmUtil.removeDataById(CartItem::class.java, modelList[position].id)
        modelList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, modelList.size)
    }
    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var optionPrice=0
        //자동 형 변환
        if (holder is CartAdapter.ViewHolder) {
            val model = getItem(position)
            holder.cart_menu_name.text=model.menu.menu_name
            holder.cart_menu_price.text= model.menu.menu_price+"원";

            //옵션 텍스트 뷰 추가
            //옵션카테고리:옵션이름 , 옵션 총 가격 계산
            for(i in model.option.indices){

                optionPrice+= PriceUtil.getOriginalPrice(model.option[i].menu_option_price)
                val optionCateAndName=TextView(mContext)
                optionCateAndName.text=model.option[i].menu_option_name
                holder.cart_menu_option_layout.addView(optionCateAndName)
            }

            //수량
            holder.cart_menu_quantity.text=cart_menu_quantity.toString()

            //총 옵션 가격
            holder.cart_menu_option_price.text= PriceUtil.comma_won(optionPrice)

            //메뉴가격 + 옵션가격 = 총 가격
            holder.cart_menu_result_price.text= PriceUtil.plusPrice(PriceUtil.TextViewToString(holder.cart_menu_price),optionPrice)

            //목록삭제
            holder.cart_delete_button.setOnClickListener{removeList(position)}

            //수량 -
            RxView.clicks(holder.cart_menu_minus)
                    .map{Integer.parseInt(holder.cart_menu_quantity.text.toString())}
                    .filter { value->value>1}
                    .map{value->value-1}
                    .subscribe { value->
                        holder.cart_menu_quantity.text=value.toString()
//                        holder.cart_menu_price.text = PriceUtil.minusPrice(PriceUtil.TextViewToString(holder.cart_menu_price),model.menu.menu_price)
                        holder.cart_menu_result_price.text = PriceUtil.minusPrice(PriceUtil.TextViewToString(holder.cart_menu_result_price),model.menu.menu_price)

                    }

            //수량+
            RxView.clicks(holder.cart_menu_plus)
                    .map{Integer.parseInt(holder.cart_menu_quantity.text.toString())}
                    .filter { value->value<100}
                    .map{value->value+1}
                    .subscribe { value->
                        holder.cart_menu_quantity.text=value.toString()
//                        holder.cart_menu_price.text = PriceUtil.plusPrice(PriceUtil.TextViewToString(holder.cart_menu_price),model.menu.menu_price)
                        holder.cart_menu_result_price.text = PriceUtil.plusPrice(PriceUtil.TextViewToString(holder.cart_menu_result_price),model.menu.menu_price)
                    }
        }


    }


    override fun getItemCount(): Int {
        return modelList.size
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    private fun getItem(position: Int): CartItem {
        return modelList[position]
    }


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, model: CartItem)
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cart_menu_name = itemView.cart_menu_name
        var cart_menu_price=itemView.cart_menu_price
        var cart_delete_button=itemView.cart_delete_button
        var cart_menu_option_layout=itemView.cart_menu_option_layout
        var cart_menu_option_price=itemView.cart_menu_option_price
        var cart_menu_minus=itemView.cart_menu_minus
        var cart_menu_quantity=itemView.cart_menu_quantity
        var cart_menu_plus=itemView.cart_menu_plus
        var cart_menu_result_price=itemView.cart_menu_result_price

        init {
            itemView.setOnClickListener { mItemClickListener!!.onItemClick(itemView, adapterPosition, modelList[adapterPosition])

            }

        }
    }

}

