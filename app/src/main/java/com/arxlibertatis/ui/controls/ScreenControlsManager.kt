package com.arxlibertatis.ui.controls

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.preference.PreferenceManager
import com.arxlibertatis.databinding.ScreenControlsBinding
import com.arxlibertatis.ui.controls.views.SDLImageButton


const val VIRTUAL_SCREEN_WIDTH = 1024
const val VIRTUAL_SCREEN_HEIGHT = 768
const val CONTROL_DEFAULT_SIZE = 70

private const val MIDDLE_MOUSE_BUTTON_ID = 2
private const val RIGHT_MOUSE_BUTTON_ID = 3

class ScreenControlsManager(
    private val screenControlsBinding: ScreenControlsBinding,
    private val activity: Activity
) {

    private var callback: ConfigureCallback? = null
    private val controlsItems = arrayListOf<ControlsItem>()
    private val specialButtons = arrayListOf<SDLImageButton>()
    private val screenSize: ScreenSize = getScreenSize()
    private val joystickHolder: SDLJoystick = SDLJoystick(screenControlsBinding.joystick)

    init {
        joystickHolder.joystick.enable = false

        controlsItems += ControlsItem(
            "joystick", joystickHolder.joystick,
            30, 330, 280
        )
        controlsItems += ControlsItem(
            "attack_button", screenControlsBinding.attackButton.setKeycode(MIDDLE_MOUSE_BUTTON_ID),
            800, 310, 130
        )
        controlsItems += ControlsItem(
            "drop_weapon_button",
            screenControlsBinding.dropWeaponButton.setKeycode(KeyEvent.KEYCODE_B),
            700,
            310,
            70
        )
        controlsItems += ControlsItem(
            "sneak_button", screenControlsBinding.sneakButton.setKeycode(KeyEvent.KEYCODE_C),
            870, 10, 70
        )
        controlsItems += ControlsItem(
            "pause_button", screenControlsBinding.pauseButton.setKeycode(KeyEvent.KEYCODE_ESCAPE),
            940, 10, 70
        )
        controlsItems += ControlsItem(
            "hide_extra_btns_button", screenControlsBinding.hideExtraBtnsButton,
            70, 10, 70
        )
        controlsItems += ControlsItem(
            "health_potion_button",
            screenControlsBinding.healthPotionButton.setKeycode(KeyEvent.KEYCODE_H),
            70,
            120,
            70
        )
        controlsItems += ControlsItem(
            "magic_potion_button",
            screenControlsBinding.magicPotionButton.setKeycode(KeyEvent.KEYCODE_G),
            140,
            120,
            70
        )
        controlsItems += ControlsItem(
            "torch_potion_button", screenControlsBinding.torchButton.setKeycode(KeyEvent.KEYCODE_J),
            210, 120, 70
        )
        controlsItems += ControlsItem(
            "hide_all_btns_button", screenControlsBinding.hideAllBtnsButton,
            450, 10, 70
        )
        controlsItems += ControlsItem(
            "character_stats_button",
            screenControlsBinding.showCharacterStatsButton.setKeycode(KeyEvent.KEYCODE_F1),
            140,
            10,
            70
        )
        controlsItems += ControlsItem(
            "show_map_button", screenControlsBinding.showMapButton.setKeycode(KeyEvent.KEYCODE_F3),
            210, 10, 70
        )
        controlsItems += ControlsItem(
            "show_spells_button",
            screenControlsBinding.showSpellsButton.setKeycode(KeyEvent.KEYCODE_F2),
            280,
            10,
            70
        )
        controlsItems += ControlsItem(
            "show_notes_button",
            screenControlsBinding.showNotesButton.setKeycode(KeyEvent.KEYCODE_F4),
            350,
            10,
            70
        )
        controlsItems += ControlsItem(
            "inventory_button",
            screenControlsBinding.inventoryButton.setKeycode(RIGHT_MOUSE_BUTTON_ID),
            940,
            130,
            70
        )
        controlsItems += ControlsItem(
            "use_button", screenControlsBinding.useButton.setKeycode(KeyEvent.KEYCODE_F),
            870, 130, 70
        )
        controlsItems += ControlsItem(
            "turn_left_button", screenControlsBinding.turnLeftButton.setKeycode(KeyEvent.KEYCODE_Q),
            700, 130, 70
        )
        controlsItems += ControlsItem(
            "turn_right_button",
            screenControlsBinding.turnRightButton.setKeycode(KeyEvent.KEYCODE_E),
            790,
            130,
            70
        )
        controlsItems += ControlsItem(
            "load_button", screenControlsBinding.quickLoadButton.setKeycode(KeyEvent.KEYCODE_F9),
            810, 10, 70
        )
        controlsItems += ControlsItem(
            "save_button", screenControlsBinding.quickSaveButton.setKeycode(KeyEvent.KEYCODE_F5),
            760, 10, 70
        )
        controlsItems += ControlsItem(
            "toggle_use_button",
            screenControlsBinding.toggleUseButton.setKeycode(MIDDLE_MOUSE_BUTTON_ID),
            940,
            250,
            70
        )
        controlsItems += ControlsItem(
            "toggle_magic_button",
            screenControlsBinding.toggleMagicButton.setKeycode(KeyEvent.KEYCODE_CTRL_LEFT),
            950,
            390,
            70
        )
        controlsItems += ControlsItem(
            "toggle_weapon_button",
            screenControlsBinding.toggleWeaponButton.setKeycode(KeyEvent.KEYCODE_TAB),
            930,
            530,
            70
        )
        controlsItems += ControlsItem(
            "jump_button", screenControlsBinding.jumpButton.setKeycode(KeyEvent.KEYCODE_SPACE),
            830, 570, 70
        )
        controlsItems += ControlsItem(
            "walk_button", screenControlsBinding.walkButton.setKeycode(KeyEvent.KEYCODE_SHIFT_LEFT),
            730, 570, 70
        )

        screenControlsBinding.showCharacterStatsButton.isSpecialBtn = true
        specialButtons += screenControlsBinding.showCharacterStatsButton

        screenControlsBinding.showMapButton.isSpecialBtn = true
        specialButtons += screenControlsBinding.showMapButton

        screenControlsBinding.showSpellsButton.isSpecialBtn = true
        specialButtons += screenControlsBinding.showSpellsButton

        screenControlsBinding.showNotesButton.isSpecialBtn = true
        specialButtons += screenControlsBinding.showNotesButton

        screenControlsBinding.healthPotionButton.isSpecialBtn = true
        specialButtons += screenControlsBinding.healthPotionButton

        screenControlsBinding.magicPotionButton.isSpecialBtn = true
        specialButtons += screenControlsBinding.magicPotionButton

        screenControlsBinding.torchButton.isSpecialBtn = true
        specialButtons += screenControlsBinding.torchButton

        controlsItems.forEach {
            it.loadPrefs()
        }
    }

    fun editScreenControls() {
        callback = ConfigureCallback(screenControlsBinding.screenControlsRoot)
        controlsItems.forEach {
            it.view.setOnTouchListener(callback)

            if (it.view is SDLImageButton) {
                it.view.interactable = false
            }
        }
        screenControlsBinding.buttonsHolder.visibility = View.VISIBLE
        screenControlsBinding.screenControlsRoot.setBackgroundColor(Color.GRAY)
    }

    fun enableScreenControls() {
        joystickHolder.joystick.enable = true
        screenControlsBinding.buttonsHolder.visibility = View.GONE
        screenControlsBinding.touchCamera.visibility = View.VISIBLE

        for (specialBtn in specialButtons) {
            specialBtn.visibility = View.GONE
        }

        screenControlsBinding.hideExtraBtnsButton.setOnClickListener {
            for (specialBtn in specialButtons) {
                specialBtn.visibility =
                    if (specialBtn.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        screenControlsBinding.hideAllBtnsButton.setOnClickListener {
            for (button in controlsItems) {
                if (button.view === screenControlsBinding.hideAllBtnsButton) {
                    continue
                }

                if (button.view is SDLImageButton && button.view.isSpecialBtn && button.view.visibility == View.GONE){
                    continue
                }

                button.view.visibility =
                    if (button.view.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
    }

    fun changeOpacity(delta: Float) {
        val view = callback?.currentView ?: return
        val el = view.tag as ControlsItem
        el.changeOpacity(delta)
        el.updateView()
    }

    fun changeSize(delta: Int) {
        val view = callback?.currentView ?: return
        val el = view.tag as ControlsItem
        el.changeSize(delta)
        el.updateView()
    }

    fun resetItems() {
        controlsItems.forEach {
            it.resetPrefs()
        }
    }

    private fun getScreenSize(): ScreenSize {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics: WindowMetrics =
                activity.getSystemService(WindowManager::class.java).currentWindowMetrics
            return ScreenSize(metrics.bounds.width(), metrics.bounds.height())
        } else {
            val displayMetrics = DisplayMetrics();
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics);
            return ScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }

    private inner class ControlsItem(
        val uniqueId: String,
        val view: View,
        val defaultX: Int,
        val defaultY: Int,
        val defaultSize: Int = CONTROL_DEFAULT_SIZE,
        val defaultOpacity: Float = 0.5f,
        val visible: Boolean = true
    ) {

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
            val v = view
            val realScreenWidth = (v.parent as View).width
            val realScreenHeight = (v.parent as View).height
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
            val v = view
            val prefs = PreferenceManager.getDefaultSharedPreferences(v.context)
            with(prefs.edit()) {
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

            with(prefs.edit()) {
                remove("osc:$uniqueId:opacity")
                remove("osc:$uniqueId:size")
                remove("osc:$uniqueId:x")
                remove("osc:$uniqueId:y")

                commit()
            }

            loadPrefs()
        }
    }

    private data class ScreenSize(val width: Int, val height: Int)

    private class ConfigureCallback(private val rootView : View) : View.OnTouchListener {

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
                    el.changePosition(
                        x * VIRTUAL_SCREEN_WIDTH / rootView.width,
                        y * VIRTUAL_SCREEN_HEIGHT /rootView.height
                    )
                    el.updateView()
                }
            }

            return true
        }
    }

}