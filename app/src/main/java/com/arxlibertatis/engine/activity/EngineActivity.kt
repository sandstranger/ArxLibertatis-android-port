package com.arxlibertatis.engine.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arxlibertatis.BuildConfig
import com.arxlibertatis.R
import com.arxlibertatis.databinding.ScreenControlsBinding
import com.arxlibertatis.engine.debugJniLibsArray
import com.arxlibertatis.engine.jniLibsArray
import com.arxlibertatis.engine.killEngine
import com.arxlibertatis.engine.setFullscreen
import com.arxlibertatis.ui.controls.ScreenControlsManager
import com.arxlibertatis.utils.MAIN_ENGINE_NATIVE_LIB
import org.libsdl.app.SDLActivity


class EngineActivity : SDLActivity () {

    private external fun resumeSound()

    private external fun pauseSound()

    override fun getMainSharedObject() = MAIN_ENGINE_NATIVE_LIB

    override fun getLibraries() = if (BuildConfig.DEBUG) debugJniLibsArray else jniLibsArray

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)

        val binding = ScreenControlsBinding.inflate(layoutInflater)

        window.addContentView(binding.root,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))

        val screenControlsManager = ScreenControlsManager(binding, this)
        screenControlsManager.enableScreenControls()
    }

    override fun onPause() {
        super.onPause()
        pauseSound()
    }

    override fun onResume() {
        super.onResume()
        resumeSound()
    }

    override fun onDestroy() {
        super.onDestroy()
        killEngine()
    }
}