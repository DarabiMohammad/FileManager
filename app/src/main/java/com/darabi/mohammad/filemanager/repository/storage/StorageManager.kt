package com.darabi.mohammad.filemanager.repository.storage

import android.app.Application
import android.os.Environment.*
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.safeSuspendCall
import java.io.File
import java.io.IOException
import javax.inject.Inject

abstract class StorageManager {

    @Inject
    protected lateinit var app: Application

    suspend fun getFiles(path: String?): Result<ArrayList<FileItem>> = safeSuspendCall {
        Result.success(listFiles(path!!))
    }

    suspend fun createNewFile(fileName: String, path: String, type: FileType): Result<Pair<FileItem, Int>> = safeSuspendCall {
        Result.success(createFile(fileName, path, type))
    }

    @Suppress("DEPRECATION")
    open suspend fun dcimFolder(): Result<ArrayList<FileItem>> = safeSuspendCall {
        getFiles(getExternalStoragePublicDirectory(DIRECTORY_DCIM).path)
    }

    @Suppress("DEPRECATION")
    open suspend fun downloadFolder(): Result<ArrayList<FileItem>> = safeSuspendCall {
        getFiles(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path)
    }

    @Suppress("DEPRECATION")
    open suspend fun moviesFolder(): Result<ArrayList<FileItem>> = safeSuspendCall {
        getFiles(getExternalStoragePublicDirectory(DIRECTORY_MOVIES).path)
    }

    @Suppress("DEPRECATION")
    open suspend fun musicsFolder(): Result<ArrayList<FileItem>> = safeSuspendCall {
        getFiles(getExternalStoragePublicDirectory(DIRECTORY_MUSIC).path)
    }

    @Suppress("DEPRECATION")
    open suspend fun picturesFolder(): Result<ArrayList<FileItem>> = safeSuspendCall {
        getFiles(getExternalStoragePublicDirectory(DIRECTORY_PICTURES).path)
    }

    protected fun listFiles(path: String): ArrayList<FileItem> = arrayListOf<FileItem>().apply {
        File(path).listFiles()?.let { array ->
            val files = arrayListOf<com.darabi.mohammad.filemanager.model.File>()
            array.forEach {
                if (it.isDirectory)
                    this.add(Directory(it.name, it.path, it.totalSpace.toString()))
                else
                    files.add(File(it.name, it.path, it.totalSpace.toString()))
            }
            this.sortBy { it.name }
            files.sortBy { it.name }
            this.addAll(files)
        }
    }

    protected fun createFile(fileName: String, path: String, type: FileType): Pair<FileItem, Int> =
        createFileOrFolder(fileName, path, type).run {
            val list = listFiles(path)
            val newFolder = list.find { it.name == fileName }
            Pair(newFolder!!, list.indexOf(newFolder))
        }

    private fun createFileOrFolder(fileName: String, path: String, type: FileType) =
        if(type is FileType.Directory) newFolder(path, fileName) else newFile(path, fileName)

    private fun newFolder(path: String, fileName: String): Boolean =
        if(File("$path${File.separator}$fileName").mkdir())
            true
        else throw IOException(app.getString(R.string.dir_allready_exist))

    private fun newFile(path: String, fileName: String): Boolean =
        if (File("$path${File.separator}${addDefaultExtensionIfNeeded(fileName)}").createNewFile())
            true
        else throw IOException(app.getString(R.string.file_allready_exist))

    private fun addDefaultExtensionIfNeeded(fileName: String): String =
        if (fileName.contains("."))
            fileName
        else "$fileName${app.getString(R.string.simple_txt_format)}"
}