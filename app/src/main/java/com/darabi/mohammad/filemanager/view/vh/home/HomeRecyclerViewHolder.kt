package com.darabi.mohammad.filemanager.view.vh.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.StorageItem
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class HomeRecyclerViewHolder constructor(
    private val view: View,
    private val callback: BaseAdapterCallback<StorageItem>
) : BaseViewHolder<StorageItem>(view) {

    private val name: TextView = view.findViewById(R.id.txt_storage_name)
    private val image: ImageView = view.findViewById(R.id.img_rcv_item_storage_dev_icon)
    private val glide: RequestManager = Glide.with(view)

    override fun bindModel(model: StorageItem) {
        name.text = model.name
        glide.asDrawable().load(model.icon).into(image)
        view.setOnClickListener { callback.onItemClick(model) }
    }
}