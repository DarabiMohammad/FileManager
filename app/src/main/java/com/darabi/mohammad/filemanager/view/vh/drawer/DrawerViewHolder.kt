package com.darabi.mohammad.filemanager.view.vh.drawer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.view.DrawerItem
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class DrawerViewHolder constructor(
    val view: View,
    val callback: OnDrawerItemClickListener?
) : BaseViewHolder<DrawerItem>(view) {

    private val title: TextView = view.findViewById<TextView>(R.id.txt_nav_item_name)
    private val icon: ImageView = view.findViewById(R.id.img_nav_item_icon)

    override fun bindModel(model: DrawerItem) {
        if(model is DrawerItem.Item) {

            title.text = model.title
            Glide.with(view).load(model.imageRes).into(icon)
            if(callback != null)
                view.setOnClickListener { callback.onDrawerItemClick(model.title) }
        }
    }
}