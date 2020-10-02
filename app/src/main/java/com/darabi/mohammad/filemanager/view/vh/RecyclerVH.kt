package com.darabi.mohammad.filemanager.view.vh

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darabi.mohammad.filemanager.R

class RecyclerVH constructor(view: View) : RecyclerView.ViewHolder(view) {

    val name = view.findViewById<TextView>(R.id.txt_storage_name)

    fun bindView(storageName: String) {
        name.text = storageName
    }
}