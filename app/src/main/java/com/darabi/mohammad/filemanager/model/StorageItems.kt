package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes
import com.darabi.mohammad.filemanager.R

interface StorageVolume : Item {

    val name: String
    val path: String
    val totalSpace: String
    val freeSpace: String
//    val isPrimary: Boolean
//    val isRemovable: Boolean
//    val isEmulated: Boolean

    @get:DrawableRes
    val icon: Int
}

data class ExternalStorage(
    override val name: String,
    override val path: String,
    override val totalSpace: String,
    override val freeSpace: String,
//    override val isPrimary: Boolean,
//    override val isRemovable: Boolean,
//    override val isEmulated: Boolean,
    override val icon: Int = R.drawable.ic_folder_black
) : StorageVolume