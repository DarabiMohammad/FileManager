package com.darabi.mohammad.filemanager.view.adapter.home

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.BaseViewHolder
import com.darabi.mohammad.filemanager.view.adapter.home.HomeRecyclerViewHolder
import javax.inject.Inject

class HomeRecyclerAdapter @Inject constructor() : BaseAdapter<StorageVolume, BaseViewHolder<StorageVolume>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder =
        HomeRecyclerViewHolder(inflateLayout(parent, R.layout.rcv_item_storage_dev), callback)
}