package com.arxlibertatis.presenter

import android.app.Activity
import android.content.Context
import com.arxlibertatis.engine.startEngine
import com.arxlibertatis.utils.extensions.requestExternalStoragePermission
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView

@InjectViewState
class MainActivityPresenter : MvpPresenter<MvpView>() {
    internal fun onStartGameBtnClicked(context: Context) = startEngine(context)

    internal fun requestExternalStorage (activity : Activity) = activity.requestExternalStoragePermission()
}