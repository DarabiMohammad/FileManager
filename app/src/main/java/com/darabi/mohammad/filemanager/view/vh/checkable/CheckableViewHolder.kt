package com.darabi.mohammad.filemanager.view.vh.checkable

import android.view.View
import com.darabi.mohammad.filemanager.view.adapter.checkable.CheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

abstract class CheckableViewHolder<M> constructor(
    private val view: View,
    private val checkableAdapter: CheckableAdapter<M>?
) : BaseViewHolder<M>(view) {

    protected fun notifyItemCheckedStateChanged(position: Int) {
        itemView.isActivated = !itemView.isActivated
        checkableAdapter?.onItemCheckedChangeState(position, itemView.isActivated)
    }

    open fun bindModel(model: M, position: Int) {
        if(checkableAdapter != null) {

            itemView.isActivated = checkableAdapter.isChecked(position)

            view.setOnClickListener {
                if (checkableAdapter.hasCheckedItem())
                    notifyItemCheckedStateChanged(position)
                else
                    checkableAdapter.onItemClick(model)
            }

            view.setOnLongClickListener {
                notifyItemCheckedStateChanged(position)
                return@setOnLongClickListener true
            }
        }
    }
}