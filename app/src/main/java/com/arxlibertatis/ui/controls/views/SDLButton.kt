package com.arxlibertatis.ui.controls.views

import android.content.Context
import android.view.MotionEvent
import android.view.View
import org.libsdl.app.SDLActivity

class SDLButton (context: Context) : androidx.appcompat.widget.AppCompatImageButton(context), View.OnTouchListener {

    var keyCode : Int = 0

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.actionMasked){
            MotionEvent.ACTION_DOWN -> SDLActivity.onNativeKeyDown(keyCode)
            MotionEvent.ACTION_UP -> SDLActivity.onNativeKeyUp(keyCode)
        }
        return true
    }
}