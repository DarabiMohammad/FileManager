package com.darabi.mohammad.filemanager.view.adapter.checkable

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

abstract class BaseCheckableAdapter<O, VH: CheckableViewHolder<O>> internal constructor() : BaseAdapter<O, VH>(),
    CheckableAdapter, CheckableViewHolder.CheckableViewHolderCallback<O> {

    companion object {
        const val DESTROY_SELECTION_ACTION_MODE = 0
    }

    abstract var adapterCallback: CheckableAdapterCallback<O>

    override var checkedItemCount: Int = 0

    val selectedModelIds = arrayListOf<Int>()

    private val selectedModels = arrayListOf<O>()

    private fun notifyItemCheckedStateChanged() =
        adapterCallback.onCheckStateChange(selectedModels, checkedItemCount, checkedItemCount == objects.size)

    override fun isChecked(position: Int): Boolean = selectedModels.contains(objects[position])

    override fun onItemCheckedChangeState(position: Int, isChecked: Boolean) {
        if (!isChecked && checkedItemCount == 0) {
            clearSelections()
        }
        if(isChecked) {
            selectedModelIds.add(position)
            selectedModels.add(objects[position])
            checkedItemCount++
        } else {
            selectedModelIds.remove(position)
            selectedModels.remove(objects[position])
            checkedItemCount--
        }
        notifyItemCheckedStateChanged()
    }

    override fun onItemClick(model: O) = adapterCallback.onItemClick(model)

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bindModel(objects[position], position)

    fun clearSelections() {
        checkedItemCount = 0
        selectedModels.clear()
        selectedModelIds.forEach {
            notifyItemChanged(it)
        }
        selectedModelIds.clear()
        notifyItemCheckedStateChanged()
    }

    fun selectAll() {
        clearSelections()
        checkedItemCount = objects.size
        selectedModels.addAll(objects)
        objects.indices.forEach { selectedModelIds.add(it) }
        notifyItemCheckedStateChanged()
        notifyDataSetChanged()
    }

    interface CheckableAdapterCallback<M> {

        fun onItemClick(model: M)

        fun onMoreOptionClick(model: M)

        fun onCheckStateChange(models: List<M>, checkedItemCount: Int, isSelectedAll: Boolean)
    }
}