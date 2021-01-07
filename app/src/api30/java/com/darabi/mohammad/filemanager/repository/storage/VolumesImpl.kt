package com.darabi.mohammad.filemanager.repository.storage

import android.app.Application
import android.content.Context
import android.os.storage.StorageManager
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.util.safeSuspendCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class VolumesImpl @Inject constructor(private val app: Application) : Volumes() {

    override suspend fun getVolumes(): Result<ArrayList<StorageVolume>> = safeSuspendCall {
        coroutineScope { Result.success(getVolumeList()) }
    }

    private fun getVolumeList(): ArrayList<StorageVolume> {
        val storagManager = app.applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumes = storagManager.storageVolumes
        val volumeInfoList = arrayListOf<StorageVolume>()
//        for (volume in volumes) {
//            volumeInfoList.add(
//                ExternalStorage(
//                volume.mediaStoreVolumeName!!, volume.directory!!.path,
//            )
//            )
//        }
        volumeInfoList.sortBy { it.name }
        return volumeInfoList
    }
}