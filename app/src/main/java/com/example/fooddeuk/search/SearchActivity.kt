package com.example.fooddeuk.search

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.restaurant.list.RestaurantAdapter
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.util.gone
import com.example.fooddeuk.util.setting
import com.example.fooddeuk.util.toast
import com.example.fooddeuk.util.visible
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchContract.View {

    private lateinit var imm: InputMethodManager
    private val compositeDisposable = CompositeDisposable()
    private lateinit var presenter: SearchPresenter
    private var isFirstSearch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        presenter = SearchPresenter().apply { view = this@SearchActivity }
        initSearch()
        backbutton.setOnClickListener { finish() }

    }

    override fun onPause() {
        super.onPause()
        imm.hideSoftInputFromWindow(search_text.windowToken, 0)
        compositeDisposable.clear()
    }

    private fun initSearch() {
        searchParent.setOnClickListener{
            imm.hideSoftInputFromWindow(search_text.windowToken, 0)
        }
        search_text.setTextChangeListener { searchText ->
            if (searchText != null && searchText.isNotEmpty()) {
                presenter.getSearchResult(searchText)
            }
        }
        search_text.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        search_text.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {

                    imm.hideSoftInputFromWindow(search_text.windowToken, 0)
                }
                else ->
                    // 기본 엔터키 동작
                {
                    imm.hideSoftInputFromWindow(search_text.windowToken, 0)
                    return@OnEditorActionListener false
                }

            }// 검색 동작
            true
        })


    }


    override fun noSearchResult() {
        rv_search.gone()
        no_search_result.visible()
    }

    override fun setSearchRV(restaurants: ArrayList<Restaurant>) {

        if (isFirstSearch) {
            rv_search.setting(RestaurantAdapter(this, restaurants), true, false, true)
            isFirstSearch = false
        } else {
            (rv_search.adapter as RestaurantAdapter).updateRestaurant(restaurants)
        }
        rv_search.visible()
        no_search_result.gone()
    }

    override fun errorSearch() {
        toast("error Search")
    }
}
