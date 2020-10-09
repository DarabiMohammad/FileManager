package com.darabi.mohammad.filemanager.view.adapter.checkable

import android.util.SparseBooleanArray
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

abstract class BaseCheckableAdapter<O, VH: CheckableViewHolder<O>> internal constructor()
    : BaseAdapter<O, VH>(), CheckableAdapter, CheckableViewHolder.CheckableViewHolderCallback<O> {

    abstract var adapterCallback: CheckableAdapterCallback<O>

    override var checkedItemCount: Int = 0

    private var checkedItemPositions = SparseBooleanArray()
    
    private val selectedModels = arrayListOf<O>()

    override fun onItemCheckedChangeState(position: Int, isChecked: Boolean) {
        if(!isChecked && checkedItemCount == 0) {
            clearCalculations()
        }
        if(isChecked) {
            selectedModels.add(objects[position])
            checkedItemCount++
        } else {
            selectedModels.remove(objects[position])
            checkedItemCount--
        }
        adapterCallback.onCheckStateChange(selectedModels, checkedItemCount)
    }

    override fun onItemClick(model: O) = adapterCallback.onItemClick(model)

    override fun onMoreOptionClick(model: O) = adapterCallback.onMoreOptionClick(model)

    private fun clearCalculations() {
        checkedItemCount = 0
        checkedItemPositions.clear()
        selectedModels.clear()
    }

    interface CheckableAdapterCallback<M> {

        fun onItemClick(model: M)

        fun onMoreOptionClick(model: M)

        fun onCheckStateChange(models: List<M>, checkedItemCount: Int)
    }

}