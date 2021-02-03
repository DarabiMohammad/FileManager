package com.darabi.mohammad.filemanager.view.adapter.selection

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.selection.SelectionViewHolder

abstract class SelectionAdapter <O: HasSelectable, VH: SelectionViewHolder<O>> internal constructor()
    : BaseAdapter<O, VH> (), AdapterCallback <O> {

    private var selectedItemCount: Int = 0
    private lateinit var selectableItems: List<O>

    protected abstract fun onSelectionChanged(isAllSelected: Boolean, item: O)

    protected abstract fun onSelectAll(items: List<O>)

    override fun onBindViewHolder(holder: VH, position: Int) = objects[position].run {
        if (this.isSelectable) holder.bindModel(this, position) else holder.bindModel(this)
    }

    override fun notifyItemSelectionChanged(item: O, position: Int) {
        if (item.isSelected) selectedItemCount++ else selectedItemCount--
        onSelectionChanged(isAllSelected(), item)
    }

    override fun hasSelection(): Boolean = selectedItemCount > 0

    override fun setSource(source: List<O>) {
        super.setSource(source)
        updateSelectableItems()
    }

    override fun addSource(source: List<O>, position: Int) {
        super.addSource(source, position)
        updateSelectableItems()
    }

    override fun removeSource(items: List<O>) {
        super.removeSource(items)
        updateSelectableItems()
        if (selectableItems.isEmpty()) objects.clear()
    }

    fun selectAll() {
        selectedItemCount =  selectableItems.size
        selectableItems.forEach { it.isSelected = true }
        notifyDataSetChanged()
        onSelectAll(selectableItems)
    }

    fun unselectAll() {
        selectedItemCount = 0
        selectableItems.forEach { it.isSelected = false }
        notifyDataSetChanged()
        onSelectAll(ArrayList())
    }

    private fun updateSelectableItems() { selectableItems = objects.filter { it.isSelectable } }

    private fun isAllSelected(): Boolean = selectedItemCount == selectableItems.size
}