package com.highestaim.exitsheet

import android.content.Context
import java.io.File

object Configs {

    private const val pdfFileName = "ExitShit.pdf"
    private const val singFileName = "sign.png"


    fun getFilePath(context: Context?): String {
        val dir = File(
            context?.getExternalFilesDir(null)?.path
                    + File.separator
                    + context?.resources?.getString(R.string.app_name)
                    + File.separator
        )
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir.path + File.separator + pdfFileName
    }

    fun getSignFilePath(context: Context?): String {
        val dir = File(
            context?.getExternalFilesDir(null)?.path
                    + File.separator
                    + context?.resources?.getString(R.string.app_name)
                    + File.separator
        )
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir.path + File.separator + singFileName
    }
}