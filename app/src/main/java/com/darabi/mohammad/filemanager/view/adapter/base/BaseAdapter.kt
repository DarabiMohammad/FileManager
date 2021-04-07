package com.darabi.mohammad.filemanager.view.adapter.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<O, VH: BaseViewHolder<O>> internal constructor(): RecyclerView.Adapter<VH>() {

    var callback: OnItemClickListener<O>? = null

    protected val objects = arrayListOf<O>()

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bindModel(objects[position])

    override fun getItemCount(): Int = objects.size

    open fun setSource(source: List<O>) {
        clear()
        objects.addAll(source)
        notifyItemRangeChanged(objects.size -1, source.size)
    }

    open fun addSource(source: List<O>, position: Int) {
        objects.addAll(position, source)
        notifyItemRangeInserted(position, source.size)
    }

    open fun addSource(source: O, position: Int) {
        objects.add(position, source)
        notifyItemInserted(position)
    }

    open fun removeSource(source: List<O>) {
        objects.removeAll(source)
        // todo : use notifyItemRangeRemoved() method in appropreate way instead of notifyDataSetChanged()
        notifyDataSetChanged()
    }

    private fun clear() = objects.clear().also { notifyDataSetChanged() }
}