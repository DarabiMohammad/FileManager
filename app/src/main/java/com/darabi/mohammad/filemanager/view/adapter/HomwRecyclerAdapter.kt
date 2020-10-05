package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.HomeRecyclerViewHolder
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import javax.inject.Inject

class HomwRecyclerAdapter @Inject constructor() : BaseAdapter<VolumeManager.Volume, BaseViewHolder<VolumeManager.Volume>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder =
        HomeRecyclerViewHolder(inflateLayout(parent, R.layout.rcv_item_storage_dev))
}