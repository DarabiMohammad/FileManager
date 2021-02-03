package com.darabi.mohammad.filemanager.vm.ccontent

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

    private var selectedItems = arrayListOf<BaseItem>()
    private lateinit var copiedPaths: List<String>

    val onFileCreated by lazy { MutableLiveData<Result<Pair<ArrayList<BaseItem>, Int>>>() }
    val onFilesDeleted by lazy { MutableLiveData<ArrayList<BaseItem>>() }

    fun getSelectedItemsCount() = selectedItems.size

    fun getCurrentDirectoryName(): String = pathManager.lastPath().run {
        this.substring(this.lastIndexOf(File.separator) + 1, this.length)
    }

    fun onBackPressed() = liveData {
        emit(storageManager.getFiles(pathManager.pervousPath(), prefsManager.isSplitModeEnabled()))
    }

    fun getFiles(path: String) = liveData {
        if (pathManager.addPath(path))
            emit(storageManager.getFiles(path, prefsManager.isSplitModeEnabled()))
    }

    fun getFilesForCategory(categoryType: CategoryType) = liveData {
        val result = when (categoryType) {
//            CategoryType.QUICK_ACCESS -> {}
//            CategoryType.RECENT_FILES -> {}
            CategoryType.IMAGES -> storageManager.getImages()
//            CategoryType.VIDEOS -> {}
//            CategoryType.AUDIO -> {}
//            CategoryType.DOCUMENTS -> {}
            else -> storageManager.getFiles("", true)
        }
        emit(result)
    }

    fun onSelectionChanged(item: BaseItem) = liveData {
        if (item.isSelected) selectedItems.add(item) else selectedItems.remove(item)
        emit(getSelectedItemsCount())
    }

    fun onSelectAll(items: List<BaseItem>) = liveData {
        selectedItems = items as ArrayList<BaseItem>
        emit(Pair(getSelectedItemsCount(), selectedItems.isNotEmpty()))
    }

    fun createFile(fileName: String, type: FileType) = liveData {
        emit(storageManager.createNewFolder(fileName, pathManager.lastPath(), prefsManager.isSplitModeEnabled()))
    }

    fun deleteFiles() = liveData {
        withContext(Dispatchers.Default) {
            selectedItems.map {
                async { storageManager.deleteFile(it as FileItem) }
            }.awaitAll().forEach { emit(it) }.also { onFilesDeleted.postValue(selectedItems) }
        }
    }

    fun copy() {
        copiedPaths = selectedItems.map { (it as FileItem).path }
    }

    fun move() {}
}