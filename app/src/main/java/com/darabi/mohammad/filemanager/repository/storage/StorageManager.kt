package com.darabi.mohammad.filemanager.repository.storage

import android.app.Application
import android.database.MergeCursor
import android.net.Uri
import android.provider.MediaStore
import android.text.format.Formatter
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.safeSuspendCall
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import java.io.File as javaFile
abstract class StorageManager {

    @Inject
    protected lateinit var app: Application

    @Suppress("UNCHECKED_CAST")
    fun getFiles(path: String?, isSplitModeEnabled: Boolean): Result<ArrayList<out BaseItem>> { //safeSuspendCall {
        return Result.success(addDividersIfNeeded(listFiles(path!!), isSplitModeEnabled))
    }

    suspend fun deleteFile(path: String): Result<Boolean> = safeSuspendCall { Result.success(javaFile(path).deleteRecursively()) }

    suspend fun getImages(): Result<List<File>> = safeSuspendCall { Result.success(getAllImages()) }

    fun createNewFolder(fileName: String, path: String, isSplitModeEnabled: Boolean): Result<Pair<ArrayList<BaseItem>, Int>> =
        Result.success(createFolder(fileName, path, isSplitModeEnabled))
//        safeSuspendCall {
//        Result.success(createFile(fileName, path, type, isSplitModeEnabled))
//    }

    protected fun listFiles(path: String): ArrayList<FileItem> = arrayListOf<FileItem>().apply {
        javaFile(path).listFiles()?.let { array ->
            val files = arrayListOf<FileItem>()
            array.forEach {
                if (it.isDirectory)
                    this.add(Directory(it.name, it.path, it.totalSpace.toString()))
                else
                    files.add(File(it.name, it.path, it.totalSpace.toString()))
            }
            this.sortBy { it.name.toLowerCase(Locale.getDefault()) }
            files.sortBy { it.name.toLowerCase(Locale.getDefault()) }
            this.addAll(files)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun addDividersIfNeeded(listFiles: ArrayList<out BaseItem>, isSplitModeEnabled: Boolean): ArrayList<BaseItem> =
        (listFiles as ArrayList<BaseItem>).apply {
            if (this.isNotEmpty()) this.add(EmptyDivider)
            if (isSplitModeEnabled) {
                this.firstOrNull { it is Directory }?.let { this.add(this.indexOf(it), FileDivider(app.getString(R.string.folders))) }
                this.firstOrNull { it is File }?.let { this.add(this.indexOf(it), FileDivider(app.getString(R.string.files))) }
            }
        }

    protected fun createFolder(fileName: String, path: String, isSplitModeEnabled: Boolean): Pair<ArrayList<BaseItem>, Int> =
        newFolder(path, fileName).run {
            val list = listFiles(path)
            val newFolder = list.find { it.name == fileName }
            var index = list.indexOf(newFolder)
            val result = arrayListOf(newFolder) as ArrayList<BaseItem>
            if (isSplitModeEnabled)
                if (list.size == 1) {
                    result.add(0, FileDivider(app.getString(R.string.folders)))
                    result.add(result.size, EmptyDivider)
                }
                else index++
            Pair(result, index)
        }

    private fun newFolder(path: String, fileName: String): Boolean =
        if(javaFile("$path${javaFile.separator}$fileName").mkdir())
            true
        else throw IOException(app.getString(R.string.dir_allready_exist))

    private fun newFile(path: String, fileName: String): Boolean =
        if (javaFile("$path${javaFile.separator}${addDefaultExtensionIfNeeded(fileName)}").createNewFile())
            true
        else throw IOException(app.getString(R.string.file_allready_exist))

    private fun addDefaultExtensionIfNeeded(fileName: String): String =
        if (fileName.contains(".")) fileName else "$fileName${app.getString(R.string.simple_txt_format)}"

    private fun getAllImages(): ArrayList<File> = arrayListOf<File>().run {
        val projection = arrayOf (
            MediaStore.MediaColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.SIZE
        )
        MergeCursor(
            arrayOf (
                app.contentResolver.query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, projection, null, null, null),
                app.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
            )
        ).use {
            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val columnIndexTitle = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val columnIndexSize = it.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            while (it.moveToNext()) {
                this.add(File(
                    it.getString(columnIndexTitle),
                    it.getString(columnIndexData),
                    Formatter.formatFileSize(app, it.getLong(columnIndexSize)),
                ))
            }
        }
        this
    }
}

