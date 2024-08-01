package com.arxlibertatis.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arxlibertatis.databinding.MainFragmentBinding
import com.arxlibertatis.interfaces.MainFragmentView
import com.arxlibertatis.presenter.MainFragmentPresenter
import com.arxlibertatis.single.fragment.SingleFragment
import com.arxlibertatis.utils.extensions.requestExternalStoragePermission
import moxy.presenter.InjectPresenter

internal class MainFragment : SingleFragment<MainFragmentBinding>(), MainFragmentView {
    @InjectPresenter
    lateinit var presenter: MainFragmentPresenter

    override fun bindView(view: View, binding: MainFragmentBinding) {
        binding.startGameButton.setOnClickListener {
            presenter.onStartGameBtnClicked(requireContext())
        }
        requireActivity().requestExternalStoragePermission()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }
}