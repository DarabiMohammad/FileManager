package com.darabi.mohammad.filemanager.view.vh

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<M> constructor(view: View) : RecyclerView.ViewHolder(view) {

    open fun bindModel(model: M) {
        throw IllegalAccessException("Illegal Method Call in BaseViewHoder.")
    }
}