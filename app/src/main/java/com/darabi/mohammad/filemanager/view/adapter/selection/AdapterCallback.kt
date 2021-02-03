package com.darabi.mohammad.filemanager.view.adapter.selection

import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener

interface AdapterCallback <in O: HasSelectable> : OnItemClickListener<O> {

    fun hasSelection(): Boolean

    fun notifyItemSelectionChanged(item: O, position: Int)
}