package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListViewModel @Inject constructor(
    private val app: Application,
    private val volumeManager: VolumeManager
) : BaseViewModel(app) {

    private var currentPath = VolumeManager.EMPTY_STRING
    val fileOrFolderCreation = MutableLiveData<DirItem.Item>()

    private fun volumeToDirItem(volume: VolumeManager.Volume): DirItem.Item {
        return DirItem.Item(volume.name, volume.path, if(volume.isFile) ItemType.LIST_FILE_ITEM else ItemType.LIST_FOLDER_ITEM)
    }

    fun getSubFiles(path: String): List<DirItem.Item> {
        currentPath = path
        return volumeManager.getSubDirectoriesPath(path).map {
            val itemType = if(it.isFile) ItemType.LIST_FILE_ITEM else ItemType.LIST_FOLDER_ITEM
            val imageRes = if(itemType == ItemType.LIST_FILE_ITEM) R.drawable.ic_file_black else R.drawable.ic_folder_black
            DirItem.Item(it.name, it.path, itemType, imageRes)
        }
    }

    fun createNewFileOrFolder(name: String, isFile: Boolean) {
        volumeManager.createFileOrFolder("$currentPath/$name", isFile).also {
            if (it != null) fileOrFolderCreation.value = volumeToDirItem(it)
        }
    }
}