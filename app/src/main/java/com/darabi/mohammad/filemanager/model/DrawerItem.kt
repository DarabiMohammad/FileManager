package com.darabi.mohammad.filemanager.model

sealed class DrawerItem {

    object Divider: DrawerItem()

    data class Item(
            override val itemName: String,
            override val itemPath: String,
            override val itemType: ItemType = ItemType.DRAWER_ITEM_CATEGORY,
            override val itemImageRes: Int?
    ) : DrawerItem(), BaseItem
}