package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.view.adapter.selection.HasSelectable

sealed class FileType {

    object Directory : FileType()

    object File : FileType()
}

interface BaseItem : Item, HasSelectable

interface BaseFile : BaseItem {

    val name: String
}

interface FileItem : BaseFile {

    override val isSelectable: Boolean get() = true

    val path: String
    val size: String

    @get:DrawableRes
    val icon: Int
}

object EmptyDivider : BaseItem { override val isSelectable: Boolean get() = false
    override var isSelected: Boolean
        get() = false
        set(value) {}
}

data class FileDivider (override val name: String, override var isSelected: Boolean = false) :
    BaseFile { override val isSelectable: Boolean get() = false }

data class Directory(
    override val name: String,
    override val path: String,
    override val size: String,
    override val icon: Int = R.drawable.ic_folder_black,
    override var isSelected: Boolean = false
): FileItem

data class File(
    override val name: String,
    override val path: String,
    override val size: String,
    override val icon: Int = R.drawable.ic_file_black,
    override var isSelected: Boolean = false
): FileItem

//data class Media (
//    override val name: String,
//    override val icon: String,
//    override val treePosition: Int
//) : FileItem