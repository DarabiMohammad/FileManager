package com.darabi.mohammad.filemanager.model

import com.darabi.mohammad.filemanager.R

interface StorageVolume : StorageItem {

    val totalSpace: String
    val freeSpace: String
}

data class ExternalStorage(
    override val name: String,
    override val path: String,
    override val totalSpace: String,
    override val freeSpace: String,
    override val type: StorageType,
    override val icon: Int = R.drawable.ic_folder_black
) : StorageVolume