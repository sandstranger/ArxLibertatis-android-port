package com.arxlibertatis.presenter

import android.content.Context
import android.content.SharedPreferences
import com.arxlibertatis.interfaces.SettingsFragmentMvpView
import com.arxlibertatis.ui.activity.ConfigureControlsActivity
import com.arxlibertatis.utils.GAME_FILES_FOLDER_NAME
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.copyGameAssets
import com.arxlibertatis.utils.extensions.startActivity
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class SettingsFragmentPresenter : MvpPresenter<SettingsFragmentMvpView>() {

    fun onConfigureScreenControlsClicked (context: Context){
        context.startActivity<ConfigureControlsActivity>(finishParentActivity = false)
    }

    fun saveGamePath(currentGamePath: String, context: Context, preferences: SharedPreferences) {
        with(preferences.edit()) {
            putString(GAME_FILES_SHARED_PREFS_KEY, currentGamePath)
            apply()
            copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
            viewState?.updatePreference(GAME_FILES_SHARED_PREFS_KEY)
        }
    }

    fun copyGameAssets(context: Context,preferences: SharedPreferences) {
        val currentGamePath = preferences.getString(GAME_FILES_SHARED_PREFS_KEY, "")

        if (currentGamePath.isNullOrEmpty()){
            return
        }

        copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
    }
}