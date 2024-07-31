package com.arxlibertatis.single.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arxlibertatis.R
import moxy.MvpAppCompatActivity

abstract class SingleActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        changeFragment()
    }

    protected abstract fun getFragment(): Fragment

    private fun changeFragment() {
        val fragmentContainterResId = R.id.fragments_container
        supportFragmentManager.beginTransaction().apply {
            replace(fragmentContainterResId, getFragment())
            commit()
        }
    }
}
