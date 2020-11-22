package com.darabi.mohammad.filemanager.view.vh.dir

import android.view.View
import android.widget.TextView
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

class DirsDividerViewHolder constructor(view: View) : CheckableViewHolder<DirItem>(view, null) {

    private val name: TextView = view.findViewById(R.id.txt_rcv_item_dir_divider_name)

    override fun bindModel(model: DirItem) { return }

    override fun bindModel(model: DirItem, position: Int) {
        if(model is DirItem.Divider)
            name.text = model.itemName
    }
}