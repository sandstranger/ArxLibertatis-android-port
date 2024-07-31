package com.arxlibertatis.ui.activity

import androidx.fragment.app.Fragment
import com.arxlibertatis.single.activity.SingleActivity
import com.arxlibertatis.ui.fragment.MainFragment

internal class MainActivity : SingleActivity() {
    override fun getFragment(): Fragment = MainFragment.newInstance()
}
