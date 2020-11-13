package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsDividerViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.DirsViewHolder
import com.darabi.mohammad.filemanager.view.vh.dir.EmptyViewHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsRecyclerAdapter @Inject constructor() : BaseCheckableAdapter<DirItem, CheckableViewHolder<DirItem>>(),
    DirsViewHolder.DirsViewHolderCallback<DirItem> {

    private val dividerViewType = 1
    private val emptyViewHolderType = 2

    override lateinit var adapterCallback: CheckableAdapterCallback<DirItem>

    override fun getItemViewType(position: Int): Int = when {
        objects[position] is DirItem.Divider -> dividerViewType
        objects[position] is DirItem.Empty -> emptyViewHolderType
        else -> 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder<DirItem> = when (viewType) {
        dividerViewType -> DirsDividerViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_divider))
        emptyViewHolderType -> EmptyViewHolder(inflateLayout(parent, R.layout.rcv_item_dir_empty))
        else -> DirsViewHolder(inflateLayout(parent, R.layout.rcv_item_dir), this, this)
    }

    override fun onRenameClick(model: DirItem) = adapterCallback.onRenameClick(model)

    override fun onEncryptClick(model: DirItem) = adapterCallback.onEncryptClick(model)

    override fun onDetailsClick(model: DirItem) = adapterCallback.onDetailsClick(model)

//    interface DirsAdapterCallback<M> : CheckableAdapterCallback<M>, DirsViewHolder.DirsViewHolderCallback<M>
}