package com.darabi.mohammad.filemanager.model

sealed class DrawerItem {

    object Divider: DrawerItem()

    data class Item(
        override val itemName: String,
        override val itemPath: String,
        override val itemImageRes: Int?
    ) : DrawerItem(), BaseItem {
        override val itemType: ItemType get() = ItemType.DRAWER_ITEM
    }
}