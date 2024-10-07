package com.arxlibertatis.engine.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.arxlibertatis.BuildConfig
import com.arxlibertatis.databinding.ScreenControlsBinding
import com.arxlibertatis.engine.debugJniLibsArray
import com.arxlibertatis.engine.jniLibsArray
import com.arxlibertatis.engine.killEngine
import com.arxlibertatis.engine.setFullscreen
import com.arxlibertatis.ui.controls.ScreenControlsManager
import com.arxlibertatis.utils.HIDE_SCREEN_CONTROLS_KEY
import com.arxlibertatis.utils.MAIN_ENGINE_NATIVE_LIB
import com.arxlibertatis.utils.extensions.displayInCutoutArea
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.libsdl.app.SDLActivity
import java.io.File


class EngineActivity : SDLActivity () {

    private lateinit var screenControlsManager : ScreenControlsManager
    private val screenControlsVisibilityUpdater = CoroutineScope(Dispatchers.Default)
    private var needToShowControlsLastState : Boolean = false
    private lateinit var prefsManager : SharedPreferences
    private lateinit var logcatProcess : Process

    private external fun resumeSound()

    private external fun pauseSound()

    private external fun needToShowScreenControls () : Boolean

    override fun getMainSharedObject() = MAIN_ENGINE_NATIVE_LIB

    override fun getLibraries() = if (BuildConfig.DEBUG) debugJniLibsArray else jniLibsArray

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        logcatProcess = enableLogcat()
        super.onCreate(savedInstanceState)
        prefsManager = PreferenceManager.getDefaultSharedPreferences(this)
        displayInCutoutArea(prefsManager)
        initScreenControls()
    }

    override fun onPause() {
        super.onPause()
        screenControlsManager?.onPause()
        pauseSound()
    }

    override fun onResume() {
        super.onResume()
        resumeSound()
    }

    override fun onDestroy() {
        super.onDestroy()
        screenControlsVisibilityUpdater.cancel()
        logcatProcess.destroy()
        killEngine()
    }

    override fun getArguments(): Array<String> {
        val commandLineArgs = prefsManager.getString("command_line", "")

        if (commandLineArgs.isNullOrEmpty() || !commandLineArgs.contains("-")){
            return super.getArguments()
        }

        try {
            val args = arrayListOf<String>()

            commandLineArgs.split(" ".toRegex()).forEach {
                if (!it.isNullOrEmpty()){
                        args +=it
                }
            }

            return args.toTypedArray()
        }
        catch (e: Exception) {
            return super.getArguments()
        }
    }

    private fun initScreenControls (){
        val hideScreenControls = prefsManager.getBoolean(HIDE_SCREEN_CONTROLS_KEY,false)

        if (!hideScreenControls) {
            val binding = ScreenControlsBinding.inflate(layoutInflater)

            window.addContentView(
                binding.root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            binding.screenControlsRoot.post {
                screenControlsManager = ScreenControlsManager(binding, this)
                screenControlsManager.enableScreenControls()

                screenControlsVisibilityUpdater.launch {
                    changeScreenControlsVisibility()
                }
            }
        }
    }

    private suspend fun changeScreenControlsVisibility(){
        while (true){
            val needToShowControls = needToShowScreenControls()
            if (needToShowControls != needToShowControlsLastState){
                this@EngineActivity.runOnUiThread {
                    if (needToShowControls) {
                        screenControlsManager.showScreenControls()
                    } else {
                        screenControlsManager.hideScreenControls()
                    }
                }
            }
            needToShowControlsLastState = needToShowControls
            delay(200)
        }
    }

    private fun enableLogcat() : Process {
        val pathToLog = "${Environment.getExternalStorageDirectory()}/Arx"

        val logcatFile = File(pathToLog)
        if (logcatFile.exists()) {
            logcatFile.delete()
        }

        val processBuilder = ProcessBuilder()
        val commandToExecute = arrayOf("/system/bin/sh", "-c", "logcat *:W -d -f $pathToLog")
        processBuilder.command(*commandToExecute)
        processBuilder.redirectErrorStream(true)
        return processBuilder.start()
    }
}