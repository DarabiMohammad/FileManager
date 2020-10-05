package com.darabi.mohammad.filemanager.view.vh.drawer

import com.darabi.mohammad.filemanager.model.DrawerItem
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback

interface OnDrawerItemClickListener : BaseAdapterCallback {

    fun onDrawerItemClick(item: DrawerItem.Item)
}