package com.arxlibertatis.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arxlibertatis.R
import com.arxlibertatis.databinding.ScreenControlsBinding
import com.arxlibertatis.engine.setFullscreen

class ConfigureControlsActivity : AppCompatActivity() {

    private lateinit var binding : ScreenControlsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)
        binding = ScreenControlsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.screenControlsRoot.setBackgroundColor(Color.GRAY)
        binding.joystick.setPadBackground(R.drawable.joystick_background)
        binding.joystick.setButtonDrawable(R.drawable.joystick_stick)
    }
}