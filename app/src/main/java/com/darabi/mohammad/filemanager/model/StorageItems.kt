package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes
import com.darabi.mohammad.filemanager.R

interface StorageItem : Item {

    val name: String

    @get:DrawableRes
    val icon: Int
}

data class PrimaryExternalStorage(override val name: String, override val icon: Int = R.drawable.ic_folder_black) : StorageItem

data class SecondaryExternalStorage(override val name: String, override val icon: Int = R.drawable.ic_folder_black) : StorageItem