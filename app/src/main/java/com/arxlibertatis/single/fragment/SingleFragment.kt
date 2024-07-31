package com.arxlibertatis.single.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.arxlibertatis.databinding.MainFragmentBinding
import moxy.MvpAppCompatFragment

abstract class SingleFragment <T> : MvpAppCompatFragment() where T : ViewBinding {

    protected var _binding: T? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        inflateView(inflater,container)
        val view = binding.root
        bindView(view, binding)
        return view
    }

    protected abstract fun inflateView (inflater: LayoutInflater,container: ViewGroup?)

    protected abstract fun bindView(view: View, binding: T)
}