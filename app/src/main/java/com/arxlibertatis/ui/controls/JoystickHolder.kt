package com.arxlibertatis.ui.controls

import android.util.Log
import android.view.KeyEvent
import com.arxlibertatis.R
import com.arxlibertatis.ui.controls.views.JoyStick
import org.libsdl.app.SDLActivity

class JoystickHolder (val joystick : JoyStick) : JoyStick.JoyStickListener {
    private var previousDirection = JoyStick.DIRECTION_CENTER

    init {
        joystick.setPadBackground(R.drawable.joystick_background)
        joystick.setButtonDrawable(R.drawable.joystick_stick)
        joystick.enable = false
        joystick.setListener(this)
    }

    override fun onMove(joyStick: JoyStick?, angle: Double, power: Double, direction: Int) {
        Log.d("PARAMS", direction.toString())
        if (previousDirection != direction){
            onKeysUp()
        }
        when(direction){
            JoyStick.DIRECTION_CENTER -> this@JoystickHolder.onKeysUp()
            JoyStick.DIRECTION_DOWN -> moveDown()
            JoyStick.DIRECTION_DOWN_LEFT -> moveDownLeft()
            JoyStick.DIRECTION_RIGHT_DOWN -> moveDownRight()
            JoyStick.DIRECTION_LEFT -> moveLeft()
            JoyStick.DIRECTION_LEFT_UP -> moveLeftUp()
            JoyStick.DIRECTION_UP -> moveUp()
            JoyStick.DIRECTION_UP_RIGHT -> moveRightUp()
            JoyStick.DIRECTION_RIGHT -> moveRight()
        }
        previousDirection = direction
    }

    override fun onTap() {
    }

    override fun onDoubleTap() {
    }

    private fun moveDown() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_S)
    }

    private fun moveDownLeft() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_S)
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_A)
    }

    private fun moveLeft() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_A)
    }

    private fun moveLeftUp() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_A)
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_W)
    }

    private fun moveRightUp() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_D)
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_W)
    }

    private fun moveUp() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_W)
    }

    private fun moveDownRight() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_S)
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_D)
    }

    private fun moveRight() {
        SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_D)
    }

    private fun onKeysUp (){
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_W)
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_A)
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_S)
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_D)
    }
}