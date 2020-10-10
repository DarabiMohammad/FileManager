package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsViewHolder
import javax.inject.Inject

class DirsRecyclerAdapter @Inject constructor() : BaseCheckableAdapter<DirItem, CheckableViewHolder<DirItem>>(),
    DirsViewHolder.DirsViewHolderCallback<DirItem> {

    override lateinit var adapterCallback: CheckableAdapterCallback<DirItem>

    override fun onMoreOptionClick(model: DirItem) = adapterCallback.onMoreOptionClick(model)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder<DirItem> =
        DirsViewHolder(inflateLayout(parent, R.layout.rcv_item_dir), this, this)
}