package com.arxlibertatis.engine.activity

import android.os.Bundle
import com.arxlibertatis.BuildConfig
import com.arxlibertatis.engine.MAIN_ENGINE_NATIVE_LIB
import com.arxlibertatis.engine.debugJniLibsArray
import com.arxlibertatis.engine.jniLibsArray
import com.arxlibertatis.engine.killEngine
import org.libsdl.app.SDLActivity

class EngineActivity : SDLActivity () {

    private external fun resumeSound()

    private external fun pauseSound()

    override fun getMainSharedObject() = MAIN_ENGINE_NATIVE_LIB

    override fun getLibraries() = if (BuildConfig.DEBUG) debugJniLibsArray else jniLibsArray

    override fun getMainFunction() = "SDL_main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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