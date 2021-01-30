package com.darabi.mohammad.filemanager.view.adapter.dirs

import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.vh.content.DirsViewHolder

interface DirsAdapterCallback<I> : OnItemClickListener<I>, DirsViewHolder.DirsMenuClickListener<I> {

    fun onSelectionChanged(selectedItemCount: Int, isAllSelected: Boolean, item: I)

    fun onSelectAll(selectedItemCount: Int, items: List<I>)

    fun onUnselectAll()
}