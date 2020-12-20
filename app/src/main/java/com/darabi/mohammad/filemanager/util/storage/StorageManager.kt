package com.darabi.mohammad.filemanager.util.storage

import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageItem
import com.darabi.mohammad.filemanager.util.safeSuspendCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor(
    private val primaryStorageManager: PrimaryExternalStorageManager,
    private val secondaryStorageManager: SecondaryExternalStorageManager
) {

    private var storageManager: AbstractStorageManager = primaryStorageManager

    // todo : change this method body for returning only available storages
    fun storages(): List<StorageItem> = listOf(primaryStorageManager.storage, secondaryStorageManager.storage)

    suspend fun getPrimaryStorageRootFiles(): Result<ArrayList<BaseItem>> = safeSuspendCall {
        primaryStorageManager.getFiles()
    }.also { storageManager = primaryStorageManager }

    suspend fun getSecondaryStorageRootFiles(): Result<ArrayList<BaseItem>> = safeSuspendCall {
        secondaryStorageManager.getFiles()
    }.also { storageManager = secondaryStorageManager }

    suspend fun getFiles(position: Int): Result<ArrayList<BaseItem>> = safeSuspendCall { storageManager.getFiles(position) }

    suspend fun backToPerviousFolder(): Result<ArrayList<BaseItem>> = safeSuspendCall { storageManager.getFiles(storageManager.perviousFiles) }

    suspend fun dcimDirectory() = safeSuspendCall { storageManager.dcimDirectory() }
    suspend fun downloadDirectory() = safeSuspendCall { storageManager.downloadDirectory() }
    suspend fun moviesDirectory() = safeSuspendCall { storageManager.moviesDirectory() }
    suspend fun musicsDirectory() = safeSuspendCall { storageManager.musicsDirectory() }
    suspend fun picturesDirectory() = safeSuspendCall { storageManager.picturesDirectory() }
    suspend fun allImages() = {}
    suspend fun allViedes() {}
    suspend fun allAudios() {}
    suspend fun allDocuments() {}
    suspend fun allApks() {}
}