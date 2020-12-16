package com.darabi.mohammad.filemanager.util.storage

import com.darabi.mohammad.filemanager.model.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File

abstract class AbstractStorageManager {

    val perviousFiles = -2

    private val rootFiles = -1
    private var filesTree: DirectoryHolder? = null
    private var visitedPositions = mutableListOf<Int>()

    protected abstract val storageName: String
    protected abstract val storagePath: String
    abstract val storage: StorageItem

    suspend fun getFiles(position: Int = rootFiles): Result<ArrayList<BaseItem>> = coroutineScope {
        if (filesTree == null)
            withContext(Default) { initStorageTree() }
        Result(result = filesAt(position), Status.SUCCESS)
    }

    protected suspend fun initStorageTree() = coroutineScope {
        File(storagePath).listFiles()?.let { filesTree = DirectoryHolder(storageName, storagePath, rootFiles, initTree(it.sorted())) }
    }

    private suspend fun filesAt(position: Int): ArrayList<BaseItem> = coroutineScope {
        addOrRemovePosition(position).run { mapToFileItems(fetchFilesFromFileTree()) }
    }

    private fun addOrRemovePosition(position: Int) = visitedPositions.apply {
        when (position) {
            perviousFiles -> visitedPositions.removeLast()
            rootFiles -> visitedPositions.clear().also { visitedPositions.add(rootFiles) }
            else -> visitedPositions.add(position)
        }
    }

    private suspend fun mapToFileItems(directoryHolder: DirectoryHolder): ArrayList<BaseItem> = coroutineScope {
        arrayListOf<BaseItem>().run {
            directoryHolder.subFiles.map {
                this.add(
                    if (it is DirectoryHolder)
                        Directory(it.name, treePosition = it.position)
                    else
                        File(it.name, treePosition = it.position)
                )
            }.also { if (isNotEmpty()) add(EmptyDivider) }
            this
        }
    }

    private suspend fun fetchFilesFromFileTree(): DirectoryHolder = coroutineScope {
        var directoryHolder: DirectoryHolder? = null
        for (i in visitedPositions) {
            directoryHolder = if (i == rootFiles) filesTree else directoryHolder!!.subFiles[i] as DirectoryHolder
        }
        directoryHolder!!
    }

    private suspend fun initTree(files: List<File>?): ArrayList<FilesTree> = coroutineScope {
        arrayListOf<FilesTree>().run {
            files?.let { it ->
                val filesList = arrayListOf<FileHolder>()
                for (position in it.indices) {
                    if (it[position].isFile)
                        filesList.add(FileHolder(it[position].name, it[position].path, position))
                    else
                        this.add(DirectoryHolder(it[position].name, it[position].path, position, initTree(it[position].listFiles()?.toList())))
                }
                // todo : implementing all neccessary stufs for sorting items by their name
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