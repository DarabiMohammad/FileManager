package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.vh.RecyclerVH
import javax.inject.Inject

class RecyclerAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerVH>() {

    val storageNames = arrayListOf<String>()

    fun setSource(source: ArrayList<String>) {
        storageNames.addAll(source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerVH =
        RecyclerVH(inflateLayout(parent, R.layout.rv_item_storage_name))

    override fun onBindViewHolder(holder: RecyclerVH, position: Int) = holder.bindView(storageNames[position])

    override fun getItemCount(): Int = storageNames.size
}