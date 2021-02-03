package com.darabi.mohammad.filemanager.view.adapter.content

import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.vh.content.ContentViewHolder

interface ContentAdapterCallback<I> : OnItemClickListener<I>, ContentViewHolder.DirsMenuClickListener<I> {

    fun onSelectionChanged(isAllSelected: Boolean, item: I)

    fun onSelectAll(items: List<I>)
}