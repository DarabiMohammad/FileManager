package com.darabi.mohammad.filemanager.view.adapter.base

import androidx.recyclerview.widget.RecyclerView
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseAdapter<O, VH: BaseViewHolder<O>> internal constructor(): RecyclerView.Adapter<VH>() {

    protected val objects = arrayListOf<O>()

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bindModel(objects[position])

    override fun getItemCount(): Int = objects.size

    open fun setSource(source: List<O>) {
        clear()
        objects.addAll(source)
        notifyItemRangeChanged(objects.size -1, source.size)
    }

    fun addSource(source: List<O>, position: Int) {
        objects.addAll(position, source)
        notifyItemRangeInserted(position, source.size)
    }

    open fun addSource(source: O, position: Int) {
        objects.add(position, source)
        notifyItemInserted(position)
    }

    private fun clear() = objects.clear().also { notifyDataSetChanged() }
}