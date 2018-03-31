package com.example.fooddeuk.pick

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.menu.DetailMenuActivity
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_pick_menu.view.*

/**
 * Created by heo on 2018. 3. 13..
 */

class PickMenuAdapter(val context: Context, private var menus: List<Menu>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        if (menus.size > 4) {
            return 4
        } else {
            return menus.size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_pick_menu, parent, false)
        val menuViewHolder = PickMenuViewHolder(itemView)
        menuViewHolder.itemView.setOnClickListener {
            context.StartActivity(RxBus.DetailMenuActivityData, menus[menuViewHolder.adapterPosition], DetailMenuActivity::class.java)
        }
        return menuViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val menu = menus[position]
        if (holder is PickMenuViewHolder) {
            holder.bind(menu)
        }
    }

}

class PickMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(menu: Menu) {
        with(itemView) {
            if (menu.picture.isNotEmpty()) {
                Picasso.with(context).load(menu.picture[0]).fit().into(pick_menu_item_image)
            }

            pick_menu_item_name.text = menu.name
            pick_menu_item_rating_star.rating = menu.rating.toFloat()
            pick_menu_item_reviewcnt.text = Util.stringFormat(context, R.string.reviewcnt, menu.reviewCnt.toString())
        }
    }
}