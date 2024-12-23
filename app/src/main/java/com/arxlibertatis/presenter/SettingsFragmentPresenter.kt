package com.arxlibertatis.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Environment
import com.arxlibertatis.interfaces.SettingsFragmentMvpView
import com.arxlibertatis.ui.activity.ConfigureControlsActivity
import com.arxlibertatis.utils.GAME_FILES_FOLDER_NAME
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.INTERNAL_MEMORY_PATH_TO_CACHE_KEY
import com.arxlibertatis.utils.copyGameAssets
import com.arxlibertatis.utils.extensions.createInternalPathToCache
import com.arxlibertatis.utils.extensions.getInternalPathToCache
import com.arxlibertatis.utils.extensions.startActivity
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class SettingsFragmentPresenter : MvpPresenter<SettingsFragmentMvpView>() {

    fun onConfigureScreenControlsClicked(context: Context) {
        context.startActivity<ConfigureControlsActivity>(finishParentActivity = false)
    }

    fun saveGamePath(data: Intent, context: Context, preferences: SharedPreferences) {
        data.data?.also { uri ->
            val pattern = Regex("[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}")
            val storageDir = Environment.getExternalStorageDirectory()
            val storagePath = storageDir.absolutePath
            val modifiedStoragePath = "/storage"
            val pathSegment = uri.lastPathSegment
            val currentGamePath = if (pattern.containsMatchIn(pathSegment ?: "")) {
                modifiedStoragePath + "/" + pathSegment?.replace(":", "/")
            } else {
                storagePath + "/" + pathSegment?.replace("primary:", "")
            }

            saveGamePath(currentGamePath, context, preferences)
        }
    }

    fun saveGamePath(currentGamePath: String, context: Context, preferences: SharedPreferences) {
        with(preferences.edit()) {
            putString(GAME_FILES_SHARED_PREFS_KEY, currentGamePath)
            apply()
            copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
            viewState?.updatePreference(GAME_FILES_SHARED_PREFS_KEY)
        }
    }

    fun copyGameAssets(context: Context, preferences: SharedPreferences) {
        val useInternalMemoryCache = preferences.getBoolean(INTERNAL_MEMORY_PATH_TO_CACHE_KEY,false)

        if (useInternalMemoryCache){
            context.createInternalPathToCache()
            copyGameAssets(context, GAME_FILES_FOLDER_NAME, context.getInternalPathToCache())
            return
        }

        val currentGamePath = preferences.getString(GAME_FILES_SHARED_PREFS_KEY, "")

        if (currentGamePath.isNullOrEmpty()) {
            return
        }

        copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
    }
}