package com.arxlibertatis.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import com.arxlibertatis.R
import com.arxlibertatis.interfaces.SettingsFragmentMvpView
import com.arxlibertatis.presenter.SettingsFragmentPresenter
import com.arxlibertatis.utils.CUSTOM_RESOLUTION_PREFS_KEY
import com.arxlibertatis.utils.GAME_FILES_SHARED_PREFS_KEY
import com.arxlibertatis.utils.extensions.changeInputTypeToDecimal
import com.arxlibertatis.utils.extensions.setHint
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import moxy.presenter.InjectPresenter
import java.io.File


class SettingsFragment : MvpAppCompatFragment(), SettingsFragmentMvpView,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val CHOOSE_DIRECTORY_REQUEST_CODE = 4321
    private val CHOOSE_DIRECTORY_TEXT = "Choose directory"
    private val REQUEST_STORAGE_PERMISSIONS: Int = 123
    private val REQUEST_MEDIA_PERMISSIONS: Int = 456
    private val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    @InjectPresenter
    lateinit var presenter: SettingsFragmentPresenter

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        addPreferencesFromResource(R.xml.settings)

        val gameFilesPreference = findPreference<Preference>(GAME_FILES_SHARED_PREFS_KEY)
        gameFilesPreference?.setOnPreferenceClickListener {
            val isTelevision =
                requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
            if (!isTelevision) {
                val file = File(DialogConfigs.DEFAULT_DIR)
                val properties = DialogProperties()
                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                properties.selection_type = DialogConfigs.DIR_SELECT;
                properties.root = file
                properties.error_dir = file
                properties.offset = file

                val dialog = FilePickerDialog(requireContext(), properties)
                dialog.setTitle(CHOOSE_DIRECTORY_TEXT)
                dialog.setDialogSelectionListener {
                    val directory = it[0]
                    presenter.saveGamePath(directory,requireContext(),this.preferenceScreen.sharedPreferences!!)
                }

                checkPermissions(dialog)

            } else {
                with(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)) {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    startActivityForResult(
                        createChooser(this, CHOOSE_DIRECTORY_TEXT),
                        CHOOSE_DIRECTORY_REQUEST_CODE
                    )
                }
            }

            true
        }
        updatePreference(gameFilesPreference!!,GAME_FILES_SHARED_PREFS_KEY)


        findPreference<Preference>("screen_controls_settings")?.setOnPreferenceClickListener {
            presenter.onConfigureScreenControlsClicked(requireContext())
            true
        }

        val customResolution = findPreference<EditTextPreference>(CUSTOM_RESOLUTION_PREFS_KEY)
        customResolution?.setHint(R.string.custom_resolution_hint)

        val hudScale = findPreference<EditTextPreference>("hud_scale")
        hudScale?.changeInputTypeToDecimal()

        val cursorScale = findPreference<EditTextPreference>("cursor_scale")
        cursorScale?.changeInputTypeToDecimal()

        val fontSize = findPreference<EditTextPreference>("font_size")
        fontSize?.changeInputTypeToDecimal()

        updatePreference(customResolution!!, CUSTOM_RESOLUTION_PREFS_KEY)
        updatePreference(hudScale!!, "hud_scale")
        updatePreference(cursorScale!!,"cursor_scale")
        updatePreference(fontSize!!,"font_size")

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            resultCode != Activity.RESULT_OK -> return
            requestCode == CHOOSE_DIRECTORY_REQUEST_CODE ->
            {
                presenter.saveGamePath(data!!,requireContext(),this.preferenceScreen.sharedPreferences!!)
            }
        }
    }

    override fun updatePreference (prefsKey : String) =
        updatePreference(findPreference(prefsKey)!!,prefsKey)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key!="command_line") {
            updatePreference(key!!)
        }
    }

    private fun updatePreference (preference: Preference, prefsKey: String){
        try {
            preference.summary = preferenceScreen.sharedPreferences?.getString(prefsKey, "") ?: ""
        }
        catch (e: Exception){

        }
    }

    private fun checkPermissions(filePickerDialog: FilePickerDialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //As the device is Android 13 and above so I want the permission of accessing Audio, Images, Videos
            //You can ask permission according to your requirements what you want to access.
            val audioPermission = Manifest.permission.READ_MEDIA_AUDIO
            val imagesPermission = Manifest.permission.READ_MEDIA_IMAGES
            val videoPermission = Manifest.permission.READ_MEDIA_VIDEO
            // Check for permissions and request them if needed
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    audioPermission
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this.requireContext(), imagesPermission
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this.requireContext(), videoPermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // You have the permissions, you can proceed with your media file operations.
                //Showing dialog when Show Dialog button is clicked.
                filePickerDialog.show()
            } else {
                // You don't have the permissions. Request them.
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf<String>(audioPermission, imagesPermission, videoPermission),
                    REQUEST_MEDIA_PERMISSIONS
                )
            }
        } else {
            //Android version is below 13 so we are asking normal read and write storage permissions
            // Check for permissions and request them if needed
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    readPermission
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    writePermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // You have the permissions, you can proceed with your file operations.
                // Show the file picker dialog when needed
                filePickerDialog.show()
            } else {
                // You don't have the permissions. Request them.
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf<String>(readPermission, writePermission),
                    REQUEST_STORAGE_PERMISSIONS
                )
            }
        }
    }
}