package com.arxlibertatis.engine

import android.content.Context
import android.os.Environment
import android.os.Process
import android.system.Os
import com.arxlibertatis.engine.activity.EngineActivity
import com.arxlibertatis.utils.extensions.startActivity

internal const val MAIN_ENGINE_NATIVE_LIB = "libarx.so"
internal const val ARX_DATA_PATH_KEY = "ARX_DATA_PATH"
internal val debugJniLibsArray= arrayOf( "SDL2","epoxy","freetyped","z","openal","arx")
internal val jniLibsArray= arrayOf( "SDL2","epoxy","freetype","z","openal","arx")
internal val ARX_DATA_PATH = Environment.getExternalStorageDirectory().path + "/arx/"

fun killEngine() = Process.killProcess(Process.myPid())

fun startEngine(context: Context) {
    Os.setenv(ARX_DATA_PATH_KEY, ARX_DATA_PATH, true)
    context.startActivity<EngineActivity>()
}