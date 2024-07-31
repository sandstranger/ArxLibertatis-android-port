package com.arxlibertatis.presenter

import android.content.Context
import com.arxlibertatis.engine.startEngine
import com.arxlibertatis.interfaces.MainFragmentView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class MainFragmentPresenter : MvpPresenter<MainFragmentView>() {
    fun onStartGameBtnClicked(context: Context) = startEngine(context)
}