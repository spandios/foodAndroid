package com.example.fooddeuk.dangol


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.fooddeuk.R

class DanGolFragment : Fragment() {
    lateinit var root: LinearLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_dangol, container, false)
        this.root = root as LinearLayout
        return root
    }

}
