package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor (
    private val app: Application
) : BaseViewModel(app) {

    val filesLiveData by lazy { MutableLiveData<Result<List<BaseItem>>>() }

    val singleFileLiveData by lazy { MutableLiveData<Result<FileItem>>() }

//    fun getPrimaryStorageRootFiles() = launchInViewModelScope(filesLiveData) {
//        addDividersIfNeeded(storageManager.getPrimaryStorageRootFiles())
//    }
//
//    fun getSecondaryStorageRootFiles() = launchInViewModelScope(filesLiveData) {
//        addDividersIfNeeded(storageManager.getSecondaryStorageRootFiles())
//    }
//
//    fun getFiles(position: Int) = launchInViewModelScope(filesLiveData) {
//        addDividersIfNeeded(storageManager.getFiles(position))
//    }
//
//    fun upToPervious() = launchInViewModelScope(filesLiveData) {
//        addDividersIfNeeded(storageManager.backToPerviousFolder())
//    }
//
//    fun createFile(fileName: String) = launchInViewModelScope(singleFileLiveData) {
//        storageManager.createFile(fileName)
//    }
//
//    fun getFilesForCategory(categoryType: CategoryType) = when (categoryType) {
//        CategoryType.DCIM -> launchInViewModelScope(filesLiveData) { addDividersIfNeeded(storageManager.dcimDirectory()) }
//        CategoryType.DOWNLOAD -> launchInViewModelScope(filesLiveData) { addDividersIfNeeded(storageManager.downloadDirectory()) }
//        CategoryType.MOVIES -> launchInViewModelScope(filesLiveData) { addDividersIfNeeded(storageManager.moviesDirectory()) }
//        CategoryType.MUSICS -> launchInViewModelScope(filesLiveData) { addDividersIfNeeded(storageManager.musicsDirectory()) }
//        CategoryType.PICTURES -> launchInViewModelScope(filesLiveData) { addDividersIfNeeded(storageManager.picturesDirectory()) }
//        CategoryType.QUICK_ACCESS -> {}
//        CategoryType.RECENT_FILES -> {}
//        CategoryType.IMAGES -> {}
//        CategoryType.VIDEOS -> {}
//        CategoryType.AUDIO -> {}
//        CategoryType.DOCUMENTS -> {}
//        else -> {}
//    }
//
//    private inline fun <T> launchInViewModelScope(liveData: MutableLiveData<Result<T>>, crossinline function: suspend () -> Result<T>) = viewModelScope.launch {
//        liveData.value = loading()
//        liveData.value = function()
//    }
//
//    private fun addDividersIfNeeded(filesList: Result<ArrayList<BaseItem>>): Result<ArrayList<BaseItem>> {
//        filesList.result?.let { list ->
//
//            if (prefsManager.isSplitModeEnabled() && list.isNotEmpty()) {
//
//                list.indexOfFirst { item -> item is Directory }.takeIf { index -> index > -1 }
//                    ?.let { list.add(it, FileDivider(getString(R.string.folders))) }
//
//                list.indexOfFirst { item -> item is File }.takeIf { index -> index > -1 }
//                    ?.let { list.add(it, FileDivider(getString(R.string.files))) }
//            }
//        }
//        return filesList
//    }
}