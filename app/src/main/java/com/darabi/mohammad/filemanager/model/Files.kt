package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes
import com.darabi.mohammad.filemanager.R

interface BaseItem : Item

interface BaseFile : BaseItem{

    val name: String
}

interface FileItem : BaseFile {

    @get:DrawableRes
    val icon: Int

    val treePosition: Int
}

object EmptyDivider : BaseItem

data class FileDivider (override val name: String) : BaseFile

data class Directory(
    override val name: String,
    override val icon: Int = R.drawable.ic_folder_black,
    override val treePosition: Int
): FileItem

data class File(
    override val name: String,
    override val icon: Int = R.drawable.ic_file_black,
    override val treePosition: Int
): FileItem