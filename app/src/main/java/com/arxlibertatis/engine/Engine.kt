package com.arxlibertatis.engine

import android.content.Context
import android.os.Environment
import android.os.Process
import android.system.Os
import android.util.Log
import android.view.View
import com.arxlibertatis.engine.activity.EngineActivity
import com.arxlibertatis.utils.extensions.startActivity

internal const val MAIN_ENGINE_NATIVE_LIB = "libarx.so"
internal const val ARX_DATA_PATH_KEY = "ARX_DATA_PATH"
internal val debugJniLibsArray= arrayOf("GL", "SDL2","freetyped","z","openal","arx")
internal val jniLibsArray= arrayOf("GL", "SDL2","freetype","z","openal","arx")
internal val ARX_DATA_PATH = Environment.getExternalStorageDirectory().path + "/arx/"

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
    Os.setenv("LIBGL_ES", "2", true)
    Os.setenv(ARX_DATA_PATH_KEY, ARX_DATA_PATH, true)
    context.startActivity<EngineActivity>()
}

