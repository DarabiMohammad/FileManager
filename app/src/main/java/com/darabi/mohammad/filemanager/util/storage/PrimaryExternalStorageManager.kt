package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.os.Environment.getExternalStorageDirectory
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.PrimaryExternalStorage
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageItem
import java.io.File
import javax.inject.Inject

/**
 *  This Class Is Used To Interacting With Primary External Storage.
 */
class PrimaryExternalStorageManager @Inject constructor(private val application: Application) : AbstractStorageManager() {

    override val storageName: String get() = application.getString(R.string.internal_storage)

    override val storage: StorageItem get() = PrimaryExternalStorage(storageName)
    override val filesTree: DirectoryHolder by lazy { initStorageTree(getExternalStorageDirectory().path) }

    override suspend fun getFiles(position: Int): Result<ArrayList<BaseItem>> = filesAt(position)















    /**
     * Handles Instantiation Of Primary External Storage In Different Android Api Levels By Calling
     * Appropreate Method For Each Api Level.
     *
     * @return Primary External Storage Path And Name Through Directory Data Holder Class.
     */
//    private fun initExternalStorage(): Directory = getExternalStorageInOldDevice().also { initStorageTree(it.path) }
//
//    @Suppress("DEPRECATION")
//    private fun getExternalStorageInOldDevice(): Directory = Directory(storageName, getExternalStorageDirectory().path, PRIMARY_EXTERNAL_STORAGE_POSITION)

    private fun files(files: List<File>): List<File> = files.filter { it.isFile }

    private fun folders(files: List<File>): List<File> = files.filter { it.isDirectory }

    private fun getFileName(path: String): String = path.substring(path.lastIndexOf(File.separator) + 1)
}