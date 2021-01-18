package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor (
    private val app: Application,
    private val pathManager: PathManager,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    var selectedItemsCount: Int = 0
    var selectedItems: ArrayList<BaseItem>? = null

    val filesLiveData by lazy { MutableLiveData<Result<List<BaseItem>>>() }
    val deleteFilesStatus by lazy { MutableLiveData<Result<Boolean>>() }
    val onFileCreated by lazy { MutableLiveData<Result<Pair<ArrayList<BaseItem>, Int>>>() }

    fun getCurrentDirectoryName(): String = pathManager.lastPath().run {
        this.substring(this.lastIndexOf(File.separator) + 1, this.length)
    }

    fun onBackPressed() {
//    = launchInViewModelScope(filesLiveData) {
        filesLiveData.value = storageManager.getFiles(pathManager.pervousPath(), prefsManager.isSplitModeEnabled())
    }

    fun getFiles(path: String) {
        if (pathManager.addPath(path)) //launchInViewModelScope(filesLiveData) {
            filesLiveData.value = storageManager.getFiles(path, prefsManager.isSplitModeEnabled())
        //}
    }

    fun getFilesForCategory(categoryType: CategoryType) = launchInViewModelScope(filesLiveData) {
        when (categoryType) {
//            CategoryType.QUICK_ACCESS -> {}
//            CategoryType.RECENT_FILES -> {}
            CategoryType.IMAGES -> storageManager.getImages()
//            CategoryType.VIDEOS -> {}
//            CategoryType.AUDIO -> {}
//            CategoryType.DOCUMENTS -> {}
            else -> storageManager.getFiles("", true)
        }
    }

    fun createFile(fileName: String, type: FileType) {
        onFileCreated.value = Result.loading()
        onFileCreated.value = storageManager.createNewFolder(fileName, pathManager.lastPath(), prefsManager.isSplitModeEnabled())
    }
//        launchInViewModelScope(onFileCreated) {
//        storageManager.createNewFile(fileName, pathManager.lastPath(), type, prefsManager.isSplitModeEnabled())
//    }

    fun deleteFiles() = selectedItems?.let { list ->
        list.forEach {
            launchInViewModelScope(deleteFilesStatus) { storageManager.deleteFile((it as FileItem).path) }
        }
    }
}