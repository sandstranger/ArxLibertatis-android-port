package com.arxlibertatis.engine

import android.content.Context
import android.content.SharedPreferences
import android.os.Process
import android.system.Os
import android.view.View
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import com.arxlibertatis.R
import com.arxlibertatis.engine.activity.EngineActivity
import com.arxlibertatis.utils.ARX_DATA_PATH_KEY
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.HIDE_SCREEN_CONTROLS_KEY
import com.arxlibertatis.utils.extensions.startActivity
import org.ini4j.Wini
import org.libsdl.app.SDLSurface
import java.io.File


internal val debugJniLibsArray= arrayOf("GL", "SDL2","freetyped","z","openal","arx")
internal val jniLibsArray= arrayOf("GL", "SDL2","freetype","z","openal","arx")
private const val RESOLUTION_DELIMITER = "x"

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
    val gamePath  = prefs.getString(GAME_FILES_SHARED_PREFS_KEY, "")
    val hideScreenControls = prefs.getBoolean(HIDE_SCREEN_CONTROLS_KEY, false)
    Os.setenv("HIDE_SCREEN_CONTROLS", hideScreenControls.toString().lowercase(), true)
    Os.setenv(ARX_DATA_PATH_KEY, gamePath, true)
    Os.setenv("LIBGL_ES", "2", true)

    val cfgIniFile = File("$gamePath/cfg.ini")

    if (!cfgIniFile.exists()){
        MaterialDialog(context).show {
            message(R.string.can_not_start_game)
            positiveButton(R.string.ok_text)
        }
        return
    }

    updateCfgIni(cfgIniFile, prefs)
    context.startActivity<EngineActivity>()
}

private fun updateCfgIni (cfgIniFile : File, prefs: SharedPreferences){
    val ini = Wini(cfgIniFile)
    val customResolution = prefs.getString("custom_resolution", "")
    var iniFileWasChanged = false
    if (!customResolution.isNullOrEmpty() && customResolution.contains(RESOLUTION_DELIMITER)){
        try {
            val resolutionsArray = customResolution.split(RESOLUTION_DELIMITER)
            val screenWidth = Integer.parseInt(resolutionsArray[0])
            val screenHeight = Integer.parseInt(resolutionsArray[1])
            SDLSurface.fixedWidth = screenWidth
            SDLSurface.fixedHeight = screenHeight
            ini.put("video","resolution", "\"$customResolution\"")
            iniFileWasChanged = true
        }
        catch (e: Exception){

        }

        if (iniFileWasChanged){
            ini.store()
        }
    }
}

