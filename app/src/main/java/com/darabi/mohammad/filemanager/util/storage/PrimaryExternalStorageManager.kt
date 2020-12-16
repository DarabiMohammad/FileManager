package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.os.Environment.getExternalStorageDirectory
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.PrimaryExternalStorage
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageItem
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

/**
 *  This Class Is Used To Interacting With Primary External Storage.
 */
class PrimaryExternalStorageManager @Inject constructor(private val application: Application) : AbstractStorageManager() {

    override val storageName: String get() = application.getString(R.string.internal_storage)

    @Suppress("DEPRECATION")
    override val storagePath: String by lazy { getExternalStorageDirectory().path }

    override val storage: StorageItem get() = PrimaryExternalStorage(storageName)

    init { CoroutineScope(SupervisorJob() + Dispatchers.Default).launch { initStorageTree() } }
}