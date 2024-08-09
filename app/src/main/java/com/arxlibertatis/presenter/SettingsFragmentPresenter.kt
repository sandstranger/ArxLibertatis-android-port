package com.arxlibertatis.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.DocumentsContract
import com.arxlibertatis.utils.ASFUriHelper.getPath
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY

class SettingsFragmentPresenter {

    fun saveGamePath (data: Intent?, context : Context, prefs : SharedPreferences ){
        val uri = data!!.data
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )

        with(prefs.edit()){
            putString(GAME_FILES_SHARED_PREFS_KEY, getPath(context, docUri))
            apply()
        }
    }
}