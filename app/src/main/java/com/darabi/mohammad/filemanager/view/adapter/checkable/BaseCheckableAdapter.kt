package com.darabi.mohammad.filemanager.view.adapter.checkable

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

abstract class BaseCheckableAdapter<O, VH: CheckableViewHolder<O>> internal constructor() : BaseAdapter<O, VH>(),
    CheckableAdapter, CheckableViewHolder.CheckableViewHolderCallback<O> {

    companion object {
        const val SELECTION_ACTION_MODE_DESTROYED = 0
    }

    abstract var adapterCallback: CheckableAdapterCallback<O>

    override var checkedItemCount: Int = 0

    val selectedModelIds = arrayListOf<Int>()

    private val selectedModels = arrayListOf<O>()

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
        adapterCallback.onCheckStateChange(selectedModels, checkedItemCount)
    }

    override fun onItemClick(model: O) = adapterCallback.onItemClick(model)

    fun clearSelections() {
        checkedItemCount = 0
        selectedModels.clear()
        selectedModelIds.clear()
    }

    interface CheckableAdapterCallback<M> {

        fun onItemClick(model: M)

        fun onMoreOptionClick(model: M)

        fun onCheckStateChange(models: List<M>, checkedItemCount: Int)
    }
}