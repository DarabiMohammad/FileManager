package com.darabi.mohammad.filemanager.vm.ccontent

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.CategoryType
import com.darabi.mohammad.filemanager.model.FileItem
import com.darabi.mohammad.filemanager.repository.storage.OnProgressChanged
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.ui.fragment.contents.CopyMoveBottomSheetFragment
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.util.TransferAction
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

@Singleton
class ContentViewModel @Inject constructor (
    private val app: Application,
    private val pathManager: PathManager,
    private val storageManager: StorageManager
) : BaseContentViewModel(app, pathManager, storageManager) {

    private var selectedItems = arrayListOf<BaseItem>()
    private lateinit var copiedPaths: List<String>

    val onFilesDeleted by lazy { MutableLiveData<ArrayList<BaseItem>>() }

    fun getSelectedItemsCount() = selectedItems.size

//    fun getCurrentDirectoryName(): String = pathManager.lastPath().run {
//        this.substring(this.lastIndexOf(File.separator) + 1, this.length)
//    }

    fun getFilesForCategory(categoryType: CategoryType?) = liveData {
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

    fun onSelectAll(items: ArrayList<BaseItem>) = liveData {
        selectedItems = items
        emit(Pair(getSelectedItemsCount(), selectedItems.isNotEmpty()))
    }

    fun isSubFolderOFSelectedItems(targetPath: String): Boolean =
        targetPath.startsWith((selectedItems[0] as FileItem).path)

    fun deleteFiles() = liveData {
        withContext(Dispatchers.Default) {
            selectedItems.map {
                async { storageManager.deleteFile(it as FileItem) }
            }.awaitAll().forEach { emit(it) }.also { onFilesDeleted.postValue(selectedItems) }
        }
    }

    fun copyOrMove(destinationPath: String, actionType: TransferAction, progressHandler: OnProgressChanged) = liveData {
        withContext(Dispatchers.Default) {
            selectedItems.map {
                async { storageManager.copy((it as FileItem).path, destinationPath, progressHandler) }
            }.awaitAll().forEach { emit(it) }
        }
    }
}