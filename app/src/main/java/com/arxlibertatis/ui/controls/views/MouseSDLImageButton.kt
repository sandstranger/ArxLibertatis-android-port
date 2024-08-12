package com.arxlibertatis.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.libsdl.app.SDLActivity

class MouseSDLImageButton (context: Context, attrs : AttributeSet)  : SDLImageButton(context, attrs)  {

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!interactable){
            return false
        }

        when (event.actionMasked){
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_UP -> SDLActivity.onVirtualMouse(keyCode, event.actionMasked)
        }

        return true
    }
}