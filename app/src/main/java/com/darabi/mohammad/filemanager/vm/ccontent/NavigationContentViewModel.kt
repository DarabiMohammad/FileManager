package com.darabi.mohammad.filemanager.vm.ccontent

import android.app.Application
import androidx.lifecycle.liveData
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel

abstract class NavigationContentViewModel constructor(
    private val app: Application,
    private val pathManager: PathManager,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    open fun onBackPressed() = loadingLiveData {
        storageManager.getFiles(pathManager.pervousPath(), prefsManager.isSplitModeEnabled())
    }

    fun getFiles(path: String?) = liveData {
        path?.let {
            emit(Result.loading<ArrayList<out BaseItem>>())
            pathManager.addPath(it)
            emit(storageManager.getFiles(it, prefsManager.isSplitModeEnabled()))
        }
    }
}