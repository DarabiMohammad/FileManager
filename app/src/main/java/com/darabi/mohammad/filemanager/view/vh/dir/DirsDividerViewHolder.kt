package com.darabi.mohammad.filemanager.view.vh.dir

import android.view.View
import android.widget.TextView
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.FileDivider
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

class DirsDividerViewHolder constructor(view: View) : CheckableViewHolder<BaseItem>(view, null) {

    private val name: TextView = view.findViewById(R.id.txt_rcv_item_dir_divider_name)

    override fun bindModel(model: BaseItem) { return }

    override fun bindModel(model: BaseItem, position: Int) {
        if(model is FileDivider) name.text = model.name
    }
}