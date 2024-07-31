package com.arxlibertatis.single.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import moxy.MvpAppCompatFragment

abstract class SingleFragment : MvpAppCompatFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResourceId(), container, false)
        bindView(view)
        return view
    }

    protected abstract fun bindView(view: View)
    @LayoutRes
    protected abstract fun layoutResourceId(): Int
}