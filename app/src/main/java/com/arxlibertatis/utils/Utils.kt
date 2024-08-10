package com.arxlibertatis.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun copyGameAssets(context: Context, src: String, dst: String) {
    val assetManager = context.assets
    try {
        val assets = assetManager.list(src) ?: return
        if (assets.isEmpty()) {
            copyFile(context, src, dst)
        } else {
            val dir = File(dst)
            if (!dir.exists())
                dir.mkdirs()
            for (i in assets.indices) {
                copyGameAssets(context, src + "/" + assets[i], dst + "/" + assets[i])
            }
        }
    } catch (ex: IOException) {
    }
}

private fun copyFile(context: Context, src: String, dst: String) {
    try {
        val inp = context.assets.open(src)
        val out = FileOutputStream(dst)
        inp.copyTo(out)
        out.flush()
        inp.close()
        out.close()
    } catch (e: IOException) {
    }
}