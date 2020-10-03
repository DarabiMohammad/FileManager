package com.darabi.mohammad.filemanager.view.adapter

import android.view.ViewGroup
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.view.DrawerItem
import com.darabi.mohammad.filemanager.util.inflateLayout
import com.darabi.mohammad.filemanager.view.vh.drawer.DrawerViewHolder
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder
import com.darabi.mohammad.filemanager.view.vh.drawer.DrawerDividerViewHolder
import javax.inject.Inject

class DrawerRecyclerAdapter @Inject constructor() : BaseAdapter<DrawerItem, BaseViewHolder<DrawerItem>>() {

    private val dividerViewType = 1

    override fun getItemViewType(position: Int): Int =
        if(objects[position] is DrawerItem.Divider)
            dividerViewType
        else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DrawerItem> =
        if(viewType == dividerViewType)
            DrawerDividerViewHolder(inflateLayout(parent, R.layout.rcv_item_divider))
        else
            DrawerViewHolder(inflateLayout(parent, R.layout.rcv_item_nav_drawer))
}