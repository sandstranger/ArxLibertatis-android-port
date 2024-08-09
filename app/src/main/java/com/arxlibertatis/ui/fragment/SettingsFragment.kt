package com.arxlibertatis.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arxlibertatis.R
import com.arxlibertatis.presenter.SettingsFragmentPresenter
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY

class SettingsFragment : PreferenceFragmentCompat(){

    private val CHOOSE_DIRECTORY_REQUEST_CODE = 4321

    private lateinit var presenter: SettingsFragmentPresenter

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        //TODO Use dagger2 here
        presenter = SettingsFragmentPresenter()
        findPreference<Preference>(GAME_FILES_SHARED_PREFS_KEY)?.setOnPreferenceClickListener {
            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            startActivityForResult(createChooser(i, "Choose directory"), CHOOSE_DIRECTORY_REQUEST_CODE)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            resultCode != Activity.RESULT_OK -> return
            requestCode == CHOOSE_DIRECTORY_REQUEST_CODE ->
                presenter.saveGamePath(data, requireContext(), this@SettingsFragment.preferenceScreen.sharedPreferences!!)
        }
    }
}