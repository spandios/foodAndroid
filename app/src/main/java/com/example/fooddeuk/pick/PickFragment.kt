package com.example.fooddeuk.pick


import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.pick.dangol.DangolListActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.example.fooddeuk.util.gone
import com.example.fooddeuk.util.toast
import com.example.fooddeuk.util.visible
import kotlinx.android.synthetic.main.fragment_pick.*


class PickFragment : Fragment(), PickContract.View {
    private lateinit var root: ConstraintLayout
    private lateinit var pickPresenter: PickPresenter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_pick, container, false)
        this.root = root as ConstraintLayout
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pickPresenter= PickPresenter().apply { view=this@PickFragment }

    }

    override fun onResume() {
        super.onResume()
        pickPresenter.getDangol()
        pickPresenter.getHot()
    }

    override fun onPause() {
        super.onPause()
        pickPresenter.clearDisposable()
    }

    override fun showErrorDangol() {
            toast("pickPresenter Error")
    }

    override fun showErrorRecommand() {
        toast("recommand Error")
    }

    override fun showErrorHot() {
        toast("hot Error")
    }

    override fun setDangolRV(dangolList: List<Restaurant>) {
        dangol_rest_recycle.layoutManager = GridLayoutManager(context, 2)
        dangol_rest_recycle.adapter = PickAdapter(context!!, dangolList)
    }

    override fun setHotRV(hotList: List<Restaurant>) {
        hot_rest_recycle.layoutManager = GridLayoutManager(context, 2)
        hot_rest_recycle.adapter = PickAdapter(context!!, hotList)
    }

    override fun noDangolList() {
        dangol_item_nothing.visible()
        dangol_rest_recycle.gone()
        dangol_item_more.gone()
    }

    override fun dangolMore(dangolList: List<Restaurant>) {
        dangol_item_more.visibility = View.VISIBLE
        dangol_item_more.setOnClickListener {
            RxBus.intentPublish(RxBus.DangolListData,PickAdapter(context!!,dangolList))
            StartActivity(DangolListActivity::class.java)
        }
    }

    override fun noHotList() {
        hot_rest_recycle.gone()
        hot_item_more.gone()
    }

    override fun hotMore() {
        hot_item_more.visibility = View.VISIBLE
    }


}
