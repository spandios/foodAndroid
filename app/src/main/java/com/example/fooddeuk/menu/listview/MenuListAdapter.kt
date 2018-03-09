package com.example.fooddeuk.menu.listview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.cart.CartActivity
import com.example.fooddeuk.cart.model.CartItem
import com.example.fooddeuk.cart.model.CartMenu
import com.example.fooddeuk.cart.model.CartOption
import com.example.fooddeuk.cart.model.CartOptionCategory
import com.example.fooddeuk.menu.listview.viewholder.DoubleOptionViewHolder
import com.example.fooddeuk.menu.listview.viewholder.NoPictureViewHolder
import com.example.fooddeuk.menu.listview.viewholder.PictureNecessaryViewHolder
import com.example.fooddeuk.menu.listview.viewholder.PictureUnNecessaryViewHolder
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.util.RealmUtil
import io.realm.RealmList
import java.util.*


/**
 * Created by heojuyeong on 2017. 7. 31..
 * MenuListAdapter For RecyclerView
 */


class MenuListAdapter(private val context: Context, private val items: ArrayList<Menu>, private val restaurant: Restaurant) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var prevPosition = -1
    private var expandedPosition = -1
    lateinit var mItemClickListener : (position : Int, height : Int) -> Unit
    lateinit var mRecyclerView: RecyclerView
    private var currentPage = 0



    //Expandable and Animation
    private val menuExpandLayoutListener = View.OnClickListener { v ->
        val holder = v.tag as RecyclerView.ViewHolder
        val mMenuName = holder.itemView.findViewById<TextView>(R.id.txt_menu_name)
        val mMenuImage = holder.itemView.findViewById<ImageView>(R.id.menu_master_picture)
        val mDetailLayout = holder.itemView.findViewById<LinearLayout>(R.id.menu_detail_layout)


        //펼침
        if (expandedPosition != holder.adapterPosition) {
            //전에 펼쳐졌던 레이아웃 접기
            if (expandedPosition >= 0) {
                val prevViewHolder = mRecyclerView.findViewHolderForAdapterPosition(expandedPosition)
                setAnimation(prevViewHolder.itemView.findViewById(R.id.txt_menu_name), prevViewHolder.itemView.findViewById(R.id.menu_master_picture), false)
                prevViewHolder.itemView.findViewById<View>(R.id.menu_detail_layout).visibility = View.GONE
            }

            //현재 클릭된 레이아웃 펼치기
            //클릭시 포지션과 메뉴아이템의 높이를 알려줌으로써 스크롤 이동
            mItemClickListener(holder.adapterPosition,holder.itemView.findViewById<View>(R.id.menu_master_layout).height)
            expandedPosition = holder.adapterPosition
            setAnimation(mMenuName, mMenuImage, true)
            mDetailLayout.visibility = View.VISIBLE

        } else {
            //기존에 펼쳐져있던 아이템을 접을 시
            setAnimation(mMenuName, mMenuImage, false)
            mDetailLayout.visibility = View.GONE
            expandedPosition = -1
        }
    }
//    private val menuExpandLayoutListener = View.OnClickListener { v ->
//        val holder = v.tag as RecyclerView.ViewHolder
//        val mMenuName = holder.itemView.findViewById<TextView>(R.id.txt_menu_name)
//        val mMenuImage = holder.itemView.findViewById<ImageView>(R.id.menu_master_picture)
//        val mDetailLayout = holder.itemView.findViewById<LinearLayout>(R.id.menu_detail_layout)
//
//
//        //펼침
//        if (expandedPosition != holder.adapterPosition) {
//            //전에 펼쳐졌던 레이아웃 접기
//            prevPosition=expandedPosition
//            if(prevPosition>=0){
//                notifyItemChanged(prevPosition)
//            }
//            expandedPosition = holder.adapterPosition
//            notifyItemChanged(expandedPosition)
//            //현재 클릭된 레이아웃 펼치기
//            //클릭시 포지션과 메뉴아이템의 높이를 알려줌으로써 스크롤 이동
////            mItemClickListener(holder.adapterPosition,holder.itemView.findViewById<View>(R.id.menu_master_layout).height)
////            expandedPosition = holder.adapterPosition
////            setAnimation(mMenuName, mMenuImage, true)
////            mDetailLayout.visibility = View.VISIBLE
//        } else {
//            expandedPosition=-1
//            notifyItemChanged(holder.adapterPosition)
//            //기존에 펼쳐져있던 아이템을 접을 시
////            setAnimation(mMenuName, mMenuImage, false)
////            mDetailLayout.visibility = View.GONE
////            expandedPosition = -1
//        }
//    }




    private fun setAnimation(mMenuName: TextView, mMenuImage: ImageView, expand: Boolean) {
        if (expand) {
            val nameAnimation = TranslateAnimation(0f, -200f, 0f, 0f).apply {
                fillAfter = true
                isFillEnabled = true
                duration = 300
            }
            mMenuName.startAnimation(nameAnimation)

            YoYo.with(Techniques.SlideOutDown)
                    .duration(300)
                    .onEnd { mMenuImage.visibility = View.GONE }
                    .playOn(mMenuImage)
        } else {
            val nameAnimation = TranslateAnimation(-200f, 0f, 0f, 0f).apply {
                fillAfter = true
                isFillEnabled = true
                duration = 300
            }
            mMenuName.startAnimation(nameAnimation)

            YoYo.with(Techniques.SlideInUp)
                    .duration(300)
                    .onStart { mMenuImage.visibility = View.VISIBLE }
                    .playOn(mMenuImage)


        }
    }


    override fun getItemViewType(position: Int): Int {
        //사진 있는 경우
        if (items[position].picture.isNotEmpty()) {
            var necessary = false
            var unnecessary = false
            val optionCategory = items[position].option
            for (i in optionCategory.indices) {
                if (!optionCategory[i].multiple) {
                    necessary = true
                }
                if (optionCategory[i].multiple) {
                    unnecessary = true
                }
            }

            return if (necessary && unnecessary) {
                pictureAndDoubleOption
            } else if (necessary && !unnecessary) {
                pictureAndNecessary
            } else if (!necessary && unnecessary) {
                pictureAndUnNecessary
            } else {
                pictureAndNoOption
            }

        } else {
            var necessary = false
            var unnecessary = false
            val optionCategory = items[position].option
            for (i in optionCategory.indices) {
                if (!optionCategory[i].multiple) {
                    necessary = true
                }
                if (optionCategory[i].multiple) {
                    unnecessary = true
                }
            }

            return if (necessary && unnecessary) {
                noPictureAndDoubleOption
            } else if (necessary && !unnecessary) {
                noPictureAndNecessary
            } else if (!necessary && unnecessary) {
                noPictureAndUnNecessary
            } else {
                noPictureAndNoOption
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        //Picture X
        if (viewType == noPictureAndNoOption) {
            val parentView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_not_have_picture, parent, false)
            val vh = NoPictureViewHolder(parentView)
            vh.itemView.setOnClickListener(menuExpandLayoutListener)
            vh.itemView.findViewById<View>(R.id.menu_detail_order).setOnClickListener { view -> checkCartAndInsertOrOrder(items[vh.adapterPosition]) }
            return vh

        } else if (viewType == pictureAndNecessary) {
            //Picture O
            val parentView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_have_picture_necessary, parent, false)
            val vh = PictureNecessaryViewHolder(context, parentView)
            setItemClick(vh)
            return vh
        } else if (viewType == pictureAndUnNecessary) {
            val parentView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_have_picture_unnecessary, parent, false)
            val vh = PictureUnNecessaryViewHolder(context, parentView)
            setItemClick(vh)
            return vh
        } else if (viewType == pictureAndDoubleOption) {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_menu_have_picture, parent, false)
            val vh = DoubleOptionViewHolder(context, itemView)
            setItemClick(vh)
            return vh
        }

        return null
    }


    override fun onBindViewHolder(tmpHolder: RecyclerView.ViewHolder, position: Int) {
        val menu = items[position]

        if (tmpHolder.itemViewType == noPictureAndNoOption) {
            val holder = tmpHolder as NoPictureViewHolder
            holder.itemView.tag = holder
            holder.bind(menu, position)

        } else if (tmpHolder.itemViewType == pictureAndNecessary) {
            val holder = tmpHolder as PictureNecessaryViewHolder
            holder.itemView.tag = holder
            holder.bind(menu)

        } else if (tmpHolder.itemViewType == pictureAndUnNecessary) {
            val holder = tmpHolder as PictureUnNecessaryViewHolder
            holder.itemView.tag = holder
            holder.bind(menu)

        } else if (tmpHolder.itemViewType == pictureAndDoubleOption) {
            val holder = tmpHolder as DoubleOptionViewHolder

            holder.itemView.tag = holder
            holder.bind(menu)

//            if(expandedPosition==holder.adapterPosition){
//                mItemClickListener(holder.adapterPosition,holder.itemView.menu_master_layout.height)
//                setAnimation(holder.itemView.txt_menu_name, holder.itemView.menu_master_picture, true)
//                holder.itemView.menu_detail_layout.visibility = View.VISIBLE
//            }else {
//                    setAnimation(holder.itemView.txt_menu_name, holder.itemView.menu_master_picture, false)
//                    holder.itemView.menu_detail_layout.visibility = View.GONE
//            }

        }
    }

    private fun setItemClick(vh: RecyclerView.ViewHolder) {
        //EXPAND
        vh.itemView.setOnClickListener(menuExpandLayoutListener)

        //ORDER
        vh.itemView.findViewById<View>(R.id.menu_detail_order).setOnClickListener { view -> checkCartAndInsertOrOrder(items[vh.adapterPosition]) }

        //CART
        //        vh.itemView.findViewById(R.id.menu_detail_cart).setOnClickListener(view -> checkCartAndInsertOrOrder(items.get(vh.getAdapterPosition()), "cart"));

    }




    private fun getCartItem(menu: Menu): CartItem {
        val optionCategoryList = RealmList<CartOptionCategory>()
        for (optionCategory in menu.option) {
            if (optionCategory.option_content.size > 0) {
                val optionList = RealmList<CartOption>()
                for (necessaryOption in optionCategory.option_content) {
                    optionList.add(CartOption(necessaryOption.menu_option_id, necessaryOption.menu_option_name, necessaryOption.menu_option_price))
                }
                optionCategoryList.add(CartOptionCategory(optionCategory, optionList))
            }
        }
        return CartItem(CartMenu(menu), optionCategoryList)
    }

    //장바구니에 아이템이 있는지 없는지 체크
    private fun checkCartAndInsertOrOrder(menu: Menu) {
        val cartItem = RealmUtil.findDataAll(CartItem::class.java)
        if (cartItem != null) {
            if (cartItem.size > 0) {
                val defaultRestaurant = RealmUtil.findData(Restaurant::class.java)
                if (defaultRestaurant != null) {
                    if (defaultRestaurant._id != restaurant._id) {
                        MaterialDialog.Builder(context)
                                .content(context.getString(R.string.cart_delete_check))
                                .positiveText("예")
                                .negativeText("아니요")
                                .onPositive { dialog, which ->
                                    RealmUtil.removeDataAll(CartItem::class.java)
                                    RealmUtil.removeDataAll(Restaurant::class.java)
                                    RealmUtil.insertData(restaurant)
                                    RealmUtil.insertData(getCartItem(menu))
                                    Util.startActivity(context,CartActivity::class.java)
                                    //                            onCartCountClickListener.onCartCount(RealmUtil.getDataSize(CartItem.class));
                                }
                                .onNegative { dialog, which -> dialog.dismiss() }
                                .show()
                        return
                    }
                }
            }
        }

        RealmUtil.insertData(restaurant)
        RealmUtil.insertData(getCartItem(menu))
        Util.startActivity(context,CartActivity::class.java)
    }


    //    선탠된 메뉴 CartItem RealmObject 생성
    //    private CartItem createCartMenuItem(CartMenu menu) {
    //        CartItem cartItem;
    //        if (necessaryArrayList.size() > 0 || unNecessaryArrayList.size() > 0) {
    //            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu, getMenuOption());
    //        } else {
    //            cartItem = new CartItem(RealmUtil.getAutoIncrementId(CartItem.class), menu);
    //        }
    //        return cartItem;
    //    }


    override fun getItemCount(): Int = items.size


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    companion object {
        private const val pictureAndNoOption = 0
        private const val pictureAndNecessary = 1
        private const val pictureAndUnNecessary = 2
        private const val pictureAndDoubleOption = 3
        private const val noPictureAndNecessary = 4
        private const val noPictureAndUnNecessary = 5
        private const val noPictureAndDoubleOption = 6
        private const val noPictureAndNoOption = 7
    }


}



