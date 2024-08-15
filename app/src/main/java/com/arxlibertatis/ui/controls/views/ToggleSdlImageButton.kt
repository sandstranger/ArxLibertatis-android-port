package com.arxlibertatis.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.libsdl.app.SDLActivity

open class ToggleSdlImageButton (context: Context, attrs : AttributeSet)  : SDLImageButton(context, attrs) {

    private var pressed : Boolean = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN || !interactable){
            return false
        }

        if (!pressed){
            onPressed()
            pressed = true
        }
        else{
            unPress()
        }
        return true
    }

    fun unPress (){
        onUnPressed()
        pressed = false;
    }

    protected open fun onPressed () {
        SDLActivity.onNativeKeyDown(keyCode)
    }

    protected open fun onUnPressed () {
        SDLActivity.onNativeKeyUp(keyCode)
    }
}