package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.RecyclerViewHolder
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import javax.inject.Inject

class RecyclerAdapter @Inject constructor() : BaseAdapter<String, BaseViewHolder<String>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder =
        RecyclerViewHolder(inflateLayout(parent, R.layout.rcv_item_storage_dev))
}