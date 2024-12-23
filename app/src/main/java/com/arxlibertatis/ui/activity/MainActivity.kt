package com.arxlibertatis.ui.activity

import android.os.Bundle
import com.arxlibertatis.R
import com.arxlibertatis.databinding.MainActivityBinding
import com.arxlibertatis.presenter.MainActivityPresenter
import com.arxlibertatis.ui.fragment.SettingsFragment
import com.arxlibertatis.utils.extensions.createInternalPathToCache
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.presenter.InjectPresenter

internal class MainActivity : MvpAppCompatActivity(), MvpView {
    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            presenter.onStartGameBtnClicked(this@MainActivity)
        }
        this.createInternalPathToCache()
        presenter.requestExternalStorage(this)
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
