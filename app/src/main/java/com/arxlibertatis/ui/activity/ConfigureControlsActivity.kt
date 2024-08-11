package com.arxlibertatis.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arxlibertatis.R
import com.arxlibertatis.databinding.ScreenControlsBinding
import com.arxlibertatis.engine.setFullscreen
import com.arxlibertatis.ui.controls.ScreenControlsManager

class ConfigureControlsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)
        val binding = ScreenControlsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val screenControlsManager = ScreenControlsManager(binding, this)
        screenControlsManager.editScreenControls()
    }
}