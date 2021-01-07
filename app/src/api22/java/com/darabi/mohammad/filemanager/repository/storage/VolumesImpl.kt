package com.darabi.mohammad.filemanager.repository.storage

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.StatFs
import android.os.storage.StorageManager
import android.text.format.Formatter.formatFileSize
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
        coroutineScope { Result.success(getVolumeInfoList()) }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("PrivateApi")
    private fun getVolumeInfoList(): ArrayList<StorageVolume> {

        val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumeList")
        val storageManager = app.applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumeList = getVolumeList.invoke(storageManager) as Array<android.os.storage.StorageVolume>
        val volumeInfoList = arrayListOf<StorageVolume>()
        for (volume in volumeList) {

            var label = "${volume.javaClass.getDeclaredField("mUserLabel").apply { this.isAccessible = true }.get(volume)}"
            val path = volume.javaClass.getDeclaredField("mPath").apply { this.isAccessible = true }.get(volume)
            val isPrimary = volume.javaClass.getDeclaredField("mPrimary").apply { this.isAccessible = true }.get(volume) as Boolean
            // todo : add otg support in future.
            val type = if (isPrimary) StorageType.PRIMARY else StorageType.SECONDARY

            if (label.equals("sdcard", true)) label = app.getString(R.string.sd_card)

            val statFs = StatFs(path!!.toString())

            volumeInfoList.add(ExternalStorage(
                label, path.toString(),
                formatFileSize(app, statFs.totalBytes),
                formatFileSize(app, statFs.freeBytes),
                type
            ))
        }
        volumeInfoList.sortBy { it.name }
        return volumeInfoList
    }
}