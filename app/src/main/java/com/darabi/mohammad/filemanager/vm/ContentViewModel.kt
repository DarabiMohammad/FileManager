package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentViewModel @Inject constructor (
    private val app: Application,
    private val pathManager: PathManager,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    var selectedItemsCount: Int = 0
    val selectedItems = arrayListOf<BaseItem>()

    val filesLiveData by lazy { MutableLiveData<Result<List<BaseItem>>>() }
    val onFileCreated by lazy { MutableLiveData<Result<Pair<ArrayList<BaseItem>, Int>>>() }
    val onFilesDeleted by lazy { MutableLiveData<Boolean>() }

    fun getCurrentDirectoryName(): String = pathManager.lastPath().run {
        this.substring(this.lastIndexOf(File.separator) + 1, this.length)
    }

    fun onBackPressed() = launchSupervisorJob(filesLiveData) {
        storageManager.getFiles(pathManager.pervousPath(), prefsManager.isSplitModeEnabled())
    }

    fun getFiles(path: String) =
        if (pathManager.addPath(path))
            launchSupervisorJob(filesLiveData) { storageManager.getFiles(path, prefsManager.isSplitModeEnabled()) }
        else null

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

    fun createFile(fileName: String, type: FileType) = launchSupervisorJob(onFileCreated) {
        storageManager.createNewFolder(fileName, pathManager.lastPath(), prefsManager.isSplitModeEnabled())
    }

    fun deleteFiles() = liveData {
        withContext(Dispatchers.Default) {
            selectedItems.map {
                async { storageManager.deleteFile((it as FileItem).path) }
            }.awaitAll().forEach { emit(it) }
        }
    }

    fun copy() {}

    fun move() {}
}