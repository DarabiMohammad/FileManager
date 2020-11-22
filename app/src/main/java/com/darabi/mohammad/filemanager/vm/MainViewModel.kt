package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.util.storage.OnRemovableStorageAttachmentistener
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor (
    private val app: Application,
    private val volumeManager: VolumeManager
) : BaseViewModel(app), OnRemovableStorageAttachmentistener {

    val removableVolumes = MutableLiveData<ArrayList<BaseItem>>()
    val onPermissionDialogDescButtonClick = MutableLiveData<PermissionDescriptionDialog.DialogAction>()
    val onActionModeChange = MutableLiveData<Pair<Int, Boolean>>()
    val onSelectAllClick = MutableLiveData<Boolean>()
    val onDeleteClicked = MutableLiveData<Boolean>()

    init {
//        volumeManager.otgConnectionCallback = this
    }

    override fun onAttachmentChange(msg: String) {
//        removableStorages.value = volumeManager.getRemovableVolumes()
    }

    override fun onCleared() {
        volumeManager.onDestroy()
        super.onCleared()
    }
}