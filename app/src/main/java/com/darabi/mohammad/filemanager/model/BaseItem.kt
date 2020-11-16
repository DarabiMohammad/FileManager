package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes

enum class ItemType {
    DRAWER_ITEM_CATEGORY,
    DRAWER_ITEM_OTHER,
    LIST_FOLDER_ITEM,
    LIST_FILE_ITEM
}

interface BaseItem {

    val itemName: String
    val itemPath: String
    val itemType: ItemType
    @get:DrawableRes val itemImageRes: Int?
}