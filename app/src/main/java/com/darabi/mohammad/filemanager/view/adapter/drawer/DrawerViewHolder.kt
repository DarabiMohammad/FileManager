package com.darabi.mohammad.filemanager.view.adapter.drawer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseDrawerItem
import com.darabi.mohammad.filemanager.model.DrawerItem
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.adapter.base.BaseViewHolder

class DrawerViewHolder constructor(
        val view: View,
        callback: OnItemClickListener<BaseDrawerItem>?
) : BaseViewHolder<BaseDrawerItem>(view, callback) {

    private val title: TextView = view.findViewById(R.id.txt_nav_item_name)
    private val icon: ImageView = view.findViewById(R.id.img_nav_item_icon)

    override fun bindModel(model: BaseDrawerItem) {
        if (model is DrawerItem) {
            title.text = model.name
            Glide.with(view).load(model.icon).into(icon)
            super.bindModel(model)
        }
    }
}