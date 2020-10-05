package com.darabi.mohammad.filemanager.view.vh.home

import android.view.View
import android.widget.TextView
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class HomeRecyclerViewHolder constructor(
    private val view: View,
    private val callback: OnHoneVolumeItemsClickListener?
) : BaseViewHolder<DirItem>(view) {

    val name: TextView = view.findViewById(R.id.txt_storage_name)

    override fun bindModel(model: DirItem) {
        if(model is DirItem.Item) {
            name.text = model.itemName
            if(callback != null)
                view.setOnClickListener { callback.onVolumeClick(model) }
        }
    }
}