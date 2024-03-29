package com.darabi.mohammad.filemanager.view.adapter.drawer

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseDrawerItem
import com.darabi.mohammad.filemanager.model.Divider
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.BaseViewHolder
import com.darabi.mohammad.filemanager.view.adapter.drawer.DrawerDividerViewHolder
import com.darabi.mohammad.filemanager.view.adapter.drawer.DrawerViewHolder
import javax.inject.Inject

class DrawerRecyclerAdapter @Inject constructor() : BaseAdapter<BaseDrawerItem, BaseViewHolder<BaseDrawerItem>>() {

    private val dividerViewType = 1

    override fun getItemViewType(position: Int): Int = if(objects[position] is Divider) dividerViewType else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseDrawerItem> =
        if(viewType == dividerViewType)
            DrawerDividerViewHolder(inflateLayout(parent, R.layout.rcv_item_nav_divider))
        else
            DrawerViewHolder(inflateLayout(parent, R.layout.rcv_item_nav_drawer), callback)
}