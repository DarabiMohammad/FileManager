package com.darabi.mohammad.filemanager.view.vh

import android.view.View
import android.widget.TextView
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.util.storage.VolumeManager

class HomeRecyclerViewHolder constructor(view: View) : BaseViewHolder<VolumeManager.Volume>(view) {

    val name: TextView = view.findViewById(R.id.txt_storage_name)

    override fun bindModel(model: VolumeManager.Volume) {
        name.text = model.name
    }
}