package com.darabi.mohammad.filemanager.util

import android.app.Application
import java.io.File
import javax.inject.Inject

class StorageManager @Inject constructor(private val application: Application) {

    private fun getParentPathExternalStorageFileDir(type: String? = null) =
        application.getExternalFilesDir(type)?.parent ?: throw StoragManagerException("Parent path must not be null.")

    private fun getExternalFileDir(type: String? = null) =
        File(getParentPathExternalStorageFileDir(type).split("/Android")[0])

    fun getPrimaryExternalStorageDirs(): Array<File>? =
        getExternalFileDir().listFiles()


    data class StoragManagerException (override val message: String) : Throwable()
}
