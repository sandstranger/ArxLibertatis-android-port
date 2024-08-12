package com.arxlibertatis.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.libsdl.app.SDLActivity

class ToggleSdlImageButton (context: Context, attrs : AttributeSet)  : SDLImageButton(context, attrs) {

    private var pressed : Boolean = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN || !interactable){
            return false
        }

        if (!pressed){
            SDLActivity.onNativeKeyDown(keyCode)
            pressed = true
        }
        else{
            SDLActivity.onNativeKeyUp(keyCode)
            pressed = false
        }
        return true
    }
}