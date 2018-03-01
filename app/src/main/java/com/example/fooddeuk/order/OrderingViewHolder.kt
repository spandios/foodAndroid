package com.example.fooddeuk.order

/**
 * Created by heo on 2018. 1. 25..
 */


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.model.order.OrderResponse
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_order_history_ing.view.*





/**
 * Created by heo on 2018. 1. 25..
 */
class OrderingViewHolder(val context: Context, parent: ViewGroup?) : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history_ing, parent, false)){

    init { itemView.setOnClickListener(View.OnClickListener { }) }

    fun bind(orderResponse: OrderResponse) {
        itemView.bindView(orderResponse)

    }

    private fun View.bindView(orderResponse: OrderResponse) {
        Picasso.with(context).load("https://maps.googleapis.com/maps/api/staticmap?center=${orderResponse.restaurant.lat},${orderResponse.restaurant.lng}&zoom=17&scale=1&size=600x300&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:1%7C${orderResponse.restaurant.lat},${orderResponse.restaurant.lng}&key=AIzaSyBilL-hPo0Rsm5cRWw0Dj7hABnC6bzN9uk").fit().into(order_list_static_map)
        Picasso.with(context).load(orderResponse.restaurant.picture).transform(CropCircleTransformation()).into(order_list_image)
        order_list_id.text="주문 번호 : ${orderResponse.id}"
        order_list_rest_name.text = orderResponse.restaurant.rest_name
        order_list_date.text= orderResponse.created_at
        order_list_state.text="주문 상태 : ${orderResponse.status}"
        order_list_arrivedTime.text="도착 예정 시간 : ${orderResponse.user.arrivedTime}"
        order_list_result_price.text="총 주문 금액 : ${orderResponse.user.completePrice}"
//        createMenuLayout(orderResponse.cartItems)
    }

//    private fun View.createMenuLayout(cartItems: ArrayList<CartItem>){
//        cartItems?.let{item->
//            if(item.size>0){
//                item.forEach({
//                    LayoutInflater.from(context).inflate(R.layout.item_order_history_menu_dynamic_layout, order_list_menu_parent_layout, false)?.let { menuLayout ->
//                        menuLayout.findViewById<TextView>(R.id.order_list_menu_name).text=it.menu.name
//                        menuLayout.findViewById<TextView>(R.id.order_list_menu_count).text = "x" + it.menu_count
//                        menuLayout.findViewById<TextView>(R.id.order_list_menu_price).text = it.menu.price + "원"
//                        if(it.option!=null){
//                            if(it.option.size>0){
//
//
//                                it.option.forEach({ option ->
//                                    //ParentOptionLayout vertical
//                                    menuLayout.findViewById<LinearLayout>(R.id.order_list_option_layout).apply {
//                                        visibility=View.VISIBLE
//                                        addView(RelativeLayout(context).apply {
//                                            //OptionContentLayout
//                                            layoutParams=RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
//
//                                            //add name
//                                            addView(TextView(context).apply {
//                                                text = option.menu_option_name
//                                                setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimension(R.dimen.order_list_option_name_text))
//                                                layoutParams=RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
//                                                    marginStart= resources.getDimension(R.dimen.order_list_option_name_margin).toInt()
//                                                    addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//
//                                                }
//                                            })
//
//                                            //add price
//                                            addView(TextView(context).apply {
//                                                text = option.menu_option_price+"원"
//                                                setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimension(R.dimen.order_list_option_price_text))
//                                                layoutParams=RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
//                                                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                                                    marginEnd= resources.getDimensionPixelSize(R.dimen.order_list_option_price_margin)
//                                                }
//                                            })
//                                        })
//                                    }
//                                })
//                            }
//                        }
//
//                        order_list_menu_parent_layout.addView(menuLayout)
//                    }
//                })
//            }
//
//        }
//    }


}