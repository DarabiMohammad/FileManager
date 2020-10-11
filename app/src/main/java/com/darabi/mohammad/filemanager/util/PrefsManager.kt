package com.darabi.mohammad.filemanager.util

import android.app.Application
import javax.inject.Inject

class PrefsManager @Inject constructor(private val application: Application) {

    companion object {
        const val PERFS_NAME = "file_manager_perfs"
    }
}