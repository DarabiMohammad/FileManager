package com.darabi.mohammad.filemanager.repository.storage

import android.app.Application
import android.database.MergeCursor
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

    suspend fun getFiles(path: String?, isSplitModeEnabled: Boolean): Result<ArrayList<out BaseItem>> = safeSuspendCall {
        Result.success(addDividersIfNeeded(listFiles(path!!), isSplitModeEnabled))
    }

    suspend fun getFolders(path: String?): Result<ArrayList<Directory>> = safeSuspendCall {
        Result.success(folders(javaFile(path!!).listFiles()!!))
    }

    suspend fun createNewFolder(fileName: String, path: String, isSplitModeEnabled: Boolean): Result<Pair<ArrayList<BaseItem>, Int>> = safeSuspendCall {
        Result.success(createFolder(fileName, path, isSplitModeEnabled))
    }

    suspend fun createNewFolder(fileName: String, path: String): Result<Pair<Directory, Int>> = safeSuspendCall {
        Result.success(createFolder(fileName, path))
    }

    suspend fun deleteFile(file: FileItem): Result<FileItem> = safeSuspendCall {
        if (javaFile(file.path).deleteRecursively())
            Result.success(file)
        else throw IOException(app.getString(R.string.delete_file_error))
    }

    suspend fun copy(fileName: String, destinationPath: String) {}

    suspend fun getImages(): Result<ArrayList<File>> = safeSuspendCall { Result.success(getAllImages()) }

    protected fun listFiles(path: String): ArrayList<FileItem> = arrayListOf<FileItem>().apply {
        javaFile(path).listFiles()?.let { this.addAll(folders(it).plus(files(it))) }
    }

    private fun folders(list: Array<javaFile>): ArrayList<Directory> = arrayListOf<Directory>().apply {
        this.addAll(list.filter { it.isDirectory }.map {
            Directory(it.name, it.path, it.totalSpace.toString())
        }.sortedBy { it.name.toLowerCase(Locale.getDefault()) })
    }

    private fun files(list: Array<javaFile>): ArrayList<File> = arrayListOf<File>().apply {
        this.addAll(list.filter { it.isFile }.map {
            File(it.name, it.path, it.totalSpace.toString())
        }.sortedBy { it.name.toLowerCase(Locale.getDefault()) })
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

    protected fun createFolder(fileName: String, path: String): Pair<Directory, Int> = newFolder(path, fileName).run {
        val list = folders(javaFile(path).listFiles()!!)
        val createdFolder = list.find { it.name == fileName } as Directory
        val index = list.indexOf(createdFolder)
        Pair(createdFolder, index)
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