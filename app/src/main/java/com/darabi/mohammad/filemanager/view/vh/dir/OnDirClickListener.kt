package com.darabi.mohammad.filemanager.view.vh.dir

import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback

interface OnDirClickListener : BaseAdapterCallback {

    fun onMoreClick(item: DirItem.Item)

    fun onLongClick(item: DirItem.Item): Boolean

    fun onClick(item: DirItem.Item)
}