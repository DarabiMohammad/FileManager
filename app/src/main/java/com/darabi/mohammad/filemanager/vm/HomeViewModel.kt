package com.darabi.mohammad.filemanager.vm

import android.app.Application
import com.darabi.mohammad.filemanager.model.StorageItem
import com.darabi.mohammad.filemanager.util.storage.StorageManager
import javax.inject.Inject

open class HomeViewModel @Inject constructor (
    private val app: Application,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    fun getStorages(): List<StorageItem> = storageManager.storages()
}