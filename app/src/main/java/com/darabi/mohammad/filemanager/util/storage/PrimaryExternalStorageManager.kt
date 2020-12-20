package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.getExternalStoragePublicDirectory
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.PrimaryExternalStorage
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *  This Class Is Used To Interacting With Primary External Storage.
 */
class PrimaryExternalStorageManager @Inject constructor(private val application: Application) : AbstractStorageManager() {

    override val storageName: String get() = application.getString(R.string.internal_storage)
    override val rootFiles: Int get() = -2

    @Suppress("DEPRECATION")
    override val storagePath: String by lazy { getRootPath() }

    override val dcmiPath: String by lazy { dcmiPath() }
    override val downloadsPath: String by lazy { downloadsPath() }
    override val moviesPath: String by lazy { moviesPath() }
    override val musicsPath: String by lazy { musicsPath() }
    override val picturesPath: String by lazy { picturesPath() }

    override val storage: StorageItem get() = PrimaryExternalStorage(storageName)

    init {
        CoroutineScope(Job() + Default).launch { initStorageTree() }
    }

    override suspend fun dcimDirectory(): Result<ArrayList<BaseItem>> = runIfFilesTreeInitilized {
        getMediaFiles(dcimDirectoryPosition!!)
    }

    override suspend fun downloadDirectory(): Result<ArrayList<BaseItem>> = runIfFilesTreeInitilized {
        getMediaFiles(downloadDctoryPosition!!)
    }

    override suspend fun moviesDirectory(): Result<ArrayList<BaseItem>> = runIfFilesTreeInitilized {
        getMediaFiles(moviesDirectoryPosition!!)
    }

    override suspend fun musicsDirectory(): Result<ArrayList<BaseItem>> = runIfFilesTreeInitilized {
        getMediaFiles(musicsDirectoryPosition!!)
    }

    override suspend fun picturesDirectory(): Result<ArrayList<BaseItem>> = runIfFilesTreeInitilized {
        getMediaFiles(picturesDirectoryPosition!!)
    }

    @Suppress("DEPRECATION")
    private fun getRootPath(): String = getExternalStorageDirectory().path

    @Suppress("DEPRECATION")
    private fun dcmiPath(): String = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path

    @Suppress("DEPRECATION")
    private fun downloadsPath(): String = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path

    @Suppress("DEPRECATION")
    private fun moviesPath(): String = getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path

    @Suppress("DEPRECATION")
    private fun musicsPath(): String = getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).path

    @Suppress("DEPRECATION")
    private fun picturesPath(): String = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
}

