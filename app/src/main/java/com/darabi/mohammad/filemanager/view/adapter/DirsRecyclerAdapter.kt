package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.OnDirClickListener
import javax.inject.Inject

class DirsRecyclerAdapter @Inject constructor() : BaseAdapter<DirItem, BaseViewHolder<DirItem>>() {

    lateinit var callback: OnDirClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DirItem> =
        DirsViewHolder(inflateLayout(parent, R.layout.rcv_item_dir), callback)
}