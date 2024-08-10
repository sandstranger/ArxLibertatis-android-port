package com.arxlibertatis.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.DocumentsContract
import com.arxlibertatis.utils.ASFUriHelper.getPath
import com.arxlibertatis.utils.GAME_ASSETS_WERE_COPIED_PREFS_KEY
import com.arxlibertatis.utils.GAME_FILES_FOLDER_NAME
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.copyGameAssets
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView

@InjectViewState
class SettingsFragmentPresenter : MvpPresenter<MvpView>() {

    private lateinit var currentGamePath: String
    private lateinit var prefs: SharedPreferences
    private lateinit var context: Context
    private var onSharedPrefsChanged: (prefsKey: String) -> Unit = {}

    fun init (prefs: SharedPreferences,
              context: Context,
              onSharedPrefsChanged: (prefsKey: String) -> Unit){
        this.onSharedPrefsChanged = onSharedPrefsChanged
        this.context = context
        this.prefs = prefs
        currentGamePath = prefs.getString(GAME_FILES_SHARED_PREFS_KEY,"") ?: ""
    }

    fun saveGamePath(data: Intent) {
        val uri = data.data
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )
        currentGamePath = getPath(context, docUri)
        copyGameAssets()
        with(prefs.edit()) {
            putString(GAME_FILES_SHARED_PREFS_KEY, currentGamePath)
            apply()
            onSharedPrefsChanged?.invoke(GAME_FILES_SHARED_PREFS_KEY)
        }
    }

    fun copyGameAssets() {
        if (currentGamePath.isNullOrEmpty()){
            return
        }
        copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
        with(prefs.edit()){
            putBoolean(GAME_ASSETS_WERE_COPIED_PREFS_KEY, true)
            apply()
        }
    }
}