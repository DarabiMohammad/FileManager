package com.darabi.mohammad.filemanager.view.adapter.base

import androidx.recyclerview.widget.RecyclerView
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

abstract class BaseAdapter<O, VH: BaseViewHolder<O>> internal constructor(): RecyclerView.Adapter<VH>() {

    protected val objects = ArrayList<O>()

    fun setSource(source: List<O>) {
        objects.addAll(source)
        notifyItemRangeRemoved(objects.size -1, source.size)
    }

    fun clear() {
        objects.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindModel(objects[position])
    }

    override fun getItemCount(): Int = objects.size
}