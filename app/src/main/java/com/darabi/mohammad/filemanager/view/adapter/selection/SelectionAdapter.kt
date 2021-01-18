package com.darabi.mohammad.filemanager.view.adapter.selection

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.selection.SelectionViewHolder

abstract class SelectionAdapter <O: HasSelectable, VH: SelectionViewHolder<O>> internal constructor()
    : BaseAdapter<O, VH> (), AdapterCallback <O> {

    protected var selectedItemCount: Int = 0
    private var isAllitemSelected: Boolean = false
    private lateinit var selectableItems: ArrayList<O>

    protected abstract fun onSelectionChanged(selectedItemCount: Int, isAllSelected: Boolean, item: O)

    protected abstract fun onSelectAll(selectedItemCount: Int, items: List<O>)

    protected abstract fun onUnselectAll()

    override fun onBindViewHolder(holder: VH, position: Int) = objects[position].run {
        if (this.isSelectable) holder.bindModel(this, position) else holder.bindModel(this)
    }

    override fun notifyItemSelectionChanged(item: O, position: Int) {
        if (item.isSelected) selectedItemCount++ else selectedItemCount--
        isAllitemSelected = selectableItems.size == selectedItemCount
        onSelectionChanged(selectedItemCount, isAllSelected(), item)
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

    protected fun isAllSelected(): Boolean = selectedItemCount == selectableItems.size

    fun selectAll() {
        selectedItemCount =  selectableItems.size
        selectableItems.forEach { it.isSelected = true }
        notifyDataSetChanged()
        onSelectAll(selectedItemCount, selectableItems)
    }

    fun unselectAll() {
        selectedItemCount = 0
        selectableItems.forEach { it.isSelected = false }
        notifyDataSetChanged()
        onUnselectAll()
    }

    private fun updateSelectableItems() { selectableItems = objects.filter { it.isSelectable } as ArrayList<O> }
}