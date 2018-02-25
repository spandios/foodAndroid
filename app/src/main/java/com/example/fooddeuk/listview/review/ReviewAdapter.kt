package com.example.fooddeuk.listview.review

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.fooddeuk.model.review.MenuReview
import java.util.*


class ReviewAdapter(internal var context: Context, private val items: ArrayList<MenuReview>, private var isSimple: Boolean,private var moreDetailLayout : LinearLayout) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(isSimple){
            true->ReviewSimpleVH(context,parent)

            false-> ReviewSimpleVH(context,parent) //detail
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reviewItem = items[position]
        if(items.size>3){
            if(position==3){
                when(holder){
                    is ReviewSimpleVH -> holder.bind(reviewItem,items.size)
                }
            }
        }else{
            when(holder){
                is ReviewSimpleVH -> holder.bind(reviewItem,items.size,false)

            }
        }




    }


    override fun getItemCount(): Int{
        if(items.size>3){
            moreDetailLayout.visibility= View.VISIBLE
            return 4
        }else{
//            moreDetailLayout.visibility= View.GONE
            return items.size
        }
    }



}
