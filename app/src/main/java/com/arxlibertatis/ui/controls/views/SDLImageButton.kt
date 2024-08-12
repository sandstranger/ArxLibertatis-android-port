package com.arxlibertatis.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import org.libsdl.app.SDLActivity

open class SDLImageButton (context: Context, attrs : AttributeSet) : AppCompatImageButton(context, attrs), View.OnTouchListener {

    var interactable : Boolean = true

    protected var keyCode : Int = 0

    init {
        setOnTouchListener(this)
    }

    open override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!interactable) return false

        when (event.actionMasked){
            MotionEvent.ACTION_DOWN -> SDLActivity.onNativeKeyDown(keyCode)
            MotionEvent.ACTION_UP -> SDLActivity.onNativeKeyUp(keyCode)
        }
        return true
    }

    fun setKeycode (keyCode: Int) : SDLImageButton {
        this.keyCode = keyCode
        return this
    }
}