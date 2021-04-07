package com.darabi.mohammad.filemanager.view.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<in M> constructor(
    private val view: View,
    private val onItemClickListener: OnItemClickListener<M>?
) : RecyclerView.ViewHolder(view) {

    open fun bindModel(model: M) {
        onItemClickListener?.let { callback ->
            view.setOnClickListener { callback.onItemClick(model) }
        }
    }
}