package com.darabi.mohammad.filemanager.view.adapter.content

import android.view.View
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.view.adapter.selection.SelectionViewHolder
import java.lang.UnsupportedOperationException

class EmptyViewHolder constructor(view: View) : SelectionViewHolder.UnSelectableViewHolder<BaseItem>(view, null) {

    override fun bindModel(model: BaseItem, position: Int) =
        throw UnsupportedOperationException("bindModel(model: BaseItem, position: Int) must not be implemented in unselectable view holder.")
}