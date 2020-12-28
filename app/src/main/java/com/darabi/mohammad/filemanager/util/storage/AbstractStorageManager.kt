package com.darabi.mohammad.filemanager.util.storage

import com.darabi.mohammad.filemanager.model.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.CancellationException

abstract class AbstractStorageManager {

    val perviousFiles = -1

    abstract val storage: StorageItem

    protected abstract val rootFiles: Int
    protected abstract val storageName: String
    protected abstract val storagePath: String

    protected abstract val dcmiPath: String
    protected abstract val downloadsPath: String
    protected abstract val moviesPath: String
    protected abstract val musicsPath: String
    protected abstract val picturesPath: String

    protected var filesTree: DirectoryHolder? = null

    protected var dcimDirectoryPosition: Int? = null
    protected var downloadDctoryPosition: Int? = null
    protected var moviesDirectoryPosition: Int? = null
    protected var musicsDirectoryPosition: Int? = null
    protected var picturesDirectoryPosition: Int? = null

    private var visitedPositions = mutableListOf<Int>()
    private var lastMediaPosition: Int? = null

    abstract suspend fun dcimDirectory(): Result<ArrayList<BaseItem>>

    abstract suspend fun downloadDirectory(): Result<ArrayList<BaseItem>>

    abstract suspend fun moviesDirectory(): Result<ArrayList<BaseItem>>

    abstract suspend fun musicsDirectory(): Result<ArrayList<BaseItem>>

    abstract suspend fun picturesDirectory(): Result<ArrayList<BaseItem>>

//    abstract suspend fun allImages(): Result<ArrayList<Media>>

    suspend fun getFiles(position: Int = rootFiles): Result<ArrayList<BaseItem>> = runIfFilesTreeInitilized {
        Result.success(filesAt(position))
    }

    suspend fun createFile(fileName: String): Result<FileItem> = coroutineScope {
        val currentDirectory = fetchFilesFromFileTree()
        val newFile = DirectoryHolder(fileName, "${currentDirectory.path}/$fileName", rootFiles, arrayListOf())
        currentDirectory.subFiles.add(newFile)
        currentDirectory.subFiles.sortedWith { o1, o2 -> o1.name.compareTo(o2.name) }
        for(i in currentDirectory.subFiles.indices) { currentDirectory.subFiles[i].position = i }
        Result.loading()
    }

    protected suspend fun initStorageTree() = coroutineScope {
        File(storagePath).listFiles()?.let { filesTree = DirectoryHolder(storageName, storagePath, rootFiles, initTree(it.sorted())) }
    }

    protected suspend fun getMediaDirectory(position: Int): Result<ArrayList<BaseItem>> = coroutineScope {
        coroutineScope {
            lastMediaPosition?.let {
                if (position == it)
                    this.cancel(CancellationException("This Media Directory Already Selected!."))
            }
        }
        visitedPositions.add(rootFiles).also {
            visitedPositions.add(position)
            lastMediaPosition = position
        }.run { Result.success(mapToFileItems(fetchFilesFromFileTree())) }
    }

    protected suspend inline fun <R> runIfFilesTreeInitilized(crossinline function: suspend () -> R): R = coroutineScope {
        if (filesTree == null)
            withContext(Default) { initStorageTree() }
        function.invoke()
    }

    private suspend fun filesAt(position: Int): ArrayList<BaseItem> = coroutineScope {
        addOrRemovePosition(position).run { mapToFileItems(fetchFilesFromFileTree()) }
    }

    private fun addOrRemovePosition(position: Int) = visitedPositions.apply {
        when (position) {
            perviousFiles -> visitedPositions.removeLast().also {
                if (visitedPositions.size > 1 && visitedPositions.last() == rootFiles)
                    visitedPositions.removeLast().also { lastMediaPosition = null }
            }
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
                for(i in this.indices) {
                    this[i].position = i
                    initMediaPositions(this[i].path, i)
                }
            }
            this
        }
    }

    private fun initMediaPositions(path: String, position: Int) = when (path) {
        dcmiPath -> dcimDirectoryPosition = position
        downloadsPath -> downloadDctoryPosition = position
        moviesPath -> moviesDirectoryPosition = position
        musicsPath -> musicsDirectoryPosition = position
        picturesPath -> picturesDirectoryPosition = position
        else -> null
    }

    protected interface FilesTree {

        val name: String
        val path: String
        var position: Int
    }

    protected data class FileHolder(override val name: String, override val path: String, override var position: Int) : FilesTree {
        override fun toString(): String = name
    }

    protected data class DirectoryHolder(override val name: String,
                                         override val path: String,
                                         override var position: Int,
                                         val subFiles: ArrayList<FilesTree>
                                         ) : FilesTree {
        override fun toString(): String = name
                                         }
}