package com.darabi.mohammad.filemanager.vm.ccontent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.FileType
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.repository.storage.Volumes
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Provider

class CopyMoveViewModel @Inject constructor (
    private val app: Application,
    private val volumes: Volumes,
    private val pathManagerProvider: Provider<PathManager>,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    val onPathSelected by lazy { MutableLiveData<String?>() }
    val openNewFileDialog by lazy { MutableLiveData<FileType?>() }

    private lateinit var pathManager: PathManager

    fun onBackPressed(): LiveData<Result<ArrayList<out BaseItem>>> = loadingLiveData {
        storageManager.getFolders(pathManager.pervousPath())
    }

    fun createFolder(fileName: String) = liveData {
        pathManager.lastPath()?.let { emit(storageManager.createNewFolder(fileName, it)) }
    }

    fun getVolumes() = loadingLiveData {
        pathManager = pathManagerProvider.get()
        volumes.getVolumes()
    }

    fun getFolders(path: String) = loadingLiveData {
        pathManager.addPath(path)
        storageManager.getFolders(path)
    }

    fun copyFiles() {}
}