package com.darabi.mohammad.filemanager.repository.volumes

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.StatFs
import android.os.storage.StorageManager
import android.text.format.Formatter.formatFileSize
import com.darabi.mohammad.filemanager.model.ExternalStorage
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.util.safeSuspendCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class VolumesImpl @Inject constructor(private val app: Application) : Volumes() {

    override suspend fun getVolumes(): Result<ArrayList<StorageVolume>> = safeSuspendCall {
        coroutineScope { Result.success(getVolumeInfoList()) }
    }

    @SuppressLint("PrivateApi")
    @Suppress("UNCHECKED_CAST")
    private fun getVolumeInfoList(): ArrayList<StorageVolume> {
        val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumeList")
        val storageManager = app.applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumeList = getVolumeList.invoke(storageManager) as Array<android.os.storage.StorageVolume>
        val volumeInfoList = arrayListOf<StorageVolume>()
        var statFs: StatFs
        for (volume in volumeList) {

            val label = volume.javaClass.getDeclaredField("mUserLabel").apply { this.isAccessible = true }.get(volume)
            val path = volume.javaClass.getDeclaredField("mPath").apply { this.isAccessible = true }.get(volume)

            // todo : maybe this variables are not needed, so if yes, remove them.

            statFs = StatFs(path!!.toString())

            volumeInfoList.add(ExternalStorage(
                label!!.toString(), path.toString(),
                formatFileSize(app, statFs.totalBytes), formatFileSize(app, statFs.freeBytes)
            ))
        }
        return volumeInfoList
    }
}