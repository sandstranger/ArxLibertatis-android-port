package com.arxlibertatis.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.arxlibertatis.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }
}