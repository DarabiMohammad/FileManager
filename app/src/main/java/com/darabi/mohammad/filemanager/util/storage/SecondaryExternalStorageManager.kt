package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.os.Environment
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import kotlinx.coroutines.*
import javax.inject.Inject

class SecondaryExternalStorageManager @Inject constructor(private val application: Application) : AbstractStorageManager() {

    override val storageName: String get() = application.getString(R.string.sd_card)
    override val rootFiles: Int get() = -3
    override val storagePath: String get() = ""
    override val dcmiPath: String
        get() = TODO("Not yet implemented")
    override val downloadsPath: String
        get() = TODO("Not yet implemented")
    override val moviesPath: String
        get() = TODO("Not yet implemented")
    override val musicsPath: String
        get() = TODO("Not yet implemented")
    override val picturesPath: String
        get() = TODO("Not yet implemented")
    override val storage: StorageItem get() = SecondaryExternalStorage(storageName)

    //    init { initStorageTree() }

    override suspend fun picturesDirectory(): Result<ArrayList<BaseItem>> = adl()

    override suspend fun dcimDirectory(): Result<ArrayList<BaseItem>> = adl()

    override suspend fun downloadDirectory(): Result<ArrayList<BaseItem>> = adl()

    override suspend fun moviesDirectory(): Result<ArrayList<BaseItem>> = adl()

    override suspend fun musicsDirectory(): Result<ArrayList<BaseItem>> = adl()

    private suspend fun adl(): Result<ArrayList<BaseItem>> = coroutineScope {
        Result(null, Status.ERROR)
    }
}