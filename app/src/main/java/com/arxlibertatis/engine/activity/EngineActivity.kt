package com.arxlibertatis.engine.activity

import com.arxlibertatis.engine.MAIN_ENGINE_NATIVE_LIB
import com.arxlibertatis.engine.debugJniLibsArray
import com.arxlibertatis.engine.jniLibsArray
import com.arxlibertatis.engine.killEngine
import org.libsdl.app.SDLActivity

class EngineActivity : SDLActivity () {

    override fun getMainSharedObject() = MAIN_ENGINE_NATIVE_LIB

    override fun getLibraries() = jniLibsArray

    override fun getMainFunction() = "SDL_main"

    override fun onDestroy() {
        super.onDestroy()
        killEngine()
    }
}