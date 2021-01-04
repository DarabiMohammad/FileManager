package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes

interface BaseDrawerItem : Item

interface DrawerItem : BaseDrawerItem {

    val name: String

    @get:DrawableRes
    val icon: Int
}

enum class CategoryType { DCIM, DOWNLOAD, MOVIES, MUSICS, PICTURES, QUICK_ACCESS, RECENT_FILES, IMAGES, VIDEOS, AUDIO, DOCUMENTS, APKS }

interface BaseCategory : DrawerItem {
    val type: CategoryType
}

object Divider : BaseDrawerItem

data class PrimaryExtStorage(override val name: String, val path: String, override val icon: Int) : DrawerItem

data class Category(override val name: String, override val icon: Int, override val type: CategoryType) : BaseCategory

data class InstalledApps(override val name: String, override val icon: Int) : DrawerItem

data class Settings(override val name: String, override val icon: Int) : DrawerItem