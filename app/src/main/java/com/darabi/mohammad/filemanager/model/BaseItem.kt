package com.darabi.mohammad.filemanager.model

enum class ItemType {
    DRAWER_ITEM,
    LIST_FOLDER_ITEM
}

interface BaseItem {

    val itemName: String
    val itemPath: String
    val itemType: ItemType
}