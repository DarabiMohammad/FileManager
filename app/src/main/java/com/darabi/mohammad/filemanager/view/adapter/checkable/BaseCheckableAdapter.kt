package com.darabi.mohammad.filemanager.view.adapter.checkable

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

abstract class BaseCheckableAdapter<O, VH: CheckableViewHolder<O>> internal constructor() : BaseAdapter<O, VH>(),
    CheckableAdapter<O> {

    companion object {
        const val DESTROY_SELECTION_ACTION_MODE = 0
    }

    private var checkedItemCount = 0

    private lateinit var checkedItems: MutableList<Boolean>

    protected var maxCheckableItemCount = -1

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bindModel(objects[position], position)

    override fun setSource(source: List<O>) {
        checkedItems = mutableListOf()
        repeat(source.size) { checkedItems.add(false) }
        super.setSource(source)
    }

    override fun hasCheckedItem(): Boolean = checkedItemCount > DESTROY_SELECTION_ACTION_MODE

    override fun isChecked(position: Int): Boolean = checkedItems[position]

    override fun onItemCheckedChangeState(position: Int, isChecked: Boolean) = isChecked.run {
        if(this) checkedItemCount++ else checkedItemCount--
        checkedItems[position] = this
    }

    override fun clearAll() {
        checkedItemCount = 0
        checkedItems.indices.forEach {
            if(checkedItems[it]) {
                checkedItems[it] = false
                notifyItemChanged(it)
            }
        }
    }

    override fun selectAll() {
        checkedItemCount = maxCheckableItemCount
        objects.indices.forEach { checkedItems[it] = true }
        notifyDataSetChanged()
    }
}