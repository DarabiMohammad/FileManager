package com.darabi.mohammad.filemanager.view.vh.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class HomeRecyclerViewHolder constructor(
    private val view: View,
    private val callback: BaseAdapterCallback<DirItem.Item>
) : BaseViewHolder<DirItem>(view) {

    private val name: TextView = view.findViewById(R.id.txt_storage_name)
    private val image: ImageView = view.findViewById(R.id.img_rcv_item_storage_dev_icon)
    private val glide: RequestManager = Glide.with(view)

    override fun bindModel(model: DirItem) {
        if(model is DirItem.Item) {
            name.text = model.itemName
            glide.asDrawable().load(model.itemImageRes).into(image)
            view.setOnClickListener { callback.onItemClick(model) }
        }
    }
}