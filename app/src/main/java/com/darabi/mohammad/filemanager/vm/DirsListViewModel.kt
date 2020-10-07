package com.darabi.mohammad.filemanager.vm

import android.app.Application
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject

class DirsListViewModel @Inject constructor(
    private val app: Application,
    private val volumeManager: VolumeManager
) : BaseViewModel(app) {

    fun getSubFiles(path: String) = volumeManager.getSubDirectoriesPath(path).map {
        val itemType = if(it.isFile) ItemType.LIST_FILE_ITEM else ItemType.LIST_FOLDER_ITEM
        val imageRes = if(itemType == ItemType.LIST_FILE_ITEM) R.drawable.ic_file_black else R.drawable.ic_folder_black
        DirItem.Item(it.name, it.path, itemType, imageRes)
    }
}