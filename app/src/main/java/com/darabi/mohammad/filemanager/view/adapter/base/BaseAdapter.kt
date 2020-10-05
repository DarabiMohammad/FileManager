package com.darabi.mohammad.filemanager.view.adapter.base

import androidx.recyclerview.widget.RecyclerView
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

abstract class BaseAdapter<O, VH: BaseViewHolder<O>> internal constructor(): RecyclerView.Adapter<VH>() {

    protected val objects = ArrayList<O>()

    companion object {
        const val FIRST_POSITION = 0
    }

    fun setSource(source: List<O>) {
        objects.addAll(source)
        notifyItemRangeChanged(objects.size -1, source.size)
    }

    fun addSource(source: List<O>, position: Int) {
        objects.addAll(position, source)
        notifyItemRangeChanged(position, source.size)
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