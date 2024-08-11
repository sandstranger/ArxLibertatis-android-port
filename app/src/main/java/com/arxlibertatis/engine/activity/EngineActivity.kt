package com.arxlibertatis.engine.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arxlibertatis.BuildConfig
import com.arxlibertatis.R
import com.arxlibertatis.engine.debugJniLibsArray
import com.arxlibertatis.engine.jniLibsArray
import com.arxlibertatis.engine.killEngine
import com.arxlibertatis.engine.setFullscreen
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

        val inflater: LayoutInflater = this.layoutInflater
        this.window.addContentView(
            inflater.inflate(R.layout.screen_controls, null),
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
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