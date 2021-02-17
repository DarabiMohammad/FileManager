package com.darabi.mohammad.filemanager.view.adapter.content

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import com.darabi.mohammad.filemanager.view.vh.content.FolderViewHolder
import com.darabi.mohammad.filemanager.view.vh.content.NewFolderViewHolder
import javax.inject.Inject

class CopyMoveRecyclerAdapter @Inject constructor() : BaseAdapter<BaseItem, BaseViewHolder<BaseItem>>() {

    private val folderViewType = 1

    override fun getItemViewType(position: Int): Int = when(objects[position]) {
        is EmptyDivider -> position
        else -> folderViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseItem> = when (viewType) {
        folderViewType -> FolderViewHolder(inflateLayout(parent, R.layout.rcv_item_folder), callback)
        else -> NewFolderViewHolder(inflateLayout(parent, R.layout.rcv_item_copy_move_new_folder), callback)
    }

    override fun addSource(source: BaseItem, position: Int) {
        super.addSource(source, position + 1)
    }
}