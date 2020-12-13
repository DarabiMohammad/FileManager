package com.darabi.mohammad.filemanager.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.Result.Companion.loading
import com.darabi.mohammad.filemanager.util.PrefsManager
import com.darabi.mohammad.filemanager.util.storage.StorageManager
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor (
    private val app: Application,
    private val prefsManager: PrefsManager,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    val filesLiveData by lazy { MutableLiveData<Result<List<BaseItem>>>() }

    fun getPrimaryStorageRootFiles() = launchInViewModelScope(filesLiveData) {
        storageManager.getPrimaryStorageRootFiles()
    }

    fun getSecondaryStorageRootFiles() = launchInViewModelScope(filesLiveData) {
        storageManager.getSecondaryStorageRootFiles()
    }

    fun getFiles(position: Int) = launchInViewModelScope(filesLiveData) {
        storageManager.getFiles(position)
    }

    fun upToPervious() = launchInViewModelScope(filesLiveData) {
        storageManager.backToPerviousFolder()
    }

    private inline fun <T> launchInViewModelScope(liveData: MutableLiveData<Result<T>>, crossinline function: suspend () -> Result<T>) = viewModelScope.launch {
        liveData.value = loading()
        liveData.value = function()
    }
}
//) : BaseViewModel(app) {
//
//    private val checkedItemsPosition = mutableSetOf<Int>()
//    private var dividersPosition = mutableListOf<Int>()
//    private var itemsCount = 0
//    private var adapterPosition = -1
//
//    private  var currentPath = EMPTY_STRING
//
//    val fileOrFolderCreation = MutableLiveData<DirItem.Item>()
//
//    fun getMaxCheckableItemCount() = itemsCount
//
//    fun getAdapterPosition() = adapterPosition
//
//    fun savePath(currentPath: String, adapterPosition: Int) = pathManager.savePath(currentPath, adapterPosition)
//
//    fun saveCurrentPath(adapterPosition: Int) = currentPath.takeIf { it.isNotEmpty() }?.let { pathManager.savePath(it, adapterPosition) }
//
//    fun previousPath(): BaseItem = pathManager.getPath().run {
//        return DirItem.Item(this.second, this.first, ItemType.LIST_FOLDER_ITEM, R.drawable.ic_settings_black).also { adapterPosition = this.third }
//    }
//
//    fun getSubFiles(item: BaseItem): ArrayList<DirItem> {
//        dividersPosition.clear()
//        checkedItemsPosition.clear()
//        currentPath = item.itemPath
//        val pair = volumeManager.getSubDirectoriesPath(item.itemPath)
//        val folders = prepareFolderItems(pair.first).also { if(it.isNotEmpty()) dividersPosition.add(0) }
//        val files = prepareFileItems(pair.second).also { if(it.isNotEmpty()) dividersPosition.add(folders.size) }
//        folders.addAll(files)
//        if(folders.size > 4) {
//            folders.add(DirItem.Empty)
//            dividersPosition.add(folders.size - 1)
//        }
//        itemsCount = folders.size - dividersPosition.size
//        return folders
//    }
//
//    fun onItemCheckedChange(position: Int, isChecked:Boolean): Pair<Int, Boolean> {
//        if(isChecked) checkedItemsPosition.add(position) else checkedItemsPosition.remove(position)
//        return Pair(checkedItemsPosition.size, checkedItemsPosition.size == itemsCount)
//    }
//
//    fun onAllItemsSelected(): Pair<Int, Boolean> {
//        repeat(itemsCount + dividersPosition.size) {
//            if(!dividersPosition.contains(it))
//                checkedItemsPosition.add(it)
//        }
//        return Pair(checkedItemsPosition.size, true)
//    }
//
//    fun onAllItemsClear(): Pair<Int, Boolean> {
//        checkedItemsPosition.clear()
//        return Pair(checkedItemsPosition.size, false)
//    }
//
//    private fun prepareFileItems(volumes: ArrayList<VolumeManager.Volume>): ArrayList<DirItem> {
//        if(volumes.size > 0) {
//            val files = arrayListOf<DirItem>(DirItem.Divider(getString(R.string.files)))
//            files.addAll(volumes.map { DirItem.Item(it.name, it.path, ItemType.LIST_FILE_ITEM, R.drawable.ic_file_black) })
//            return files
//        }
//        return arrayListOf()
//    }
//
//    private fun prepareFolderItems(volumes: ArrayList<VolumeManager.Volume>): ArrayList<DirItem> {
//        if(volumes.size > 0) {
//            val folders = arrayListOf<DirItem>(DirItem.Divider(getString(R.string.folders)))
//            folders.addAll(volumes.map { DirItem.Item(it.name, it.path, ItemType.LIST_FOLDER_ITEM, R.drawable.ic_folder_black) })
//            return folders
//        }
//        return arrayListOf()
//    }
//
////    fun createNewFileOrFolder(name: String, isFile: Boolean) {
////        volumeManager.createFileOrFolder("${lastPath()}/$name", isFile).also {
////            if (it != null) fileOrFolderCreation.value = volumeToDirItem(it)
////        }
////    }
//
////    fun getSelectedItemNames(): List<String> = selectedItems.map { (it as DirItem.Item).itemName }
//
////    fun delete() = getSelectedItemPaths().forEach { deleteAndNotify(it) }
//    //    private fun deleteAndNotify(path: String) = viewModelScope.launch {
////        if(volumeManager.delete(path)) {
////            deletePercentage.value = ++percentage
////        }
////    }
//
////    private fun getSelectedItemPaths(): List<String> = selectedItems.map { (it as DirItem.Item).itemPath }
//    //    val deletePercentage = MutableLiveData<Int>()
////    private var percentage = 0
//
////    private fun volumeToDirItem(volume: VolumeManager.Volume): DirItem.Item =
////        DirItem.Item(volume.name, volume.path, if(volume.isFile) ItemType.LIST_FILE_ITEM else ItemType.LIST_FOLDER_ITEM)
//}