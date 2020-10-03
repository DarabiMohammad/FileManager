package com.darabi.mohammad.filemanager.view.vh

import android.view.View
import android.widget.TextView
import com.darabi.mohammad.filemanager.R

class RecyclerViewHolder constructor(view: View) : BaseViewHolder<String>(view) {

    val name = view.findViewById<TextView>(R.id.txt_storage_name)

    override fun bindModel(model: String) {
        name.text = model
    }
}