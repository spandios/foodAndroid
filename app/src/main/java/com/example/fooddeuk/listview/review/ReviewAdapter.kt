package com.example.fooddeuk.listview.review

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.fooddeuk.model.review.MenuReview
import java.util.*


class ReviewAdapter(internal var context: Context, private val items: ArrayList<MenuReview>, private var isSimple: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (isSimple) {
            true -> ReviewSimpleVH(context, parent)

            false -> ReviewSimpleVH(context, parent) //detail
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reviewItem = items[position]
        //리뷰가 4개 이상이면
        if (items.size > 3) {
            if (position == 3) {
                when (holder) {
                    is ReviewSimpleVH -> {
                        holder.bindMore(items)
                        return
                    }
                }
            }
        }

        when (holder) {
            is ReviewSimpleVH -> holder.bind(reviewItem)
        }

    }


    override fun getItemCount(): Int {
        return if(items.size>3){
            4
        }else{
            items.size
        }
    }


}
