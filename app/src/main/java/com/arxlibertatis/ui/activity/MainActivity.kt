package com.arxlibertatis.ui.activity

import android.os.Bundle
import com.arxlibertatis.R
import com.arxlibertatis.databinding.MainActivityBinding
import com.arxlibertatis.interfaces.MainActivityView
import com.arxlibertatis.presenter.MainActivityPresenter
import com.arxlibertatis.ui.fragment.SettingsFragment
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

internal class MainActivity : MvpAppCompatActivity(), MainActivityView {
    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            presenter.onStartGameBtnClicked(this@MainActivity)
        }

        presenter.requestExternalStorage(this)
        changeFragment()
    }

    private fun changeFragment() {
        val fragmentContainterResId = R.id.fragments_container
        var fragment = supportFragmentManager.findFragmentById(fragmentContainterResId)

        if (fragment == null) {
            fragment = SettingsFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragments_container, fragment)
                commit()
            }
        }
    }
}
