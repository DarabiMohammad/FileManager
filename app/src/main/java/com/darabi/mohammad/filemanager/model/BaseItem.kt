package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes

enum class ItemType {
    DRAWER_ITEM_STORAGE,
    DRAWER_ITEM_OTHER,
    LIST_FOLDER_ITEM,
    LIST_FILE_ITEM,
    LIST_DIVIDER
}

interface BaseItem {

    val itemName: String
    val itemPath: String
    val itemType: ItemType
    @get:DrawableRes val itemImageRes: Int?
}