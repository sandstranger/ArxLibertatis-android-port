package com.arxlibertatis.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.content.SharedPreferences
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
        presenter = SettingsFragmentPresenter { key -> updatePreference(key) }

        val gameFilesPreference = findPreference<Preference>(GAME_FILES_SHARED_PREFS_KEY)
        gameFilesPreference?.setOnPreferenceClickListener {
            with(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)) {
                addCategory(Intent.CATEGORY_DEFAULT)
                startActivityForResult(createChooser(this, "Choose directory"),
                    CHOOSE_DIRECTORY_REQUEST_CODE)
            }
            true
        }
        updatePreference(gameFilesPreference!!,GAME_FILES_SHARED_PREFS_KEY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            resultCode != Activity.RESULT_OK -> return
            requestCode == CHOOSE_DIRECTORY_REQUEST_CODE ->
                presenter.saveGamePath(data!!, requireContext(), this@SettingsFragment.preferenceScreen.sharedPreferences!!)
        }
    }

    private fun updatePreference (prefsKey : String) =
        updatePreference(findPreference(prefsKey)!!,prefsKey)

    private fun updatePreference (preference: Preference, prefsKey: String){
        preference.summary = preferenceScreen.sharedPreferences?.getString(prefsKey, "") ?: ""
    }
}