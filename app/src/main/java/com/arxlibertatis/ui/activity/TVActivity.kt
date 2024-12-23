package com.arxlibertatis.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arxlibertatis.R
import com.arxlibertatis.databinding.MainActivityBinding
import com.arxlibertatis.databinding.TvActivityBinding
import com.arxlibertatis.engine.startEngine
import com.arxlibertatis.ui.fragment.SettingsFragment
import com.arxlibertatis.utils.extensions.createInternalPathToCache
import com.arxlibertatis.utils.extensions.requestExternalStoragePermission

class TVActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = TvActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            startEngine(this)
        }
        this.createInternalPathToCache()
        requestExternalStoragePermission()
        changeFragment()
    }

    private fun changeFragment() {
        val fragmentContainterResId = R.id.fragments_container
        var fragment = supportFragmentManager.findFragmentById(fragmentContainterResId)

        if (fragment == null) {
            fragment = SettingsFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(fragmentContainterResId, fragment)
                commit()
            }
        }
    }
}