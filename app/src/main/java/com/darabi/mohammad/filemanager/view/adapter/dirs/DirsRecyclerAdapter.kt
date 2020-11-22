package com.darabi.mohammad.filemanager.view.adapter.dirs

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsDividerViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.EmptyViewHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsRecyclerAdapter @Inject constructor() : BaseCheckableAdapter<DirItem, CheckableViewHolder<DirItem>>() {

    private val dividerViewType = 1
    private val emptyViewHolderType = 2

    lateinit var adapterCallback: DirsAdapterCallback<DirItem>

    override fun getItemViewType(position: Int): Int = when {
        objects[position] is DirItem.Divider -> dividerViewType
        objects[position] is DirItem.Empty -> emptyViewHolderType
        else -> 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder<DirItem> = when (viewType) {
        dividerViewType -> DirsDividerViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_divider))
        emptyViewHolderType -> EmptyViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_empty))
        else -> DirsViewHolder(inflateLayout(parent, R.layout.rcv_item_dir), adapterCallback, this)
    }

    override fun onItemClick(item: DirItem) = adapterCallback.onItemClick(item)

    override fun onItemCheckedChangeState(position: Int, isChecked: Boolean) {
        super.onItemCheckedChangeState(position, isChecked)
        adapterCallback.onCheckStateChange(position,isChecked)
    }

    fun setSource(source: List<DirItem>, maxCheckableItemCount: Int) {
        this.maxCheckableItemCount = maxCheckableItemCount
        super.setSource(source)
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