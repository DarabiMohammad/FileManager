package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.os.Build
import java.io.File
import javax.inject.Inject
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi

class VolumeManager @Inject constructor(private val application: Application) {

    private fun getParentPathExternalStorageFileDir(type: String? = null) =
        application.getExternalFilesDir(type)?.parent ?: throw StoragManagerException("Parent path must not be null.")

    private fun getExternalFileDir(type: String? = null) =
        File(getParentPathExternalStorageFileDir(type).split("/Android")[0])

    fun getPrimaryExternalStorageDirs(): Array<File>? =
        getExternalFileDir().listFiles()



    @RequiresApi(Build.VERSION_CODES.M)
    private fun getStorageManager() = application.getSystemService(StorageManager::class.java)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getStorageVolumes() = getStorageManager().storageVolumes

    private fun getStoragesNameLegacy(): ArrayList<String> = arrayListOf("", "")

    fun getAvailableStorageNames(): ArrayList<String> {
        val storageNames = arrayListOf<String>()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            getStorageVolumes().forEach { storageNames.add(it.getDescription(application)) }
        else
            storageNames.addAll(getStoragesNameLegacy())
        return storageNames
    }

    data class StoragManagerException internal constructor(override val message: String) : Throwable()
}
