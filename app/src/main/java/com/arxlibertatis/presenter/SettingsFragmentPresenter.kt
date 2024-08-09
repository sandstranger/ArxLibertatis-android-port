package com.arxlibertatis.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.DocumentsContract
import com.arxlibertatis.utils.ASFUriHelper.getPath
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView

@InjectViewState
class SettingsFragmentPresenter : MvpPresenter<MvpView>() {

    private var onSharedPrefsChanged : (prefsKey : String) -> Unit = {}

    var sharedPrefsChanged : (prefsKey : String) -> Unit
        get() {
            return onSharedPrefsChanged
        }
        set(value) { onSharedPrefsChanged = value }

    fun saveGamePath (data: Intent, context : Context, prefs : SharedPreferences ){
        val uri = data.data
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )

        with(prefs.edit()){
            putString(GAME_FILES_SHARED_PREFS_KEY, getPath(context, docUri))
            apply()
            onSharedPrefsChanged?.invoke(GAME_FILES_SHARED_PREFS_KEY)
        }
    }
}