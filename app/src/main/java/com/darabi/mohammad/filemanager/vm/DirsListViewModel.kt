package com.darabi.mohammad.filemanager.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.model.Result.Companion.loading
import com.darabi.mohammad.filemanager.util.PrefsManager
import com.darabi.mohammad.filemanager.util.storage.StorageManager
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor (
    private val app: Application,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    val filesLiveData by lazy { MutableLiveData<Result<List<BaseItem>>>() }

    fun getPrimaryStorageRootFiles() = launchInViewModelScope(filesLiveData) {
        addDividersIfNeeded(storageManager.getPrimaryStorageRootFiles())
    }

    fun getSecondaryStorageRootFiles() = launchInViewModelScope(filesLiveData) {
        addDividersIfNeeded(storageManager.getSecondaryStorageRootFiles())
    }

    fun getFiles(position: Int) = launchInViewModelScope(filesLiveData) {
        addDividersIfNeeded(storageManager.getFiles(position))
    }

    fun upToPervious() = launchInViewModelScope(filesLiveData) {
        addDividersIfNeeded(storageManager.backToPerviousFolder())
    }

    fun getFilesForCategory(categoryType: CategoryType) {
    }

    private inline fun <T> launchInViewModelScope(liveData: MutableLiveData<Result<T>>, crossinline function: suspend () -> Result<T>) = viewModelScope.launch {
        liveData.value = loading()
        liveData.value = function()
    }

    private fun addDividersIfNeeded(filesList: Result<ArrayList<BaseItem>>): Result<ArrayList<BaseItem>> {
        filesList.result?.let { list ->
            if (prefsManager.isSplitModeEnabled() && list.isNotEmpty()) {

                list.indexOfFirst { item -> item is Directory }.takeIf { index -> index > -1 }
                    ?.let { list.add(it, FileDivider(getString(R.string.folders))) }

                list.indexOfFirst { item -> item is File }.takeIf { index -> index > -1 }
                    ?.let { list.add(it, FileDivider(getString(R.string.files))) }
            }
        }
        return filesList
    }
}