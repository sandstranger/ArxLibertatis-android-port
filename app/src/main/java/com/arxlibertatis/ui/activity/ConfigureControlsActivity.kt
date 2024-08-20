package com.arxlibertatis.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.arxlibertatis.R
import com.arxlibertatis.databinding.ScreenControlsBinding
import com.arxlibertatis.engine.setFullscreen
import com.arxlibertatis.ui.controls.ScreenControlsManager
import com.arxlibertatis.utils.extensions.displayInCutoutArea

private const val CHANGE_OPACITY_STEP = 0.1f
private const val CHANGE_SIZE_STEP = 5

class ConfigureControlsActivity : AppCompatActivity() {

    private lateinit var screenControlsManager : ScreenControlsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)
        displayInCutoutArea(PreferenceManager.getDefaultSharedPreferences(this))
        val binding = ScreenControlsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.screenControlsRoot.post {
            screenControlsManager = ScreenControlsManager(binding, this)
            screenControlsManager.editScreenControls()
        }
    }

    fun clickOpacityPlus(v: View) {
        screenControlsManager.changeOpacity(CHANGE_OPACITY_STEP)
    }

    fun clickOpacityMinus(v: View) {
        screenControlsManager.changeOpacity(-1 * CHANGE_OPACITY_STEP)
    }

    fun clickSizePlus(v: View) {
        screenControlsManager.changeSize(CHANGE_SIZE_STEP)
    }

    fun clickSizeMinus(v: View) {
        screenControlsManager.changeSize(-1 * CHANGE_SIZE_STEP)
    }

    fun clickResetControls(v: View) {
        screenControlsManager.resetItems()
    }

    fun clickBack(v: View) {
        finish()
    }
}