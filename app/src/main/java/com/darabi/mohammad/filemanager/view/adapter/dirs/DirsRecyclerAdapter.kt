package com.darabi.mohammad.filemanager.view.adapter.dirs

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.EmptyDivider
import com.darabi.mohammad.filemanager.model.FileDivider
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.selection.SelectionAdapter
import com.darabi.mohammad.filemanager.view.vh.selection.SelectionViewHolder
import com.darabi.mohammad.filemanager.view.vh.content.DirsDividerViewHolder
import com.darabi.mohammad.filemanager.view.vh.content.DirsViewHolder
import com.darabi.mohammad.filemanager.view.vh.content.EmptyViewHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsRecyclerAdapter @Inject constructor() : SelectionAdapter<BaseItem, SelectionViewHolder<BaseItem>>(),
    DirsViewHolder.DirsViewHolderCallback<BaseItem> {

    var adapterCallback: DirsAdapterCallback<BaseItem>? = null

    private val dividerViewType = 0
    private val emptyViewType = 1

    override fun getItemViewType(position: Int): Int = when {
        objects[position] is FileDivider -> dividerViewType
        objects[position] is EmptyDivider -> emptyViewType
        else -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder<BaseItem> = when (viewType) {
        dividerViewType -> DirsDividerViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_divider))
        emptyViewType -> EmptyViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_empty))
        else -> DirsViewHolder(inflateLayout(parent, R.layout.rcv_item_dir), this)
    }

    override fun onSelectionChanged(selectedItemCount: Int, isAllSelected: Boolean, item: BaseItem) {
        adapterCallback?.onSelectionChanged(selectedItemCount, isAllSelected, item)
    }

    override fun onSelectAll(selectedItemCount: Int, items: List<BaseItem>) {
        adapterCallback?.onSelectAll(selectedItemCount, items)
    }

    override fun onUnselectAll() {
        adapterCallback?.onUnselectAll()
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