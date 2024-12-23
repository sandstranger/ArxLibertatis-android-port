package com.arxlibertatis.engine

import android.content.Context
import android.content.SharedPreferences
import android.os.Process
import android.system.Os
import android.util.Log
import android.view.View
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import com.arxlibertatis.R
import com.arxlibertatis.engine.activity.EngineActivity
import com.arxlibertatis.utils.ARX_DATA_PATH_KEY
import com.arxlibertatis.utils.CUSTOM_RESOLUTION_PREFS_KEY
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.HIDE_SCREEN_CONTROLS_KEY
import com.arxlibertatis.utils.INTERNAL_MEMORY_PATH_TO_CACHE_KEY
import com.arxlibertatis.utils.extensions.getInternalPathToCache
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
    val gamePath  = if (prefs.getBoolean(INTERNAL_MEMORY_PATH_TO_CACHE_KEY,false)) context.getInternalPathToCache() else
        prefs.getString(GAME_FILES_SHARED_PREFS_KEY, "")
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

    updateCfgIni(cfgIniFile, prefs, hideScreenControls)
    context.startActivity<EngineActivity>()
}

private fun updateCfgIni(cfgIniFile: File, prefs: SharedPreferences, hideScreenControls : Boolean) {
    val ini = Wini(cfgIniFile)
    val customResolution = prefs.getString(CUSTOM_RESOLUTION_PREFS_KEY, "")
    var iniFileWasChanged = false
    if (!customResolution.isNullOrEmpty() && customResolution.contains(RESOLUTION_DELIMITER)) {
        try {
            val resolutionsArray = customResolution.split(RESOLUTION_DELIMITER)
            SDLSurface.fixedWidth = Integer.parseInt(resolutionsArray[0])
            SDLSurface.fixedHeight = Integer.parseInt(resolutionsArray[1])
            ini.put("video", "resolution", "\"$customResolution\"")
            iniFileWasChanged = true
        } catch (e: Exception) {
        }
    }

    fun writeValue (sectionName : String, optionName : String){
        val valueToWrite = prefs.getString(optionName, "")
        if (!valueToWrite.isNullOrEmpty()) {
            ini.put(sectionName, optionName, valueToWrite)
            iniFileWasChanged = true
        }
    }

    fun writeValue (sectionName : String, optionName : String, prefsName:String){
        val valueToWrite = prefs.getString(prefsName, "")
        if (!valueToWrite.isNullOrEmpty()) {
            ini.put(sectionName, optionName, valueToWrite)
            iniFileWasChanged = true
        }
    }

    writeValue("interface", "hud_scale")
    writeValue("interface", "cursor_scale")
    writeValue("interface", "font_size")
    writeValue("language","string", "text_localization")
    writeValue("language","audio", "audio_localization")
    ini.put("key","action_combine_k0",if (hideScreenControls) "Button1" else "Button3" )

    if (iniFileWasChanged) {
        ini.store()
    }
}


