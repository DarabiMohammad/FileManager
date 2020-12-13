package com.darabi.mohammad.filemanager.util.storage

import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.util.safeSuspendCall
import kotlinx.coroutines.*
import java.io.File
import java.lang.NullPointerException

abstract class AbstractStorageManager {

    val perviousFiles = -2

    private val rootFiles = -1
    private var filesTree: DirectoryHolder? = null
    private var visitedPositions = mutableListOf<Int>()

    protected abstract val storageName: String
    abstract val storage: StorageItem

    suspend fun getFiles(position: Int = rootFiles): Result<ArrayList<BaseItem>> = filesAt(position)

    protected abstract fun initStoragePath(): String?

    protected fun initStorageTree() = initStoragePath()?.let {
        CoroutineScope(Dispatchers.Main).launch {
            filesTree = DirectoryHolder(storageName, it, rootFiles, initTree(File(it).listFiles().takeIf { it != null }?.sorted()))
        }
    }

    private suspend fun filesAt(position: Int): Result<ArrayList<BaseItem>> = filesTree.let {
        if (it == null) initStoragePath()
        if (position == perviousFiles) visitedPositions.removeLast() else {
            if (position == rootFiles) visitedPositions.clear()
            visitedPositions.add(position)
        }
        safeSuspendCall { mapToFileItems(fetchFilesFromFileTree()) }
    }

    private fun mapToFileItems(directoryHolder: DirectoryHolder): Result<ArrayList<BaseItem>> = arrayListOf<BaseItem>().run {
        directoryHolder.subFiles.map {
            this.add(if (it is DirectoryHolder) Directory(it.name, treePosition = it.position) else File(it.name, treePosition = it.position))
        }.also { if (this.isNotEmpty()) this.add(EmptyDivider) }
        Result.success(this)
    }

    private fun fetchFilesFromFileTree(): DirectoryHolder {
        var directoryHolder: DirectoryHolder? = null
        for (i in visitedPositions) {
            directoryHolder = if (i == rootFiles) filesTree else directoryHolder!!.subFiles[i] as DirectoryHolder
        }
        return directoryHolder!!
    }

    private suspend fun initTree(files: List<File>?): ArrayList<FilesTree> = withContext(Dispatchers.Default) {
        arrayListOf<FilesTree>().run {
            files?.let { it ->
                val filesList = arrayListOf<FileHolder>()
                for (position in it.indices) {
                    if (it[position].isFile)
                        filesList.add(FileHolder(it[position].name, it[position].path, position))
                    else
                        this.add(DirectoryHolder(it[position].name, it[position].path, position, initTree(it[position].listFiles()?.toList())))
                }
                // todo : implementing all neccessary stufs fo sorting items by their name
                this.addAll(filesList)
                for(i in this.indices) { this[i].position = i }
            }
            this
        }
    }

    protected interface FilesTree {

        val name: String
        val path: String
        var position: Int
    }

    protected data class FileHolder(override val name: String, override val path: String, override var position: Int) : FilesTree

    protected data class DirectoryHolder(override val name: String, override val path: String, override var position: Int, val subFiles: ArrayList<FilesTree>) : FilesTree
}