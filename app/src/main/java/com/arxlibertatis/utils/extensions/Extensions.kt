package com.arxlibertatis.utils.extensions

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arxlibertatis.BuildConfig

inline fun <reified T> Context.startActivity(finishParentActivity : Boolean = true) where T : Activity {
    val i = Intent(this, T::class.java)

    if (this is Application) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(Intent(this, T::class.java))

    if (finishParentActivity && this is Activity) this.finish();
}

fun String?.toBoolean(): Boolean =
    when(this) {
        "0" -> false
        else -> true
    }

fun Activity.requestExternalStoragePermission () {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (!Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            this.startActivity(
                Intent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    uri
                )
            )
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 23
                )
            }
        }
    }
}