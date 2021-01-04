package com.darabi.mohammad.filemanager.repository.volumes

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.StatFs
import android.os.storage.StorageManager
import android.text.format.Formatter
import com.darabi.mohammad.filemanager.model.ExternalStorage
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.util.safeSuspendCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class VolumesImpl @Inject constructor(private val app: Application) : Volumes() {

    override suspend fun getVolumes(): Result<ArrayList<StorageVolume>> = safeSuspendCall {
        coroutineScope { Result.success(getVolumeList()) }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    private fun getVolumeList(): ArrayList<StorageVolume> {

        val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumes")
        val storageManager = app.applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumeList = getVolumeList.invoke(storageManager) as List<Any>
        val volumeInfoList = arrayListOf<StorageVolume>()
        var statFs: StatFs

        for (   volume in volumeList) {

            val id = volume.javaClass.getDeclaredField("id").apply { this.isAccessible = true }.get(volume)

            if (id == "private")
                continue

            val label = if (id == "emulated")
                "Internal Storage"
            else
                volume.javaClass.getDeclaredField("fsLabel").apply { this.isAccessible = true }.get(volume)

            val path = volume.javaClass.getDeclaredField("path").apply { this.isAccessible = true }.get(volume)

            statFs = StatFs(path!!.toString())

            volumeInfoList.add(ExternalStorage(
                label.toString(), path.toString(),
                Formatter.formatFileSize(app, statFs.totalBytes),
                Formatter.formatFileSize(app, statFs.freeBytes)
            ))
        }
        return volumeInfoList
    }
}