package com.darabi.mohammad.filemanager.view.vh.checkable

import android.view.View
import com.darabi.mohammad.filemanager.view.adapter.checkable.CheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

abstract class CheckableViewHolder<M> constructor(
    private val view: View,
    private val callback: CheckableViewHolderCallback<M>,
    private val checkableAdapter: CheckableAdapter
) : BaseViewHolder<M>(view) {

    protected fun notifyItemCheckedStateChanged() {
        itemView.isActivated = !itemView.isActivated
        checkableAdapter.onItemCheckedChangeState(adapterPosition, itemView.isActivated)
    }

    override fun bindModel(model: M) {

        view.setOnClickListener {
            if(checkableAdapter.checkedItemCount > 0)
                notifyItemCheckedStateChanged()
            else
                callback.onItemClick(model)
        }

        view.setOnLongClickListener {
            notifyItemCheckedStateChanged()
            return@setOnLongClickListener true
        }
    }

    interface CheckableViewHolderCallback<M> {

        fun onItemClick(model: M)
    }
}