package com.darabi.mohammad.filemanager.view.vh.checkable

import android.view.View
import com.darabi.mohammad.filemanager.view.adapter.checkable.CheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

abstract class CheckableViewHolder<M> constructor(
    private val view: View,
    private val callback: CheckableViewHolderCallback<M>,
    private val checkableAdapter: CheckableAdapter
) : BaseViewHolder<M>(view) {

    protected fun notifyItemCheckedStateChanged(position: Int) {
        itemView.isActivated = !itemView.isActivated
        checkableAdapter.onItemCheckedChangeState(position, itemView.isActivated)
    }

    open fun bindModel(model: M, position: Int) {
        itemView.isActivated = checkableAdapter.isChecked(position)
        view.setOnClickListener {
            if(checkableAdapter.checkedItemCount > 0)
                notifyItemCheckedStateChanged(position)
            else
                callback.onItemClick(model)
        }

        view.setOnLongClickListener {
            notifyItemCheckedStateChanged(position)
            return@setOnLongClickListener true
        }
    }

    interface CheckableViewHolderCallback<M> {

        fun onItemClick(model: M)
    }
}