package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.util.storage.OnRemovableStorageAttachmentistener
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val app: Application,
    private val volumeManager: VolumeManager
) : BaseViewModel(app), OnRemovableStorageAttachmentistener {

    val removableStorages = MutableLiveData<ArrayList<VolumeManager.Volume>>()

    init { volumeManager.otgConnectionCallback = this }

    override fun onAttachmentChange(msg: String) {
        removableStorages.value = volumeManager.getRemovableVolumes()
    }

    override fun onCleared() {
        volumeManager.onDestroy()
        super.onCleared()
    }
}