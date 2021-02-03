package com.darabi.mohammad.filemanager.view.vh.content

import android.view.View
import android.widget.TextView
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.FileItem
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class FolderViewHolder constructor(
    private val view: View,
    private val callback: OnItemClickListener<BaseItem>?
) : BaseViewHolder<BaseItem>(view, callback) {

    private val folderName: TextView = view.findViewById(R.id.txt_folder)

    override fun bindModel(model: BaseItem) {
        if (model is FileItem) folderName.text = model.name
        super.bindModel(model)
    }
}