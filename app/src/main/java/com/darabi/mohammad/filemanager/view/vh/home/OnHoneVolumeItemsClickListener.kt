package com.darabi.mohammad.filemanager.view.vh.home

import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.storage.VolumeManager

interface OnHoneVolumeItemsClickListener {

    fun onVolumeClick(volume: DirItem.Item)
}