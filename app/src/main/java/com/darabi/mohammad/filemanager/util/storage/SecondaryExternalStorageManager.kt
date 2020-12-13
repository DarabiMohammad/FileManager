package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.os.Environment
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.SecondaryExternalStorage
import com.darabi.mohammad.filemanager.model.StorageItem
import javax.inject.Inject

class SecondaryExternalStorageManager @Inject constructor(private val application: Application) : AbstractStorageManager() {

    override val storageName: String get() = application.getString(R.string.sd_card)
    override val storage: StorageItem get() = SecondaryExternalStorage(storageName)

    init { initStorageTree() }

    override fun initStoragePath(): String? = null
}