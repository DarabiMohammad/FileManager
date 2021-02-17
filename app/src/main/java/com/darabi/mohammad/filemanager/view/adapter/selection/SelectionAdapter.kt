package com.darabi.mohammad.filemanager.view.adapter.selection

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.selection.SelectionViewHolder

abstract class SelectionAdapter <O: HasSelectable, VH: SelectionViewHolder<O>> internal constructor()
    : BaseAdapter<O, VH> (), AdapterCallback <O> {

    protected abstract fun onSelectionChanged(isAllSelected: Boolean, item: O)

    protected abstract fun onSelectAll(items: ArrayList<O>)

    private var selectedItemCount: Int = 0
    private lateinit var selectableItems: ArrayList<O>

    override fun onBindViewHolder(holder: VH, position: Int) = objects[position].run {
        if (this.isSelectable) holder.bindModel(this, position) else holder.bindModel(this)
    }

    override fun notifyItemSelectionChanged(item: O, position: Int) {
        if (item.isSelected) selectedItemCount++ else selectedItemCount--
        onSelectionChanged(selectedItemCount == selectableItems.size, item)
    }

    override fun hasSelection(): Boolean = selectedItemCount > 0

    override fun setSource(source: List<O>) {
        super.setSource(source)
        setSelectableItems()
    }

    override fun addSource(source: List<O>, position: Int) {
        if (selectableItems.addAll(source.filter { it.isSelectable })) super.addSource(source, position)
    }

    override fun removeSource(source: List<O>) {
        if (selectableItems.removeAll(source)) super.removeSource(source)
        if (selectableItems.isEmpty()) objects.clear()
    }

    fun selectAll() = selectableItems.forEach { it.isSelected = true }.run {
        selectedItemCount = selectableItems.size
        notifyDataSetChanged()
        // todo : fix this, find a better way
        onSelectAll(objects.filter { it.isSelectable } as ArrayList<O>)
//        onSelectAll(selectableItems)
    }

    fun unselectAll() = selectableItems.forEach { it.isSelected = false }.run {
        selectedItemCount = 0
        notifyDataSetChanged()
        onSelectAll(ArrayList())
    }

    private fun setSelectableItems() { selectableItems = objects.filter { it.isSelectable } as ArrayList<O> }
}