package com.darabi.mohammad.filemanager.view.adapter.content

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.EmptyDivider
import com.darabi.mohammad.filemanager.model.FileDivider
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.selection.SelectionAdapter
import com.darabi.mohammad.filemanager.view.adapter.selection.SelectionViewHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRecyclerAdapter @Inject constructor() : SelectionAdapter<BaseItem, SelectionViewHolder<BaseItem>>(),
    ContentViewHolder.DirsViewHolderCallback<BaseItem> {

    var adapterCallback: ContentAdapterCallback<BaseItem>? = null

    private val dividerViewType = 0
    private val emptyViewType = 1

    override fun getItemViewType(position: Int): Int = when {
        objects[position] is FileDivider -> dividerViewType
        objects[position] is EmptyDivider -> emptyViewType
        else -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder<BaseItem> = when (viewType) {
        dividerViewType -> ContentDividerViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_divider))
        emptyViewType -> EmptyViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_empty))
        else -> ContentViewHolder(inflateLayout(parent, R.layout.rcv_item_content), this)
    }

    override fun onSelectionChanged(isAllSelected: Boolean, item: BaseItem) {
        adapterCallback?.onSelectionChanged(isAllSelected, item)
    }

    override fun onSelectAll(items: ArrayList<BaseItem>) {
        adapterCallback?.onSelectAll(items)
    }

    override fun onItemClick(item: BaseItem) {
        adapterCallback?.onItemClick(item)
    }

    override fun onRenameClick(item: BaseItem) {
        adapterCallback?.onRenameClick(item)
    }

    override fun onEncryptClick(item: BaseItem) {
        adapterCallback?.onEncryptClick(item)
    }

    override fun onDetailsClick(item: BaseItem) {
        adapterCallback?.onDetailsClick(item)
    }
}