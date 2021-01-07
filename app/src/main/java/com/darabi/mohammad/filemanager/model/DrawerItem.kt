package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes

enum class StorageType { PRIMARY, SECONDARY, OTG }

enum class CategoryType {
    DCIM,
    DOWNLOAD,
    MOVIES,
    MUSICS,
    PICTURES,
    QUICK_ACCESS,
    RECENT_FILES,
    IMAGES,
    VIDEOS,
    AUDIO,
    DOCUMENTS,
    APKS
}

interface BaseDrawerItem : Item

interface DrawerItem : BaseDrawerItem {

    val name: String

    @get:DrawableRes
    val icon: Int
}

interface StorageItem : DrawerItem {

    val path: String
    val type: StorageType
}

interface BaseCategory : DrawerItem {
    val type: CategoryType
}

interface DocumentItem : BaseCategory {

    val path: String
}

object Divider : BaseDrawerItem

data class PrimaryExtStorage(override val name: String, override val path: String,
                             override val type: StorageType, override val icon: Int
                             ) : StorageItem

data class Category(override val name: String, override val icon: Int, override val type: CategoryType) : BaseCategory

data class Document (
    override val name: String,
    override val type: CategoryType,
    override val path: String,
    override val icon: Int
) : DocumentItem

data class InstalledApps(override val name: String, override val icon: Int) : DrawerItem

data class Settings(override val name: String, override val icon: Int) : DrawerItem