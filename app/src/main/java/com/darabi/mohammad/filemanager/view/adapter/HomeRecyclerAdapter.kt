package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.view.vh.home.HomeRecyclerViewHolder
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import javax.inject.Inject

class HomeRecyclerAdapter @Inject constructor() : BaseAdapter<DirItem, BaseViewHolder<DirItem>>() {

    lateinit var callback: BaseAdapterCallback<DirItem.Item>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder =
        HomeRecyclerViewHolder(inflateLayout(parent, R.layout.rcv_item_storage_dev), callback)
}