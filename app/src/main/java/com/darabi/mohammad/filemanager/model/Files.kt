package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes
import com.darabi.mohammad.filemanager.R

sealed class FileType {

    object Directory : FileType()

    object File : FileType()
}

interface BaseItem : Item

interface BaseFile : BaseItem {

    val name: String
}

interface FileItem : BaseFile {

    val path: String
    val size: String

    @get:DrawableRes
    val icon: Int
}

object EmptyDivider : BaseItem

data class FileDivider (override val name: String) : BaseFile

data class Directory(
    override val name: String,
    override val path: String,
    override val size: String,
    override val icon: Int = R.drawable.ic_folder_black
): FileItem

data class File(
    override val name: String,
    override val path: String,
    override val size: String,
    override val icon: Int = R.drawable.ic_file_black
): FileItem

//data class Media (
//    override val name: String,
//    override val icon: String,
//    override val treePosition: Int
//) : FileItem