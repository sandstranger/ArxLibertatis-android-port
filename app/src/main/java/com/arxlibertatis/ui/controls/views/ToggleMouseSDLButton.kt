package com.arxlibertatis.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import org.libsdl.app.SDLActivity

class ToggleMouseSDLButton (context: Context, attrs : AttributeSet)  : ToggleSdlImageButton(context, attrs) {

    protected override fun onPressed() {
        SDLActivity.onVirtualMouse(keyCode, MotionEvent.ACTION_DOWN)
    }

    protected override fun onUnPressed() {
        SDLActivity.onVirtualMouse(keyCode, MotionEvent.ACTION_UP)
    }
}