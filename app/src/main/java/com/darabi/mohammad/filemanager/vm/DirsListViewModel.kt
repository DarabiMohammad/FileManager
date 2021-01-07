package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor (
    private val app: Application,
    private val pathManager: PathManager,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    val filesLiveData by lazy { MutableLiveData<Result<List<BaseItem>>>() }

    val onFileCreated by lazy { MutableLiveData<Result<Pair<FileItem, Int>>>() }

    private var storageType: StorageType? = null

    fun onBackPressed() = launchInViewModelScope(filesLiveData) { storageManager.getFiles(pathManager.pervousPath()) }

    fun getFiles(path: String) {
        if (pathManager.addPath(path))
            launchInViewModelScope(filesLiveData) { storageManager.getFiles(path) }
    }

    fun getFilesForCategory(categoryType: CategoryType) {
//    = launchInViewModelScope(filesLiveData) {
//        when (categoryType) {
//            CategoryType.QUICK_ACCESS -> {}
//            CategoryType.RECENT_FILES -> {}
//            CategoryType.IMAGES -> {}
//            CategoryType.VIDEOS -> {}
//            CategoryType.AUDIO -> {}
//            CategoryType.DOCUMENTS -> {}
//            else -> {}
//        }
    }

    fun createFile(fileName: String, type: FileType) = launchInViewModelScope(onFileCreated) {
        storageManager.createNewFile(fileName, pathManager.lastPath(), type)
    }
}