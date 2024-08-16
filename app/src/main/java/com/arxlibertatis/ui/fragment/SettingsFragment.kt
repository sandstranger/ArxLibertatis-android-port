package com.arxlibertatis.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import com.arxlibertatis.R
import com.arxlibertatis.interfaces.SettingsFragmentMvpView
import com.arxlibertatis.presenter.SettingsFragmentPresenter
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import moxy.presenter.InjectPresenter
import java.io.File


class SettingsFragment : MvpAppCompatFragment(), SettingsFragmentMvpView{

    @InjectPresenter
    lateinit var presenter: SettingsFragmentPresenter

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        addPreferencesFromResource(R.xml.settings)

        val gameFilesPreference = findPreference<Preference>(GAME_FILES_SHARED_PREFS_KEY)
        gameFilesPreference?.setOnPreferenceClickListener {
            MaterialDialog(this.requireContext()).show {
                folderChooser (this.context, initialDirectory = File("/storage/emulated/0/")) { _, folder ->
                    presenter.saveGamePath(folder.path,requireContext(),
                        this@SettingsFragment.preferenceScreen.sharedPreferences!!)
                }
            }
            true
        }
        updatePreference(gameFilesPreference!!,GAME_FILES_SHARED_PREFS_KEY)

        findPreference<Preference>("screen_controls_settings")?.setOnPreferenceClickListener {
            presenter.onConfigureScreenControlsClicked(requireContext())
            true
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.copy_game_assets -> {
                presenter.copyGameAssets(requireContext(), preferenceScreen.sharedPreferences!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updatePreference (prefsKey : String) =
        updatePreference(findPreference(prefsKey)!!,prefsKey)

    private fun updatePreference (preference: Preference, prefsKey: String){
        preference.summary = preferenceScreen.sharedPreferences?.getString(prefsKey, "") ?: ""
    }
}