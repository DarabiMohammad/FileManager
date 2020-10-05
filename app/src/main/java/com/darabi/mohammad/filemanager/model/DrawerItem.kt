package com.darabi.mohammad.filemanager.model

import androidx.annotation.DrawableRes

sealed class DrawerItem {

    object Divider: DrawerItem()

    data class Item(
        override val itemName: String,
        override val itemPath: String,
        @DrawableRes val imageRes: Int,
    ) : DrawerItem(), BaseItem {
        override val itemType: ItemType get() = ItemType.DRAWER_ITEM
    }
}