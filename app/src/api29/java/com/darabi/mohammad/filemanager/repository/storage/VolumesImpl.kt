package com.darabi.mohammad.filemanager.repository.storage

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.text.format.Formatter
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.ExternalStorage
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageType
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.repository.safeSuspendCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class VolumesImpl @Inject constructor(private val app: Application) : Volumes() {

    override suspend fun getVolumes(): Result<ArrayList<StorageVolume>> = safeSuspendCall {
        coroutineScope { Result.success(getVolumeList()) }
    }

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    private fun getVolumeList(): ArrayList<StorageVolume> {

        val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumes")
        val storageManager = app.applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumeList = getVolumeList.invoke(storageManager) as List<Any>
        val volumeInfoList = arrayListOf<StorageVolume>()
        val primaryStoragePath = Environment.getExternalStorageDirectory().path

        for (   volume in volumeList) {

            val id = volume.javaClass.getDeclaredField("id").apply { this.isAccessible = true }.get(volume)

            if (id == "private")
                continue

            val label = if (id == "emulated")
                app.getString(R.string.internal_storage)
            else
                volume.javaClass.getDeclaredField("fsLabel").apply { this.isAccessible = true }.get(volume)

            var path = "${volume.javaClass.getDeclaredField("path").apply { this.isAccessible = true }.get(volume)}"

            if (id == "emulated" && !path.endsWith("0")) path = "$path/0"

            // todo : missing otg support.
            val type = if (path == primaryStoragePath) StorageType.PRIMARY else StorageType.SECONDARY

            val statFs = StatFs(path)

            volumeInfoList.add(ExternalStorage(
                label.toString(), path,
                Formatter.formatFileSize(app, statFs.totalBytes),
                Formatter.formatFileSize(app, statFs.freeBytes), type
            ))
        }
        volumeInfoList.sortBy { it.name }
        return volumeInfoList
    }
}