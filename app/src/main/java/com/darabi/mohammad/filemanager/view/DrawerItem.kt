package com.darabi.mohammad.filemanager.view

import androidx.annotation.DrawableRes

sealed class DrawerItem {

    object Divider: DrawerItem()

    data class Item(val title: String, @DrawableRes val imageRes: Int) : DrawerItem()
}