package com.darabi.mohammad.filemanager.model

enum class ItemType {
    DRAWER_ITEM,
    LIST_FOLDER_ITEM,
    LIST_FILE_ITEM,
    LIST_DIVIDER
}

interface BaseItem {

    val itemName: String
    val itemPath: String
    val itemType: ItemType
}