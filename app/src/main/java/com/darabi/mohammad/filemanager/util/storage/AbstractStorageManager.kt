package com.darabi.mohammad.filemanager.util.storage

import android.media.UnsupportedSchemeException
import androidx.annotation.DrawableRes
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import java.io.File

abstract class AbstractStorageManager {

    protected interface FilesTree {

        val name: String
        val path: String
        val position: Int
    }

    protected data class FileHolder(override val name: String, override val path: String, override val position: Int) : FilesTree

    protected data class DirectoryHolder(override val name: String, override val path: String, override val position: Int, val subFiles: ArrayList<FilesTree>) : FilesTree

    protected abstract val storageName: String

    protected abstract val filesTree: DirectoryHolder

    abstract val storage: StorageItem

    private var visitedPositions = arrayListOf<Int>()

    abstract suspend fun getFiles(position: Int = -1): Result<ArrayList<BaseItem>>

    protected fun initStorageTree(path: String) =
        DirectoryHolder(storageName, path,-1, initTree(File(path).listFiles()!!.sorted()))

    protected fun filesAt(position: Int): Result<ArrayList<BaseItem>> = position.run {
        if (position < 0) mapToFileItems(filesTree) else visitedPositions.add(this).run { mapToFileItems(fetchFilesFromFileTree()) }
    }

    private fun mapToFileItems(directoryHolder: DirectoryHolder): Result<ArrayList<BaseItem>> = arrayListOf<BaseItem>().run {
        directoryHolder.subFiles.map {
            val file = if (it is DirectoryHolder) Directory(it.name, treePosition = it.position) else File(it.name, treePosition = it.position)
            this.add(file)
        }.also { this.add(EmptyDivider) }
        Result.success(this)
    }

    private fun fetchFilesFromFileTree(): DirectoryHolder {
        var directoryHolder: DirectoryHolder? = null
        for (i in visitedPositions) {
            directoryHolder = if (directoryHolder == null)
                filesTree.subFiles[i] as DirectoryHolder
            else directoryHolder.subFiles[i] as DirectoryHolder
        }
        return directoryHolder!!
    }

    private fun initTree(files: List<File>?): ArrayList<FilesTree> = arrayListOf<FilesTree>().run {
        files?.let {
            for (position in it.indices) {
                if (it[position].isFile)
                    this.add(FileHolder(it[position].name, it[position].path, position))
                else
                    this.add(DirectoryHolder(it[position].name, it[position].path, position, initTree(it[position].listFiles()?.toList())))
            }
        }
        this
    }
}