package com.arxlibertatis.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import com.arxlibertatis.R
import com.arxlibertatis.interfaces.MainFragmentView
import com.arxlibertatis.presenter.MainFragmentPresenter
import com.arxlibertatis.single.fragment.SingleFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import moxy.presenter.InjectPresenter

internal class MainFragment : SingleFragment(), MainFragmentView {

    @InjectPresenter
    lateinit var presenter: MainFragmentPresenter

    override fun layoutResourceId(): Int = R.layout.main_fragment

    override fun bindView(view: View) {
        val startGameButton = view.findViewById<FloatingActionButton>(R.id.startGameButton)
        startGameButton.setOnClickListener {
            presenter.onStartGameBtnClicked(requireContext())
        }
    }

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }
}