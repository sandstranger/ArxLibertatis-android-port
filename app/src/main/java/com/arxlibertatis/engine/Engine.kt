package com.arxlibertatis.engine

import android.content.Context
import android.os.Environment
import android.os.Process
import android.system.Os
import android.view.View
import androidx.preference.PreferenceManager
import com.arxlibertatis.engine.activity.EngineActivity
import com.arxlibertatis.utils.ARX_DATA_PATH_KEY
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.extensions.startActivity

internal val debugJniLibsArray= arrayOf("GL", "SDL2","freetyped","z","openal","arx")
internal val jniLibsArray= arrayOf("GL", "SDL2","freetype","z","openal","arx")
internal val DEFAULT_ARX_DATA_PATH = Environment.getExternalStorageDirectory().path + "/arx/"

@Suppress("DEPRECATION")
internal fun setFullscreen(decorView: View) {
    val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    decorView.systemUiVisibility = uiOptions
}

fun killEngine() = Process.killProcess(Process.myPid())

fun startEngine(context: Context) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val gamePath  = prefs.getString(GAME_FILES_SHARED_PREFS_KEY, DEFAULT_ARX_DATA_PATH)
    Os.setenv(ARX_DATA_PATH_KEY, gamePath, true)
    Os.setenv("LIBGL_ES", "2", true)
    context.startActivity<EngineActivity>()
}

