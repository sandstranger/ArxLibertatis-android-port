package com.arxlibertatis.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.os.Bundle
import androidx.preference.Preference
import com.arxlibertatis.R
import com.arxlibertatis.presenter.SettingsFragmentPresenter
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import moxy.MvpView
import moxy.presenter.InjectPresenter

class SettingsFragment : MvpAppCompatFragment(), MvpView{

    private val CHOOSE_DIRECTORY_REQUEST_CODE = 4321

    @InjectPresenter
    lateinit var presenter: SettingsFragmentPresenter

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)

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
            {
                if (presenter.sharedPrefsChanged == null) {
                    presenter.sharedPrefsChanged = { key -> updatePreference(key) }
                }
                presenter.saveGamePath(data!!, requireContext(), this@SettingsFragment.preferenceScreen.sharedPreferences!!)
            }
        }
    }

    private fun updatePreference (prefsKey : String) =
        updatePreference(findPreference(prefsKey)!!,prefsKey)

    private fun updatePreference (preference: Preference, prefsKey: String){
        preference.summary = preferenceScreen.sharedPreferences?.getString(prefsKey, "") ?: ""
    }
}