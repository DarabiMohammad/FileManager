package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.StorageItem
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import com.darabi.mohammad.filemanager.view.vh.home.HomeRecyclerViewHolder
import javax.inject.Inject

class HomeRecyclerAdapter @Inject constructor() : BaseAdapter<StorageItem, BaseViewHolder<StorageItem>>() {

    lateinit var callback: BaseAdapterCallback<StorageItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder =
        HomeRecyclerViewHolder(inflateLayout(parent, R.layout.rcv_item_storage_dev), callback)
}