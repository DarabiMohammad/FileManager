package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.util.EMPTY_STRING
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor (
    private val app: Application,
    private val volumeManager: VolumeManager
) : BaseViewModel(app) {

    private val pathSeparator = File.pathSeparator
    private var selectedItems = listOf<DirItem>()
    var checkedItemCount = 0
    var currentPath = EMPTY_STRING
    val fileOrFolderCreation = MutableLiveData<DirItem.Item>()

    val deletePercentage = MutableLiveData<Int>()
    private var percentage = 0

    private fun volumeToDirItem(volume: VolumeManager.Volume): DirItem.Item =
        DirItem.Item(volume.name, volume.path, if(volume.isFile) ItemType.LIST_FILE_ITEM else ItemType.LIST_FOLDER_ITEM)

    private fun lastPath(): String = currentPath.substring(currentPath.lastIndexOf(pathSeparator) + 1)

    private fun prepareFileItems(volumes: ArrayList<VolumeManager.Volume>): ArrayList<DirItem> {
        val files = arrayListOf<DirItem>(DirItem.Divider(getString(R.string.files)))
        files.addAll(volumes.map { DirItem.Item(it.name, it.path, ItemType.LIST_FILE_ITEM, R.drawable.ic_file_black) })
        return if(files.size == 1 && files[0] is DirItem.Divider) arrayListOf() else files
    }

    private fun prepareFolderItems(volumes: ArrayList<VolumeManager.Volume>): ArrayList<DirItem> {
        val folders = arrayListOf<DirItem>(DirItem.Divider(getString(R.string.folders)))
        folders.addAll(volumes.map { DirItem.Item(it.name, it.path, ItemType.LIST_FOLDER_ITEM, R.drawable.ic_folder_black) })
        return if(folders.size == 1 && folders[0] is DirItem.Divider) arrayListOf() else folders
    }

    private fun getSubDirsOrFiles(): ArrayList<DirItem> {
        val pair = volumeManager.getSubDirectoriesPath(lastPath())
        val folders = prepareFolderItems(pair.first)
        folders.addAll(prepareFileItems(pair.second))
        return folders
    }

    private fun deleteAndNotify(path: String) = viewModelScope.launch {
        if(volumeManager.delete(path)) {
            deletePercentage.value = ++percentage
        }
    }

    private fun getSelectedItemPaths(): List<String> = selectedItems.map { (it as DirItem.Item).itemPath }

    fun removeLastPath(): String {
        if(!currentPath.contains(pathSeparator)) return EMPTY_STRING
        currentPath = currentPath.substring(currentPath.indexOf(File.separator), currentPath.lastIndexOf(pathSeparator))
        return currentPath
    }

    fun getSubFiles(path: String): ArrayList<DirItem> {
        currentPath = if(path != currentPath) "$currentPath$pathSeparator$path" else path
        currentPath = if(currentPath.startsWith(pathSeparator)) currentPath.removePrefix(pathSeparator) else currentPath
        return getSubDirsOrFiles()
    }

    fun createNewFileOrFolder(name: String, isFile: Boolean) {
        volumeManager.createFileOrFolder("${lastPath()}/$name", isFile).also {
            if (it != null) fileOrFolderCreation.value = volumeToDirItem(it)
        }
    }

    fun onCheckStateChange(models: List<DirItem>, checkedItemCount: Int) {
        selectedItems = models
        this.checkedItemCount = checkedItemCount
    }

    fun getSelectedItemNames(): List<String> = selectedItems.map { (it as DirItem.Item).itemName }

    fun delete() = getSelectedItemPaths().forEach { deleteAndNotify(it) }
}