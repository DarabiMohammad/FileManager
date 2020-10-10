package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes

sealed class DirItem {

    data class Divider (val itemType: ItemType) : DirItem()

    data class Item(
        override val itemName: String,
        override val itemPath: String,
        override val itemType: ItemType = ItemType.LIST_FOLDER_ITEM,
        @DrawableRes val imageRes: Int? = null,
    ) : DirItem(), BaseItem
}