package com.darabi.mohammad.filemanager.view.adapter.dirs

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsDividerViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.EmptyViewHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsRecyclerAdapter @Inject constructor() : BaseCheckableAdapter<BaseItem, CheckableViewHolder<BaseItem>>() {

    private val dividerViewType = 0
    private val emptyViewType = 1

    lateinit var adapterCallback: DirsAdapterCallback<FileItem>

    override fun getItemViewType(position: Int): Int = when {
        objects[position] is FileDivider -> dividerViewType
        objects[position] is EmptyDivider -> emptyViewType
        else -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder<BaseItem> = when (viewType) {
        dividerViewType -> DirsDividerViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_divider))
        emptyViewType -> EmptyViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_empty))
        else -> DirsViewHolder(inflateLayout(parent, R.layout.rcv_item_dir), adapterCallback, this)
    }

    override fun onItemClick(item: BaseItem) = adapterCallback.onItemClick(item as FileItem)

    override fun onItemCheckedChangeState(position: Int, isChecked: Boolean) {
        super.onItemCheckedChangeState(position, isChecked)
        adapterCallback.onCheckStateChange(position,isChecked)
    }

    fun setSource(source: List<BaseItem>, maxCheckableItemCount: Int) {
        this.maxCheckableItemCount = maxCheckableItemCount
        super.setSource(source)
    }

    fun addSource(source: FileItem, position: Int) = maxCheckableItemCount++.also {
        super.addSource(source, position)
    }

    override fun selectAll() {
        super.selectAll()
        adapterCallback.onAllSelected()
    }

    override fun clearAll() {
        super.clearAll()
        adapterCallback.onClear()
    }
}