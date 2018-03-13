package com.example.fooddeuk.pick.dangol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.fooddeuk.R
import com.example.fooddeuk.pick.PickAdapter
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.setting
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_dangol_list.*

class DangolListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dangol_list)
        RxBus.intentSubscribe(RxBus.DangolListData,this.javaClass, Consumer {
            if(it is PickAdapter){
                dangol_list_rv.setting(it,true,true,true)
            }
        })
    }
}
