package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.util.EMPTY_STRING
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor (
    private val app: Application,
    private val volumeManager: VolumeManager
) : BaseViewModel(app) {

    private val pathSeparator = File.pathSeparator
    var currentPath = EMPTY_STRING
    val fileOrFolderCreation = MutableLiveData<DirItem.Item>()

    private fun volumeToDirItem(volume: VolumeManager.Volume): DirItem.Item =
        DirItem.Item(volume.name, volume.path, if(volume.isFile) ItemType.LIST_FILE_ITEM else ItemType.LIST_FOLDER_ITEM)

    private fun lastPath(): String = currentPath.substring(currentPath.lastIndexOf(pathSeparator) + 1)

    fun removeLastPath(): String {
        if(!currentPath.contains(pathSeparator)) return EMPTY_STRING
        currentPath = currentPath.substring(currentPath.indexOf(File.separator), currentPath.lastIndexOf(pathSeparator))
        return currentPath
    }

    fun getSubFiles(path: String): List<DirItem.Item> {
        currentPath = if(path != currentPath) "$currentPath$pathSeparator$path" else path
        currentPath = if(currentPath.startsWith(pathSeparator)) currentPath.removePrefix(pathSeparator) else currentPath
        return getSubDirsOrFiles(lastPath())
    }

    fun createNewFileOrFolder(name: String, isFile: Boolean) {
        volumeManager.createFileOrFolder("$currentPath/$name", isFile).also {
            if (it != null) fileOrFolderCreation.value = volumeToDirItem(it)
        }
    }

    private fun getSubDirsOrFiles(path: String): List<DirItem.Item> =
        volumeManager.getSubDirectoriesPath(path).map {
            val itemType = if(it.isFile) ItemType.LIST_FILE_ITEM else ItemType.LIST_FOLDER_ITEM
            val imageRes = if(itemType == ItemType.LIST_FILE_ITEM) R.drawable.ic_file_black else R.drawable.ic_folder_black
            DirItem.Item(it.name, it.path, itemType, imageRes)
        }
}