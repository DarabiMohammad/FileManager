package com.darabi.mohammad.filemanager.view.adapter.content

import android.view.View
import android.widget.TextView
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.FileDivider
import com.darabi.mohammad.filemanager.view.adapter.selection.SelectionViewHolder

class ContentDividerViewHolder constructor(view: View) :
    SelectionViewHolder.UnSelectableViewHolder<BaseItem>(view, null) {

    private val name: TextView = view.findViewById(R.id.txt_rcv_item_dir_divider_name)

    override fun bindModel(model: BaseItem) {
        if (model is FileDivider) name.text = model.name
    }

    override fun bindModel(model: BaseItem, position: Int) =
        throw UnsupportedOperationException("bindModel(model: BaseItem, position: Int) must not be implemented in unselectable view holder.")
}