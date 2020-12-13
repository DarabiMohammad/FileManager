package com.darabi.mohammad.filemanager.util.storage

import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.BaseResult
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor(
    private val primaryStorageManager: PrimaryExternalStorageManager,
    private val secondaryStorageManager: SecondaryExternalStorageManager
) {

    // todo : change this method body for returning only available storages
    fun storages(): List<StorageItem> = listOf(primaryStorageManager.storage, secondaryStorageManager.storage)

    suspend fun getPrimaryStorageRootFiles(): Result<ArrayList<BaseItem>> = primaryStorageManager.getFiles()

    suspend fun getSecondaryStorageRootFiles(): Result<ArrayList<BaseItem>> = secondaryStorageManager.getFiles()

    suspend fun getFiles(position: Int): Result<List<BaseItem>> = primaryStorageManager.getFiles(position)

    suspend fun backToPerviousFolder(): Result<List<BaseItem>> = primaryStorageManager.getFiles(primaryStorageManager.perviousFiles)
}