package com.darabi.mohammad.filemanager.vm.ccontent

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.vm.StoragesViewModel
import javax.inject.Inject

class CopyMoveViewModel @Inject constructor (
    private val app: Application
) : StoragesViewModel(app) {

    val availableVolumes by lazy { MutableLiveData<Result<ArrayList<StorageVolume>>>() }

    fun getVolumes() = launchInViewModelScope(availableVolumes) { volumes.getVolumes() }
}