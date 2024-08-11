package com.arxlibertatis.ui.controls

import android.app.Activity
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.preference.PreferenceManager
import com.arxlibertatis.R
import com.arxlibertatis.databinding.ScreenControlsBinding

const val VIRTUAL_SCREEN_WIDTH = 1024
const val VIRTUAL_SCREEN_HEIGHT = 768
const val CONTROL_DEFAULT_SIZE = 70

class ScreenControlsManager (
    private val screenControlsBinding: ScreenControlsBinding,
    private val activity : Activity) {

    private val controlsItems = arrayListOf<ControlsItem>()
    private val screenSize: ScreenSize = getScreenSize()
    private val joystickHolder : JoystickHolder = JoystickHolder(screenControlsBinding.joystick)

    init {
        joystickHolder.joystick.enable = false

        controlsItems += ControlsItem ("joystick",joystickHolder.joystick,
            30, 330, 280)

        controlsItems.forEach {
            it.loadPrefs()
        }
    }

    fun editScreenControls (){
        controlsItems.forEach {
            val callback = ConfigureCallback(screenSize)
            it.view.setOnTouchListener(callback)
        }
        screenControlsBinding.screenControlsRoot.setBackgroundColor(Color.GRAY)
    }

    fun enableScreenControls (){
        joystickHolder.joystick.enable = true
        screenControlsBinding.touchCamera.visibility = View.VISIBLE
    }

    private fun getScreenSize () : ScreenSize {
        val displayMetrics = DisplayMetrics();
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics);
        return ScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    private inner class ControlsItem (val uniqueId: String,
                        val view : View,
                        val defaultX: Int,
                        val defaultY: Int,
                        val defaultSize : Int = CONTROL_DEFAULT_SIZE,
                        val defaultOpacity : Float = 0.5f,
                        val visible: Boolean = true){

        private var opacity = defaultOpacity
        var size = defaultSize
        var x = defaultX
        var y = defaultY

        init {
            view.tag = this
        }

        fun changeOpacity(delta: Float) {
            opacity = Math.max(0f, Math.min(opacity + delta, 1.0f))
            savePrefs()
        }

        fun changeSize(delta: Int) {
            size = Math.max(0, size + delta)
            savePrefs()
        }

        fun changePosition(virtualX: Int, virtualY: Int) {
            x = virtualX
            y = virtualY
            savePrefs()
        }

        fun updateView() {
            val v = view ?: return
            val realScreenWidth = this@ScreenControlsManager.screenSize.width
            val realScreenHeight = this@ScreenControlsManager.screenSize.height
            val realX = x * realScreenWidth / VIRTUAL_SCREEN_WIDTH
            val realY = y * realScreenHeight / VIRTUAL_SCREEN_HEIGHT

            val screenSize = (1.0 * size * realScreenWidth / VIRTUAL_SCREEN_WIDTH).toInt()
            val params = FrameLayout.LayoutParams(screenSize, screenSize)

            params.leftMargin = realX
            params.topMargin = realY

            v.layoutParams = params

            v.alpha = opacity
        }

        private fun savePrefs() {
            val v = view ?: return
            val prefs = PreferenceManager.getDefaultSharedPreferences(v.context)
            with (prefs.edit()) {
                putFloat("osc:$uniqueId:opacity", opacity)
                putInt("osc:$uniqueId:size", size)
                putInt("osc:$uniqueId:x", x)
                putInt("osc:$uniqueId:y", y)

                commit()
            }
        }

        fun loadPrefs() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(view.context)

            opacity = prefs.getFloat("osc:$uniqueId:opacity", defaultOpacity)
            size = prefs.getInt("osc:$uniqueId:size", defaultSize)
            x = prefs.getInt("osc:$uniqueId:x", defaultX)
            y = prefs.getInt("osc:$uniqueId:y", defaultY)

            updateView()
        }

        fun resetPrefs() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(view.context)

            with (prefs.edit()) {
                remove("osc:$uniqueId:opacity")
                remove("osc:$uniqueId:size")
                remove("osc:$uniqueId:x")
                remove("osc:$uniqueId:y")

                commit()
            }

            loadPrefs()
        }
    }

    private data class ScreenSize (val width: Int,val height : Int)

    private class ConfigureCallback(val screenSize: ScreenSize) : View.OnTouchListener {

        var currentView: View? = null
        private var origX: Float = 0.0f
        private var origY: Float = 0.0f
        private var startX: Float = 0.0f
        private var startY: Float = 0.0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    currentView?.setBackgroundColor(Color.TRANSPARENT)
                    currentView = v
                    v.setBackgroundColor(Color.RED)
                    origX = v.x
                    origY = v.y
                    startX = event.rawX
                    startY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> if (currentView != null) {
                    val view = currentView!!
                    val x = ((event.rawX - startX) + origX).toInt()
                    val y = ((event.rawY - startY) + origY).toInt()

                    val el = view.tag as ControlsItem
                    el.changePosition(x * VIRTUAL_SCREEN_WIDTH / screenSize.width, y * VIRTUAL_SCREEN_HEIGHT / screenSize.height)
                    el.updateView()
                }
            }

            return true
        }
    }

}