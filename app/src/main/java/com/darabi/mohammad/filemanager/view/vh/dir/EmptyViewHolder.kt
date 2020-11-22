package com.darabi.mohammad.filemanager.view.vh.dir

import android.view.View
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

class EmptyViewHolder constructor(view: View) : CheckableViewHolder<DirItem>(view, null) {

    override fun bindModel(model: DirItem) { return }
}