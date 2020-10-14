package com.darabi.mohammad.filemanager.model

sealed class DirItem {

    data class Divider (val itemName: String) : DirItem()

    data class Item(
        override val itemName: String,
        override val itemPath: String,
        override val itemType: ItemType = ItemType.LIST_FOLDER_ITEM,
        override val itemImageRes: Int? = null
    ) : DirItem(), BaseItem
}