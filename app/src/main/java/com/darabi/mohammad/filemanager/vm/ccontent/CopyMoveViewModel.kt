package com.darabi.mohammad.filemanager.vm.ccontent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.repository.storage.Volumes
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.vm.StoragesViewModel
import javax.inject.Inject
import javax.inject.Singleton

class CopyMoveViewModel @Inject constructor (
    private val app: Application,
    private val volumes: Volumes,
    private val pathManager: PathManager,
    private val storageManager: StorageManager
) : NavigationContentViewModel(app, pathManager, storageManager) {

    val availableVolumes by lazy { MutableLiveData<Result<ArrayList<StorageVolume>>>() }
    val onPathSelected by lazy { MutableLiveData<String?>() }

    fun getVolumes() = launchInViewModelScope(availableVolumes) { volumes.getVolumes() }

    override fun onBackPressed(): LiveData<Result<ArrayList<out BaseItem>>> = loadingLiveData {
        storageManager.getFolders(pathManager.pervousPath())
    }

    fun getFolders(path: String) = loadingLiveData {
        pathManager.addPath(path)
        storageManager.getFolders(path)
    }
}