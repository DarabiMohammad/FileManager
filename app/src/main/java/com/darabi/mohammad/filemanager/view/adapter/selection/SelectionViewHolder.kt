package com.darabi.mohammad.filemanager.view.adapter.selection

import android.view.View
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.adapter.base.BaseViewHolder

sealed class SelectionViewHolder <O: HasSelectable> constructor (
    view: View, callback: OnItemClickListener<O>?
) : BaseViewHolder<O>(view, callback) {

    abstract fun bindModel(model: O, position: Int)

    open class UnSelectableViewHolder <O: HasSelectable> constructor(
        view: View, callback: OnItemClickListener<O>?
    ) : SelectionViewHolder<O>(view, callback) {

        override fun bindModel(model: O, position: Int) =
            throw UnsupportedOperationException("Can't perform bindModel(model: O, position: Int) in an unselectable view holder.")
    }

    open class SelectableViewHolder <O: HasSelectable> constructor (
        private val view: View, private val callback: AdapterCallback<O>?
    ) : SelectionViewHolder<O>(view, callback) {

        override fun bindModel(model: O, position: Int) {
            callback?.let {

                itemView.isActivated = model.isSelected

                view.setOnClickListener {
                    if (callback.hasSelection())
                        notifySelectionChanged(model, position)
                    else callback.onItemClick(model)
                }

                view.setOnLongClickListener {
                    notifySelectionChanged(model, position)
                    return@setOnLongClickListener true
                }
            }
        }

        protected fun notifySelectionChanged(item: O, position: Int) {
            item.isSelected = !item.isSelected
            itemView.isActivated = item.isSelected
            callback!!.notifyItemSelectionChanged(item, position)
        }
    }
}