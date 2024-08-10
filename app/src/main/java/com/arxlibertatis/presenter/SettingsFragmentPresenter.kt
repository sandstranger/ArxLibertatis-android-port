package com.arxlibertatis.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.DocumentsContract
import com.arxlibertatis.interfaces.SettingsFragmentMvpView
import com.arxlibertatis.utils.ASFUriHelper.getPath
import com.arxlibertatis.utils.GAME_ASSETS_WERE_COPIED_PREFS_KEY
import com.arxlibertatis.utils.GAME_FILES_FOLDER_NAME
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.copyGameAssets
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class SettingsFragmentPresenter : MvpPresenter<SettingsFragmentMvpView>() {

    fun saveGamePath(data: Intent, context: Context, preferences: SharedPreferences) {
        val uri = data.data
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )

        with(preferences.edit()) {
            putString(GAME_FILES_SHARED_PREFS_KEY, getPath(context, docUri))
            apply()
            copyGameAssets(context,preferences)
            viewState?.updatePreference(GAME_FILES_SHARED_PREFS_KEY)
        }
    }

    fun copyGameAssets(context: Context,preferences: SharedPreferences) {
        val currentGamePath = preferences.getString(GAME_FILES_SHARED_PREFS_KEY, "")

        if (currentGamePath.isNullOrEmpty()){
            return
        }

        copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
        with(preferences.edit()){
            putBoolean(GAME_ASSETS_WERE_COPIED_PREFS_KEY, true)
            apply()
        }
    }
}