package com.darabi.mohammad.filemanager.view.vh.dir

import android.view.View
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

class EmptyViewHolder constructor(view: View) : CheckableViewHolder<BaseItem>(view, null) {

    override fun bindModel(model: BaseItem) { return }
}